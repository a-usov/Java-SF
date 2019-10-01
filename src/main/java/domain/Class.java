package domain;

import java.util.Set;

public class Class {
  private final String name;
  private final Set<Field> fields;
  private final Constructor constructor;

  /**
   * Creates a class struct that has a name, list of fields, and constructor.
   * @param name name of the class
   * @param fields Set of unique fields
   * @param constructor constructor of class
   */
  public Class(final String name, final Set<Field> fields, final Constructor constructor) {
    super();
    this.name = name;
    this.fields = fields;
    this.constructor = constructor;
  }

  public String getName() {
    return name;
  }

  public Constructor getConstructor() {
    return constructor;
  }

  public Set<Field> getFields() {
    return fields;
  }

  @Override
  public String toString() {
    return "Class: " + name + "\n" + fields + "\n" + constructor;
  }
}
