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

    return new Program(classes, ctx.expression());
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
      if (c.isNotResolved()) {
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

          for (final var method : superClass.getMethods().values()) {
            if (c.getMethods().get(method.getName()) != null) {
              reportError("Overwriting field of super class", c.getMethods().get(method.getName()).getToken());
            } else {
              c.addMethod(method);
            }
          }
        }
      }
    }

    generateRelation(program.getClasses().values(), program);

    System.out.println("Relation: " + TypeHelper.SUB_CLASSES);

    final var classVisitor = new ClassVisitor();
    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));

    // TODO check null handled correctly
    var expressionVisitor = new ExpressionVisitor(program, null);
    expressionVisitor.visit(program.getExpression());
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

    for (final var c : program.getClasses().values()) {
      if (c.isNotResolved()) {
        var allResolved = true;

        for (final var field : c.getFields().values()) {
          for (final var type : field.getType().getTypes()) {
            if (type instanceof ClassType) {
              if (type.equals(c.getType())){
                return false;
              }

              if (!program.getClasses().containsKey(type.getName())) {
                reportError("Class " + type.getName() + " does not exist", field.getToken());
              }

              if (program.getClasses().get(type.getName()).isNotResolved()) {
                allResolved = false;
              }
            }
          }
        }

        if (allResolved) {
          c.setResolved(true);
          hasChanged = true;
        }
      }
    }

    return hasChanged;
  }
}
