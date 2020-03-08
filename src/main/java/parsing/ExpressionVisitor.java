package parsing;

import static util.TypeResolverUtils.getFromValue;
import static util.TypeResolverUtils.isNotValidSubtype;
import static util.TypeResolverUtils.reportError;

import com.google.common.collect.Sets;
import domain.MethodParameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;
import java.util.HashSet;
import java.util.Set;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.*;

public class ExpressionVisitor extends jsfBaseVisitor<Set<Type>> {

  private final Program program;
  private final MethodParameter parameter;
  private final ClassType currentClass;

  /**
   * Create an expression visitor, done on second round analysis.
   *
   * @param program   whole program context
   * @param parameter the parameter of the method the expression is in
   */
  public ExpressionVisitor(final Program program, final MethodParameter parameter, final ClassType currentClass) {
    super();
    this.program = program;
    this.parameter = parameter;
    this.currentClass = currentClass;
  }

  @Override
  public Set<Type> visitExpression(final ExpressionContext ctx) {
    final var types = ctx.e1.accept(this);

    if (ctx.e2 != null) {

      final var types2 = ctx.e2.accept(this);
      types.retainAll(types2);

      final var hasBasic = types.stream().anyMatch(t -> t instanceof BasicType && !((BasicType) t).name().equals("BOOLEAN"));

      if (!hasBasic) {
        reportError("Mathematical Expressions not supported for custom classes", ctx.start);
      }
    }

    return types;
  }

  @Override
  public Set<Type> visitNum(final NumContext ctx) {
    return Sets.newHashSet(getFromValue(ctx.NUMBER().getText()));
  }

  @Override
  public Set<Type> visitDecimal(final DecimalContext ctx) {
    return Sets.newHashSet(getFromValue(ctx.DECIMAL().getText()));
  }

  @Override
  public Set<Type> visitTrue(final TrueContext ctx) {
    return Sets.newHashSet(BasicType.BOOLEAN);
  }

  @Override
  public Set<Type> visitFalse(final FalseContext ctx) {
    return Sets.newHashSet(BasicType.BOOLEAN);
  }

  @Override
  public Set<Type> visitVar(final VarContext ctx) {
    if (parameter == null || !ctx.ID().getText().equals(parameter.getName())) {
      reportError("Referring to parameter that is not defined", ctx.start);
    }

    return parameter.getType().a != null ? parameter.getType().a.getSet() : parameter.getType().b.getTypes(program);
  }

  @Override
  public Set<Type> visitField(final FieldContext ctx) {
    final var primExpression = ctx.primExpression().accept(this);

    final var onlyBasic = primExpression.stream().anyMatch(t -> t instanceof ClassType);

    if (!onlyBasic) {
      throw new RuntimeException("Cannot access field of a type with only base types");
    } else {
      final var types = new HashSet<Type>();

      primExpression.forEach(t -> {
        if (t instanceof ClassType) {
          final var type = (ClassType) t;
          final var field = program.getClasses().get(type.getName()).getFields().get(ctx.ID().getText());

          if (field != null) {
            types.addAll(field.getType().getSet());
          }
        }
      });

      if (types.isEmpty()) {
        throw new RuntimeException("None of the possible types have this field");
      } else {
        return types;
      }
    }
  }

  @Override
  public Set<Type> visitMethod(final MethodContext ctx) {
    final var primExpression = ctx.primExpression().accept(this);

    final var onlyBasic = primExpression.stream().anyMatch(t -> t instanceof ClassType);
    if (!onlyBasic) {
      reportError("Cannot access method of a type with only base types: " + primExpression, ctx.start);
    }

    final Set<Type> methodArguments = ctx.expression() == null ? Sets.newHashSet(BasicType.VOID) : ctx.expression().accept(this);
    final var returnTypes = new HashSet<Type>();

    primExpression.forEach(t -> {
      if (t instanceof ClassType) {
        final var type = (ClassType) t;
        var currentClass = program.getClasses().get(type.getName());

        do {
          final var method = currentClass.getMethods().get(ctx.ID().getText());

          if (method != null) {
            final var argumentTypes = method.getParameter().getType().a != null ? method.getParameter().getType().a.getSet() : method.getParameter().getType().b.getTypes(program);

            if (!isNotValidSubtype(argumentTypes, methodArguments)) {
              returnTypes.addAll(method.getReturnType().getSet());
            }
          }

          currentClass = currentClass.getSuperName() == null ? null : program.getClasses().get(currentClass.getSuperName());
        } while (currentClass != null);
      }
    });

    if (returnTypes.isEmpty()) {
      // TODO improve message
      reportError("None of the possible types have this method", ctx.start);
    }

    return returnTypes;
  }

  @Override
  public Set<Type> visitThis(ThisContext ctx) {
    if (this.currentClass == null) {
      reportError("Trying to refer to 'this' outside of a method", ctx.start);
    }
    return Sets.newHashSet(this.currentClass);
  }

  @Override
  public Set<Type> visitObject(final ObjectContext ctx) {
    final var c = program.getClasses().get(ctx.ID().getText());

    if (c == null) {
      reportError("A class called " + ctx.ID().getText() + " does not exist", ctx.start);
    }
    if (c.getConstructor().getParameterList().size() != ctx.expression().size()) {
      reportError("Not passing enough arguments to constructor of class", ctx.start);
    }

    final var paramIterator = c.getConstructor().getParameterList().values().iterator();
    final var argumentIterator = ctx.expression().iterator();

    while (paramIterator.hasNext() && argumentIterator.hasNext()) {
      final var parameter = paramIterator.next();
      final var argument = argumentIterator.next();

      final var argumentTypes = argument.accept(this);

      if (isNotValidSubtype(parameter.getType(), argumentTypes)) {
        throw new RuntimeException("method argument and parameter types do not match up");
      }
    }

    return Sets.newHashSet(c.getType());
  }
}

