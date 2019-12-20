package domain;

import domain.type.Type;
import org.antlr.v4.runtime.Token;

public class Parameter {
  private final String name;
  private final Type type;
  private Token ctx;

  /**
   * Creates a local parameter.
   *
   * @param name name of parameter
   * @param type type of parameter, currently as a string
   */
  public Parameter(final String name, final Type type, final Token ctx) {
    super();
    this.name = name;
    this.type = type;
    this.ctx = ctx;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public Token getCtx() {
    return ctx;
  }

  @Override
  public String toString() {
    return "Parameter: " + type + " " + name;
  }
}
