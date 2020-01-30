package domain;

import domain.type.BooleanType;
import org.antlr.v4.runtime.Token;

public class Field {
  private final String name;
  private BooleanType type;
  private final Token token;

  /**
   * Object representing a field in a class.
   *
   * @param name name of the field
   * @param type type of the field
   */
  public Field(final String name, final BooleanType type, final Token token) {
    super();
    this.name = name;
    this.type = type;
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public BooleanType getType() {
    return type;
  }

  public void setType(final BooleanType type) {
    this.type = type;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return "Field: " + type + " " + name;
  }
}
