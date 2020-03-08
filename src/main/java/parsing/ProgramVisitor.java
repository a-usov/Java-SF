package parsing;

import static java.util.stream.Collectors.toList;
import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Program;
import domain.type.ClassType;
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

    var unresolved = program.getClasses().values().stream().filter(Class::isNotResolved).collect(toList());
    if (!unresolved.isEmpty()) {
      throw new RuntimeException("Cannot resolve these types: " + unresolved);
    }

    for (final var c : program.getClasses().values()) {
      var superClass = c.getSuperName() == null ? null : program.getClasses().get(c.getSuperName());

      while (superClass != null) {
        if (program.getClasses().get(superClass.getName()) == null) {
          reportError("The super class " + c.getSuperName() + " does not exist", c.getToken());
        } else {
          for (final var field : superClass.getFields().values()) {
            if (c.getFields().get(field.getName()) == null) {
              c.addField(field);
            } else {
              reportError("Overwriting field of super class", c.getFields().get(field.getName()).getToken());
            }
          }

          for (final var method : superClass.getMethods().values()) {
            if (c.getMethods().get(method.getName()) == null) {
              c.addMethod(method);
            }
          }
        }

        superClass = superClass.getSuperName() == null ? null : program.getClasses().get(superClass.getSuperName());
      }
    }

    generateRelation(program.getClasses().values(), program);

    System.out.println("Relation: " + TypeHelper.SUB_CLASSES);

    final var classVisitor = new ClassVisitor();
    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));

    new ExpressionVisitor(program, null, null).visit(program.getExpression());
  }

  private void generateRelation(final Collection<Class> classes, final Program program) {
    var untyped = classes.stream().filter(c -> !TypeHelper.addClass(c, program)).collect(toList());

    if (!untyped.isEmpty()) {
      generateRelation(untyped, program);
    }
  }

  private boolean resolve(final Program program) {
    return program.getClasses().values().stream().filter(Class::isNotResolved).anyMatch(c -> {
      var allResolved = true;

      for (final var field : c.getFields().values()) {
        for (final var type : field.getType().getTypes()) {
          if (type instanceof ClassType) {
            if (type.equals(c.getType())) {
              reportError("Class " + type.getName() + " is used in own field declaration", field.getToken());
            } else if (!program.getClasses().containsKey(type.getName())) {
              reportError("Class " + type.getName() + " does not exist", field.getToken());
            } else if (program.getClasses().get(type.getName()).isNotResolved()) {
              allResolved = false;
            }
          }
        }
      }

      if (allResolved) {
        c.setResolved(true);
      }

      return allResolved;
    });
  }
}
