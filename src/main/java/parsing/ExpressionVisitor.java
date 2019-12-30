package parsing;

import static util.TypeResolverUtils.getFromValue;

import domain.Method;
import domain.Parameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.ExpressionContext;
import jsf.jsfParser.FieldContext;
import jsf.jsfParser.MethodContext;
import jsf.jsfParser.NumContext;
import jsf.jsfParser.ObjectContext;
import jsf.jsfParser.VarContext;

public class ExpressionVisitor extends jsfBaseVisitor<Type> {

  private final Program program;
  private final Parameter parameter;

  /**
   * Create an expression visitor, done on second round analysis.
   *
   * @param program   whole program context
   * @param parameter the parameter of the method the expression is in
   */
  public ExpressionVisitor(final Program program, final Parameter parameter) {
    super();
    this.program = program;
    this.parameter = parameter;
  }

  @Override
  public Type visitExpression(final ExpressionContext ctx) {
    final Type type = ctx.e1.accept(this);

    Type type2 = null;
    if (ctx.op != null && ctx.e2 != null) {
      type2 = ctx.e2.accept(this);
    }

    // TODO addition is not defined for non basic types
    if (type2 != null) {
      if (type.equals(type2)) {
        if (type.getClass() != BasicType.class) {
          throw new RuntimeException("Mathematical Expressions not supported for custom classes");
        }
      } else {
        throw new RuntimeException("Types of expression don't match");
      }
    }

    return type;
  }

  @Override
  public Type visitNum(final NumContext ctx) {
    return getFromValue(ctx.NUMBER().getText());
  }

  @Override
  public Type visitVar(final VarContext ctx) {
    if (!ctx.ID().getText().equals(parameter.getName())) {
      throw new RuntimeException("Referring to parameter that is not defined");
    }
    return parameter.getType();
  }

  @Override
  public Type visitField(final FieldContext ctx) {
    final Type subExpression = ctx.primExpression().accept(this);
    if (subExpression.getClass() == BasicType.class) {
      throw new RuntimeException("Cannot access field of a basic type");
    } else {
      final var classes = program.getClasses();
      final var fieldName = ctx.ID().getText();

      if (!classes.containsKey(subExpression.getName())) {
        throw new RuntimeException("Type of class field access has not been defined");
      } else if (!classes.get(subExpression.getName()).getFields().containsKey(fieldName)) {
        throw new RuntimeException("Field being accessed does not exist");
      } else {
        return classes.get(subExpression.getName()).getFields().get(fieldName).getType();
      }
    }
  }

  @Override
  public Type visitMethod(final MethodContext ctx) {
    final Type subExpression = ctx.primExpression().accept(this);

    if (subExpression.getClass() == BasicType.class) {
      throw new RuntimeException("Cannot access method of a basic type");
    }

    final var classes = program.getClasses();
    final var methodName = ctx.ID().getText();

    if (!classes.containsKey(subExpression.getName())) {
      throw new RuntimeException("Type of class method access has not been defined");
    } else if (!classes.get(subExpression.getName()).getMethods().containsKey(methodName)) {
      throw new RuntimeException("Method being accessed does not exist");
    }

    final Method calledMethod = classes.get(subExpression.getName()).getMethods().get(methodName);
    Type methodCallValue = ctx.expression().accept(this);

    if (methodCallValue == null) {
      methodCallValue = BasicType.VOID;
    }

    if (calledMethod.getParameter().getType() != methodCallValue) {
      throw new RuntimeException("Expression passed to method is of wrong type");
    }

    return calledMethod.getReturnType();
  }

  @Override
  public Type visitObject(final ObjectContext ctx) {
    return new ClassType(ctx.ID().getText());
  }
}
