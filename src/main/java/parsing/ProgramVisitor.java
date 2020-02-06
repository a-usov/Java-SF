package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Program;
import domain.type.ClassType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.ProgramContext;
import util.TypeHelper;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  @Override
  public Program visitProgram(final ProgramContext ctx) {
    final var classes = new HashMap<String, Class>();

    final var classVisitor = new ClassVisitor();
    ctx.classDecl().forEach(classCtx -> {
      final var c = classVisitor.visit(classCtx);
      if (classes.containsKey(c.getName())) {
        reportError("repeated class name: " + c.getName(), c.getToken());
      } else {
        classes.put(c.getName(), c);
      }
    });

    return new Program(classes);
  }

  /**
   * Second round visit for a whole program.
   *
   * @param program the program we are visiting
   */
  public void visit(final Program program) {
    var hasChanged = false;
    do {
      hasChanged = resolve(program);
    } while (hasChanged);

    for (final var c : program.getClasses().values()) {
      if (!c.isResolved()) {
        // TODO make error message nicer
        throw new RuntimeException("Cannot resolve the types");
      }
    }

    for (final var c : program.getClasses().values()) {
      if (c.getSuperName() != null) {
        if (program.getClasses().get(c.getSuperName()) == null) {
          reportError("The super class " + c.getSuperName() + " of class " + c.getName()
              + " does not exist", c.getToken());
        } else {
          final var superClass = program.getClasses().get(c.getSuperName());
          for (final var field : superClass.getFields().values()) {
            if (c.getFields().get(field.getName()) != null) {
              reportError("Overwriting field of super class", c.getFields().get(field.getName()).getToken());
            } else {
              c.addField(field);
            }
          }
        }

      }
    }

    generateRelation(program.getClasses().values(), program);

    System.out.println("Relation: " + TypeHelper.SUB_CLASSES);

    final var classVisitor = new ClassVisitor();
    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));
  }

  private void generateRelation(final Collection<Class> classes, final Program program) {
    final var untyped = new ArrayList<Class>();

    for (final var c : classes) {
      if (!TypeHelper.addClass(c, program)) {
        untyped.add(c);
      }
    }

    if (!untyped.isEmpty()) {
      generateRelation(untyped, program);
    }
  }

  private boolean resolve(final Program program) {
    var hasChanged = false;
    var allResolved = true;

    for (final var c : program.getClasses().values()) {
      if (!c.isResolved()) {
        for (final var field : c.getFields().values()) {
          for (final var type : field.getType().getTypes()) {
            if (type instanceof ClassType) {
              if (program.getClasses().get(type.getName()).isResolved()) {
                hasChanged = true;
              } else {
                allResolved = false;
              }
            }
          }
        }

        if (allResolved) {
          c.setResolved(true);
        }
      }
    }

    return hasChanged;
  }
}
