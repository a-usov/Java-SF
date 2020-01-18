package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jsf.jsfBaseVisitor;
import jsf.jsfParser.ProgramContext;
import util.TyperHelper;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  @Override
  public Program visitProgram(final ProgramContext ctx) {
    final var classes = new HashMap<String, Class>();

    final var classVisitor = new ClassVisitor();
    ctx.classDecl().forEach(classCtx -> {
      final Class c = classCtx.accept(classVisitor);
      if (classes.containsKey(c.getName())) {
        reportError("repeated class name: " + c.getName(), c.getToken());
      } else {
        classes.put(c.getName(), c);
      }
    });

    final var program = new Program(classes);
    System.out.println(program);
    return program;
  }

  /**
   * Second round visit for a whole program.
   *
   * @param program the program we are visiting
   */
  public void visit(final Program program) {
    var hasChanged = false;
    do
      hasChanged = resolve(program);
    while (hasChanged);

    for (var c : program.getClasses().values()) {
      if (!c.isResolved()) {
        // TODO make error message nicer
        throw new RuntimeException("Cannot resolve the types");
      }
    }

    generateRelation(program.getClasses().values(), program);

    System.out.println("Relation: " + TyperHelper.SUB_CLASSES);
    System.out.println("Universe: " + TyperHelper.UNIVERSE_SET);

    final var classVisitor = new ClassVisitor();
    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));

    System.out.println(program);
  }

  private void generateRelation(final Collection<Class> classes, Program program) {
    final var untyped = new ArrayList<Class>();

    for (final var c : classes) {
      if (!TyperHelper.addClass(c, program)) {
        untyped.add(c);
      }
    }

    if (!untyped.isEmpty()) {
      generateRelation(untyped, program);
    }
  }

  private boolean resolve(Program program) {
    var hasChanged = false;

    for (var c : program.getClasses().values()) {
      if (!c.isResolved()) {
        for (var f : c.getFields().values()) {
          var typeName = f.getTypeName();
          if (f.getType() == null && program.getClasses().get(typeName).isResolved()) {
            f.setType(program.getClasses().get(typeName).getType());
            hasChanged = true;
          }
        }

        for (var m : c.getMethods().values()) {
          var typeName = m.getReturnTypeName();
          if (m.getReturnType() == null && program.getClasses().get(typeName).isResolved()) {
            m.setReturnType(program.getClasses().get(typeName).getType());
            hasChanged = true;
          }
        }

        var hasBeenResolved = true;

        for (var f : c.getFields().values()) {
          if (f.getType() == null) {
            hasBeenResolved = false;
            break;
          }
        }

        for (var m : c.getMethods().values()) {
          if (m.getReturnType() == null) {
            hasBeenResolved = false;
            break;
          }
        }

        if (hasBeenResolved) {
          c.setResolved(true);
        }
      }
    }
    return hasChanged;
  }
}
