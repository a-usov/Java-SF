package domain;

import domain.type.ClassType;

import java.util.Map;

import org.antlr.v4.runtime.Token;

public class Class {
  private final ClassType type;

  private final Map<String, Field> fields;
  private final Constructor constructor;
  private final Map<String, Method> methods;
  private final String superName;

  private final Token token;
  private boolean isResolved;

  /**
   * Creates a class struct that has a name, list of fields, and constructor.
   *
   * @param name        name of the class
   * @param fields      Set of unique fields
   * @param constructor constructor of class
   */
  public Class(final String name, final Map<String, Field> fields, final Constructor constructor,
               final Map<String, Method> methods, final String superName, final Token ctx,
               boolean isResolved) {
    super();
    this.type = new ClassType(name);
    this.fields = fields;
    this.constructor = constructor;
    this.methods = methods;
    this.superName = superName;
    this.token = ctx;
    this.isResolved = isResolved;
  }

  public String getName() {
    return this.type.getName();
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

  public Token getToken() {
    return token;
  }

  public boolean isResolved() {
    return isResolved;
  }

  public void setResolved(boolean resolved) {
    this.isResolved = resolved;
  }

  @Override
  public String toString() {
    return "Class: " + this.type.getName() + " extends: " + superName + "\n" + fields + "\n" + constructor + "\n"
            + methods + "\n" + "isResolved: " + isResolved;
  }
}
