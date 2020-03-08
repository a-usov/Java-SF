package parsing;

import static util.TypeResolverUtils.isNotValidSubtype;
import static util.TypeResolverUtils.reportError;

import domain.Constructor;
import domain.Field;
import domain.Parameter;
import domain.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    if (!ctx.constructorname.getText().equals(owner)) {
      reportError("constructor name does not match the class", ctx);
    }

    final var typeVisitor = new TypeVisitor();

    // Constructor Parameters --------------------------------------------------------

    final var parameters = new LinkedHashMap<String, Parameter>();

    if (ctx.type().size() > 0) {
      IntStream.range(0, ctx.type().size()).forEach(i -> {
        final var id = ctx.ID(i + 1);

        final var parameter = new Parameter(id.getText(), typeVisitor.visit(ctx.type(i)), id.getSymbol());

        for (var t : parameter.getType().getTypes()) {
          if (t.getName().equals(owner)) {
            reportError("type of parameter in constructor cant be itself", parameter.getToken());
          }
        }

        if (parameters.containsKey(parameter.getName())) {
          reportError("repeated formal parameter: " + parameter.getName(), parameter.getToken());
        } else {
          parameters.put(parameter.getName(), parameter);
        }
      });
    }

    // Super call arguments ---------------------------------------------

    final var superArgumentNames = new SuperVisitor().visit(ctx.superDecl());

    final var superArguments = new ArrayList<Parameter>();
    superArgumentNames.forEach(a -> {
      if (!parameters.containsKey(a)) {
        reportError(a + "doesnt exist in " + "constructor parameters of " + owner, ctx.superDecl().start);
      } else {
        superArguments.add(parameters.get(a));
      }
    });

    // Field assignments -----------------------------------------------

    final var fieldAssignments = new ArrayList<Pair<Token, Parameter>>();
    final var fieldAssignmentVisitor = new FieldAssignmentVisitor();

    ctx.fieldAssignment().forEach(a -> {
      final var pair = fieldAssignmentVisitor.visit(a);

      if (!parameters.containsKey(pair.b.getText())) {
        reportError("Parameter " + pair.b.getText() + " does not exist", pair.b);
      } else {
        fieldAssignments.add(new Pair<>(pair.a, parameters.get(pair.b.getText())));
      }
    });

    return new Constructor(parameters, superArguments, fieldAssignments, ctx.constructorname);
  }

  /**
   * Second round visit of constructor.
   *
   * @param constructor constructor being visited
   * @param program     whole program context
   */
  public void visit(final Constructor constructor, final Program program) {
    checkSuperCall(constructor, program);
    checkFieldAssign(constructor, program);
  }

  private void checkSuperCall(final Constructor constructor, final Program program) {
    final var superClassName = program.getClasses().get(owner).getSuperName();

    if (superClassName == null) {
      if (constructor.getSuperArguments().size() > 0) {
        reportError("Passing more arguments to constructor super call than it takes", constructor.getToken());
      }
    } else {
      final var superParameters = program.getClasses().get(superClassName).getConstructor().getParameterList().values();

      if (constructor.getSuperArguments().size() != superParameters.size()) {
        reportError("Passing wrong number of arguments to super call of constructor", constructor.getToken());
      }

      final var parameterIterator = superParameters.iterator();
      final var argumentIterator = constructor.getSuperArguments().iterator();

      while (parameterIterator.hasNext() && argumentIterator.hasNext()) {
        final var parameter = parameterIterator.next();
        final var argument = argumentIterator.next();

        if (isNotValidSubtype(parameter.getType(), argument.getType())) {
          reportError("Type of parameter doesnt match type of super constructor: "
              + argument.getType().getSet() + " != " + parameter.getType().getSet(), argument.getToken());
        }
      }
    }
  }

  private void checkFieldAssign(final Constructor constructor, final Program program) {
    final var fieldAssigned = new HashMap<Field, Boolean>();

    program.getClasses().get(owner).getFields().values().forEach(f -> fieldAssigned.put(f, false));

    final var superClassName = program.getClasses().get(owner).getSuperName();
    if (superClassName != null) {
      program.getClasses().get(superClassName).getFields().values().forEach(f -> fieldAssigned.put(f, true));
    }

    for (final var assignment : constructor.getFieldAssignments()) {
      final var field = program.getClasses().get(owner).getFields().get(assignment.a.getText());
      final var parameter = assignment.b;

      if (field == null) {
        reportError("Field " + assignment.a.getText() + " does not exist", assignment.a);
      } else if (isNotValidSubtype(field.getType(), parameter.getType())) {
        reportError("Field " + assignment.a.getText() + " and parameter " + parameter.getName() +
            " have different types: " + field.getType().getSet() + " != " + parameter.getType().getSet(), assignment.a);
      } else {
        fieldAssigned.put(field, true);
      }
    }

    for (final var field : fieldAssigned.entrySet()) {
      if (!field.getValue()) {
        reportError("Field + " + field.getKey() + " has not been set,", field.getKey().getToken());
      }
    }

  }
}
