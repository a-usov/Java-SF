package domain;

import domain.type.BooleanType;
import java.util.Objects;
import org.antlr.v4.runtime.Token;

public class Parameter {
  private final String name;
  private final BooleanType type;
  private final Token token;

  /**
   * Creates a local parameter.
   *
   * @param name name of parameter
   * @param type type of parameter, currently as a string
   */
  public Parameter(final String name, final BooleanType type, final Token token) {
    super();
    this.name = name;
    this.type = type;
    this.token = token;
  }

  public BooleanType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Parameter parameter = (Parameter) o;
    return Objects.equals(type, parameter.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }

  @Override
  public String toString() {
    return "Parameter: " + type + " " + name;
  }
}
