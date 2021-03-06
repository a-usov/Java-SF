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
  private boolean resolved;

  /**
   * Creates a class struct that has a name, list of fields, and constructor.
   *
   * @param name        name of the class
   * @param fields      Set of unique fields
   * @param constructor constructor of class
   */
  public Class(final String name, final Map<String, Field> fields, final Constructor constructor,
               final Map<String, Method> methods, final String superName, final Token ctx, final boolean resolved) {
    super();
    this.type = new ClassType(name);
    this.fields = fields;
    this.constructor = constructor;
    this.methods = methods;
    this.superName = superName;
    this.token = ctx;
    this.resolved = resolved;
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

  public void addField(final Field field) {
    this.getFields().put(field.getName(), field);
  }

  public Map<String, Method> getMethods() {
    return methods;
  }

  public void addMethod(final Method method) {
    this.getMethods().put(method.getName(), method);
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

  public boolean isNotResolved() {
    return !resolved;
  }

  public void setResolved(final boolean resolved) {
    this.resolved = resolved;
  }

  @Override
  public String toString() {
    return "Class: " + this.type.getName() + " extends: " + superName + "\n" + fields.values() + "\n" + constructor
        + "\n" + methods.values() + "\n" + "isResolved: " + resolved;
  }
}
