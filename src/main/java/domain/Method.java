package domain;

import domain.type.BooleanType;
import jsf.jsfParser.ExpressionContext;
import org.antlr.v4.runtime.Token;

public class Method {

  private final String name;
  private final MethodParameter parameter;
  private final ExpressionContext expression;
  private final Token token;
  // TODO - Encapsulate return and parameter type
  private BooleanType returnType;

  /**
   * Creates a method, with a return type, name and single parameter.
   *
   * @param returnType a Type which can be basic or a custom class
   * @param name       method name as String
   * @param parameter  parameter that method takes
   * @param ctx        the expression context within method that we store to type check later
   */
  public Method(final BooleanType returnType, final String name, final MethodParameter parameter,
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

  public MethodParameter getParameter() {
    return parameter;
  }

  public BooleanType getReturnType() {
    return returnType;
  }

  public void setReturnType(final BooleanType type) {
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
