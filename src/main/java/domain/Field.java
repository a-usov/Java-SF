package domain;

public class Field {
  private final String name;
  private final String owner;
  private final String type;

  /**
   * Object representing a field in a class.
   * @param name name of the field
   * @param type type of the field
   * @param owner owning class of the field
   */
  public Field(final String name, final String type, final String owner) {
    super();
    this.name = name;
    this.owner = owner;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getOwner() {
    return owner;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return type + " " + name + ", owner: " + owner;
  }
}
