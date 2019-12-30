package domain;

import domain.type.Type;
import jsf.jsfParser.ExpressionContext;
import jsf.jsfParser.MethodDeclContext;

public class Method {

  // TODO - Encapsulate return and parameter type
  private final Type returnType;
  private final String name;
  private final Parameter parameter;
  private final ExpressionContext expression;
  private final MethodDeclContext ctx;

  /**
   * Creates a method, with a return type, name and single parameter.
   *
   * @param returnType a Type which can be basic or a custom class
   * @param name       method name as String
   * @param parameter  parameter that method takes
   * @param exprCtx    the expression context within method that we store to type check later
   */
  public Method(final Type returnType, final String name, final Parameter parameter,
                final ExpressionContext exprCtx, final MethodDeclContext methodCtx) {
    this.returnType = returnType;
    this.name = name;
    this.parameter = parameter;
    this.expression = exprCtx;
    this.ctx = methodCtx;
  }

  public String getName() {
    return name;
  }

  public Parameter getParameter() {
    return parameter;
  }

  public Type getReturnType() {
    return returnType;
  }

  public ExpressionContext getExpression() {
    return expression;
  }

  public MethodDeclContext getCtx() {
    return ctx;
  }

  @Override
  public String toString() {
    return "Method: " + returnType + " " + name + "(" + parameter + ")";
  }
}
