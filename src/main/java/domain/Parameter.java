package domain;

public class Parameter {
  private final String name;
  private final String type;

  /**
   * Creates a local parameter.
   * @param name name of parameter
   * @param type type of parameter, currently as a string
   */
  public Parameter(final String name, final String type) {
    super();
    this.name = name;
    this.type = type;
  }

  public String getType() {
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
