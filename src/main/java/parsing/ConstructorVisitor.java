package parsing;

import static util.TypeResolverUtils.getFromTypeName;
import static util.TypeResolverUtils.reportError;

import domain.Constructor;
import domain.Field;
import domain.Parameter;
import domain.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.ConstructorDeclContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class ConstructorVisitor extends jsfBaseVisitor<Constructor> {

  private final String owner;

  public ConstructorVisitor(final String owner) {
    super();
    this.owner = owner;
  }

  @Override
  public Constructor visitConstructorDecl(final ConstructorDeclContext ctx) {
    final var parameters = new LinkedHashMap<String, Parameter>();
    final var fieldAssignments = new ArrayList<Pair<Token, Token>>();

    if (!ctx.constructorname.getText().equals(owner)) {
      reportError("constructor name does not match the class", ctx);
    }

    if (ctx.type().size() > 0) {
      IntStream.range(0, ctx.type().size()).forEach(i -> {
        final var id = ctx.ID(i + 1);

        final var parameter = new Parameter(id.getText(), getFromTypeName(ctx.type(i).getText()), id.getSymbol());
        if (parameters.containsKey(parameter.getName())) {
          reportError("repeated formal parameter: " + parameter.getName(), parameter.getToken());
        } else {
          parameters.put(parameter.getName(), parameter);
        }
      });
    }

    final var superVisitor = new SuperVisitor();
    final List<Parameter> superParameters = ctx.superDecl().accept(superVisitor);

    final var fieldAssignmentVisitor = new FieldAssignmentVisitor();
    ctx.fieldAssignment().forEach(a -> fieldAssignments.add(a.accept(fieldAssignmentVisitor)));

    return new Constructor(parameters, superParameters, fieldAssignments, ctx.constructorname);
  }

  /**
   * Second round visit of constructor.
   *
   * @param constructor constructor being visited
   * @param program     whole program context
   */
  public void visit(final Constructor constructor, final Program program) {
    checkSuperCall(constructor, program, owner);
    checkFieldAssign(constructor, program, owner);
  }

  private void checkSuperCall(final Constructor constructor, final Program program, final String owner) {
    final String superClassName = program.getClasses().get(owner).getSuperName();

    if (superClassName == null) {
      if (constructor.getSuperParameters().size() > 0) {
        reportError("Passing more variables to constructor super call than it takes", constructor.getToken());
      }
    } else {
      final var superParameters = program.getClasses().get(superClassName).getConstructor().getParameterList().values();

      if (constructor.getSuperParameters().size() != superParameters.size()) {
        reportError("Passing wrong number of arguments to super call of constructor", constructor.getToken());
      }

      final var superIterator = superParameters.iterator();
      final var parameterIterator = constructor.getSuperParameters().iterator();

      while (superIterator.hasNext() && parameterIterator.hasNext()) {
        final Parameter superParam = superIterator.next();
        final Parameter formalParam = parameterIterator.next();

        final Parameter actualParam = constructor.getParameterList().get(formalParam.getName());

        if (actualParam == null) {
          reportError("Parameter + " + formalParam.getName() + "doesnt exist in "
              + "constructor of " + owner, formalParam.getToken());
        } else {
          if (superParam.getType() != actualParam.getType()) {
            reportError("Type of parameter doesnt match type of super constructor: "
                + actualParam.getType() + " != " + superParam.getType(), actualParam.getToken());
          }
        }
      }
    }
  }

  private void checkFieldAssign(final Constructor constructor, final Program program, final String owner) {
    final var fieldHasBeenAssigned = new HashMap<String, Boolean>();

    program.getClasses().get(owner).getFields().values().forEach(f -> fieldHasBeenAssigned.put(f.getName(), false));

    final String superClassName = program.getClasses().get(owner).getSuperName();
    if (superClassName != null) {
      program.getClasses().get(superClassName).getFields().values()
          .forEach(f -> fieldHasBeenAssigned.put(f.getName(), true));
    }

    for (final var assignment : constructor.getFieldAssignments()) {
      final Field field = program.getClasses().get(owner).getFields().get(assignment.a.getText());
      final Parameter parameter = constructor.getParameterList().get(assignment.b.getText());

      if (field == null) {
        reportError("Field " + assignment.a.getText() + " does not exist", assignment.a);
      } else if (parameter == null) {
        reportError("Parameter " + assignment.b.getText() + " does not exist", assignment.b);
      } else if (!field.getType().getSet().contains(parameter.getType())) {
        reportError("Field " + assignment.a.getText() + " and parameter " + assignment.b.getText()
            + " have different types: " + field.getType() + " != " + parameter.getType(), assignment.a);
      } else {
        fieldHasBeenAssigned.put(field.getName(), true);
      }
    }

    for (final var field : fieldHasBeenAssigned.entrySet()) {
      if (!field.getValue()) {
        reportError("Field + " + field.getKey() + " has not been set,",
            program.getClasses().get(owner).getFields().get(field.getKey()).getToken());
      }
    }

  }
}
