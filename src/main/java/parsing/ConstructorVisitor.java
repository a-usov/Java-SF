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
    if (!ctx.constructorname.getText().equals(owner)) {
      reportError("constructor name does not match the class", ctx);
    }

    final var typeVisitor = new TypeVisitor();

    final var parameters = new LinkedHashMap<String, Parameter>();

    if (ctx.type().size() > 0) {
      IntStream.range(0, ctx.type().size()).forEach(i -> {
        final var id = ctx.ID(i + 1);

        final var parameter = new Parameter(id.getText(), typeVisitor.visit(ctx.type(i)), id.getSymbol());
        if (parameters.containsKey(parameter.getName())) {
          reportError("repeated formal parameter: " + parameter.getName(), parameter.getToken());
        } else {
          parameters.put(parameter.getName(), parameter);
        }
      });
    }

    final var superVisitor = new SuperVisitor();
    final List<String> superArgumentNames = superVisitor.visit(ctx.superDecl());

    final List<Parameter> superArguments = new ArrayList<>();
    superArgumentNames.forEach(a -> {
      if (!parameters.containsKey(a)) {
        reportError("Parameter " + a + "doesnt exist in " + "constructor of " + owner, ctx.superDecl().start);
      } else {
        superArguments.add(parameters.get(a));
      }
    });

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
    final String superClassName = program.getClasses().get(owner).getSuperName();

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
        final Parameter parameter = parameterIterator.next();
        final Parameter argument = argumentIterator.next();

        if (isNotValidSubtype(parameter.getType(), argument.getType())) {
          reportError("Type of parameter doesnt match type of super constructor: "
              + argument.getType() + " != " + parameter.getType(), argument.getToken());
        }
      }
    }
  }

  private void checkFieldAssign(final Constructor constructor, final Program program) {
    final var fieldHasBeenAssigned = new HashMap<String, Boolean>();

    program.getClasses().get(owner).getFields().values().forEach(f -> fieldHasBeenAssigned.put(f.getName(), false));

    final String superClassName = program.getClasses().get(owner).getSuperName();
    if (superClassName != null) {
      program.getClasses().get(superClassName).getFields().values()
          .forEach(f -> fieldHasBeenAssigned.put(f.getName(), true));
    }

    for (final var assignment : constructor.getFieldAssignments()) {
      final Field field = program.getClasses().get(owner).getFields().get(assignment.a.getText());
      final Parameter parameter = assignment.b;

      if (field == null) {
        reportError("Field " + assignment.a.getText() + " does not exist", assignment.a);
      } else if (isNotValidSubtype(field.getType(), parameter.getType())) {
        reportError("Field " + assignment.a.getText() + " and parameter " + parameter.getName()
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
