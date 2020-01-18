package domain;

import domain.type.Type;
import org.antlr.v4.runtime.Token;

public class Field {
  private final String name;
  private Type type;
  private final Token token;
  private final String typeName;

  /**
   * Object representing a field in a class.
   *
   * @param name name of the field
   * @param type type of the field
   */
  public Field(final String name, final Type type, final Token token, final String typeName) {
    super();
    this.name = name;
    this.type = type;
    this.token = token;
    this.typeName = typeName;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Token getToken() {
    return token;
  }

  public String getTypeName() {
    return typeName;
  }

  @Override
  public String toString() {
    return "Field: " + type + " " + name;
  }
}
