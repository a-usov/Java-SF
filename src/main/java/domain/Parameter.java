package domain;

import domain.type.Type;

public class Parameter {
  private final String name;
  private final Type type;

  /**
   * Creates a local parameter.
   *
   * @param name name of parameter
   * @param type type of parameter, currently as a string
   */
  public Parameter(final String name, final Type type) {
    super();
    this.name = name;
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Parameter: " + type + " " + name;
  }
}
