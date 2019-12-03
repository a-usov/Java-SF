package domain;

import domain.type.Type;

public class Field {
  private final String name;
  private final Type type;

  /**
   * Object representing a field in a class.
   *
   * @param name name of the field
   * @param type type of the field
   */
  public Field(final String name, final Type type) {
    super();
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Field: " + type + " " + name;
  }
}
