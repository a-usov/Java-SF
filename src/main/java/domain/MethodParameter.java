package domain;

import domain.type.BooleanType;
import domain.type.MethodType;
import java.util.Objects;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class MethodParameter {
  private final String name;
  private final Pair<BooleanType, MethodType> type;
  private final Token token;

  /**
   * Creates a local parameter.
   *
   * @param name name of parameter
   * @param type type of parameter, currently as a string
   */
  public MethodParameter(final String name, final Pair<BooleanType, MethodType> type, final Token token) {
    super();
    this.name = name;
    this.type = type;
    this.token = token;
  }

  public Pair<BooleanType, MethodType> getType() {
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
    MethodParameter parameter = (MethodParameter) o;
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
