package domain;

import domain.type.Type;
import jsf.jsfParser.ExpressionContext;
import org.antlr.v4.runtime.Token;

public class Method {

  // TODO - Encapsulate return and parameter type
  private Type returnType;
  private final String name;
  private final Parameter parameter;
  private final ExpressionContext expression;
  private final Token token;

  /**
   * Creates a method, with a return type, name and single parameter.
   *
   * @param returnType a Type which can be basic or a custom class
   * @param name       method name as String
   * @param parameter  parameter that method takes
   * @param ctx        the expression context within method that we store to type check later
   */
  public Method(final Type returnType, final String name, final Parameter parameter,
                final ExpressionContext ctx, final Token token) {
    this.returnType = returnType;
    this.name = name;
    this.parameter = parameter;
    this.expression = ctx;
    this.token = token;
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

  public void setReturnType(final Type type) {
    this.returnType = type;
  }

  public ExpressionContext getExpression() {
    return expression;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return "Method: " + returnType + " " + name + "(" + parameter + ")";
  }
}
