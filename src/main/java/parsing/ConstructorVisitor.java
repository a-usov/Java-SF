package parsing;

import domain.Constructor;
import domain.Field;
import domain.Parameter;
import domain.Program;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;
import java.util.stream.IntStream;

import static util.TypeResolverUtils.getFromTypeName;
import static util.TypeResolverUtils.reportError;

public class ConstructorVisitor extends jsfBaseVisitor<Constructor> {

  private final String owner;

  public ConstructorVisitor(final String owner) {
    super();
    this.owner = owner;
  }

  @Override
  public Constructor visitConstructorDecl(final jsfParser.ConstructorDeclContext ctx) {
    final Map<String, Parameter> parameters = new LinkedHashMap<>();
    final List<Pair<String, String>> fieldAssignments = new ArrayList<>();

    if (!ctx.constructorname.getText().equals(owner)) {
      reportError("constructor name does not match the class", ctx);
    }

    if (ctx.type().size() > 0) {
      IntStream.range(0, ctx.type().size()).forEach(i -> {
        final Parameter parameter = new Parameter(
            ctx.ID(i + 1).getText(), getFromTypeName(ctx.type(i).getText()));

        if (parameters.containsKey(parameter.getName())) {
          throw new RuntimeException(
              reportError("repeated formal parameter: " + parameter.getName(), ctx));
        } else {
          parameters.put(parameter.getName(), parameter);
        }

      });
    }

    final SuperVisitor superVisitor = new SuperVisitor();
    final List<Parameter> superParameters = ctx.superDecl().accept(superVisitor);

    final FieldAssignmentVisitor fieldAssignmentVisitor = new FieldAssignmentVisitor();
    ctx.fieldAssignment().forEach(a -> fieldAssignments.add(a.accept(fieldAssignmentVisitor)));

    return new Constructor(parameters, superParameters, fieldAssignments);
  }

  /**
   * Second round visit of constructor.
   * @param constructor constructor being visited
   * @param program whole program context
   */
  public void visit(final Constructor constructor, final Program program) {
    checkSuperCall(constructor, program, owner);
    checkFieldAssign(constructor, program, owner);
  }

  private void checkSuperCall(final Constructor constructor, final Program program,
                              final String owner) {
    final String superClassName = program.getClasses().get(owner).getSuperName();

    if ("".equals(superClassName)) {
      if (constructor.getSuperParameters().size() > 0) {
        throw new RuntimeException("Passing more variables to constructor than it takes");
      }
    } else {
      final var superClassParameters = program.getClasses().get(superClassName)
          .getConstructor().getParameterList().values();

      if (constructor.getSuperParameters().size() != superClassParameters.size()) {
        throw new RuntimeException("Passing wrong number of arguments");
      }

      final var superIterator = superClassParameters.iterator();
      final var parameterIterator = constructor.getSuperParameters().iterator();

      while (superIterator.hasNext() && parameterIterator.hasNext()) {
        final Parameter superParam = superIterator.next();
        final Parameter formalParam = parameterIterator.next();

        final Parameter actualParam = constructor.getParameterList().get(formalParam.getName());

        if (actualParam == null) {
          throw new RuntimeException("Variable doesnt exist: " + formalParam.getName() + " in "
              + "constructor call of " + owner);
        } else {
          if (superParam.getType() != actualParam.getType()) {
            throw new RuntimeException("constructor type doesnt match super: "
                + actualParam.getType() + " != " + superParam.getType());
          }
        }
      }
    }
  }

  private void checkFieldAssign(final Constructor constructor, final Program program,
                                final String owner) {
    final Map<String, Boolean> fieldSet = new HashMap<>();

    program.getClasses().get(owner).getFields().values()
        .forEach(f -> fieldSet.put(f.getName(), false));

    for (final var assignment : constructor.getFieldAssignments()) {
      final Field field = program.getClasses().get(owner).getFields().get(assignment.a);
      final Parameter parameter = constructor.getParameterList().get(assignment.b);

      if (field == null) {
        throw new RuntimeException("Field " + assignment.a + " does not exist");
      } else if (parameter == null) {
        throw new RuntimeException("Parameter " + assignment.b + " does not exist");
      } else if (field.getType() != parameter.getType()) {
        throw new RuntimeException("Field " + assignment.a + " and parameter " + assignment.b
            + " have different types: " + field.getType() + " != " + parameter.getType());
      }

      fieldSet.put(field.getName(), true);
    }

    final StringBuilder errorMessage = new StringBuilder();
    for (final var field : fieldSet.entrySet()) {
      if (!field.getValue()) {
        errorMessage.append("Field ").append(field.getKey()).append(" has not been set, ");
      }
    }
    if (errorMessage.length() != 0) {
      throw new RuntimeException(errorMessage.toString());
    }

  }
}
