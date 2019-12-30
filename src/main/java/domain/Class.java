package domain;

import domain.type.ClassType;
import java.util.Map;
import jsf.jsfParser.ClassDeclContext;

public class Class {
  private final String name;
  private final ClassType type;
  private final Map<String, Field> fields;
  private final Constructor constructor;
  private final Map<String, Method> methods;
  private final String superName;
  private final ClassDeclContext ctx;

  /**
   * Creates a class struct that has a name, list of fields, and constructor.
   *
   * @param name        name of the class
   * @param fields      Set of unique fields
   * @param constructor constructor of class
   */
  public Class(final String name, final Map<String, Field> fields, final Constructor constructor,
               final Map<String, Method> methods, final String superName, final ClassDeclContext ctx) {
    super();
    this.name = name;
    this.type = new ClassType(name, fields, methods);
    this.fields = fields;
    this.constructor = constructor;
    this.methods = methods;
    this.superName = superName;
    this.ctx = ctx;
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

  public String getSuperName() {
    return superName;
  }

  public ClassType getType() {
    return type;
  }

  public ClassDeclContext getCtx() {
    return ctx;
  }

  @Override
  public String toString() {
    return "Class: " + name + " extends: " + superName + "\n" + fields + "\n" + constructor + "\n" + methods;
  }
}
