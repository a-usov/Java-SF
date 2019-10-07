package domain;

import java.util.Map;

public class Class {
  private final String name;
  private final Map<String, Field> fields;
  private final Constructor constructor;
  private final Map<String, Method> methods;

  /**
   * Creates a class struct that has a name, list of fields, and constructor.
   * @param name name of the class
   * @param fields Set of unique fields
   * @param constructor constructor of class
   */
  public Class(final String name, final Map<String, Field> fields,
               final Constructor constructor, final Map<String, Method> methods) {
    super();
    this.name = name;
    this.fields = fields;
    this.constructor = constructor;
    this.methods = methods;
  }

  public String getName() {
    return name;
  }

  public Constructor getConstructor() {
    return constructor;
  }

  public Map<String, Field> getFields() {
    return fields;
  }

  public Map<String, Method> getMethods() {
    return methods;
  }

  @Override
  public String toString() {
    return "Class: " + name + "\n" + fields + "\n" + constructor + "\n" + methods;
  }
}
