package domain;

import domain.type.Type;
import org.antlr.v4.runtime.misc.Pair;

public class Method {

  private final Type returnType;
  private final String name;
  private final Pair<Type, String> parameter;

  /**
   * Creates a method, with a return type, name and single parameter.
   * @param returnType a Type which can be basic or a custom class
   * @param name method name as String
   * @param parameter pair of the parameter type and parameter name
   */
  public Method(final Type returnType, final String name, final Pair<Type, String> parameter) {
    this.returnType = returnType;
    this.name = name;
    this.parameter = parameter;
  }

  public String getName() {
    return name;
  }

  public Pair<Type, String> getParameter() {
    return parameter;
  }

  public Type getReturnType() {
    return returnType;
  }

  @Override
  public String toString() {
    return "Method: " + returnType + " " + name + "(" + parameter + ")";
  }
}
