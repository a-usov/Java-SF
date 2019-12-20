package domain;

import domain.type.Type;
import jsf.jsfParser.FieldDeclContext;

public class Field {
  private final String name;
  private final Type type;
  private final FieldDeclContext ctx;

  /**
   * Object representing a field in a class.
   *
   * @param name name of the field
   * @param type type of the field
   */
  public Field(final String name, final Type type, FieldDeclContext ctx) {
    super();
    this.name = name;
    this.type = type;
    this.ctx = ctx;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public FieldDeclContext getCtx() {
    return ctx;
  }

  @Override
  public String toString() {
    return "Field: " + type + " " + name;
  }
}
