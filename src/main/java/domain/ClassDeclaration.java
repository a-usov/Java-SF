package domain;

public class ClassDeclaration {
  private final String name;

  public ClassDeclaration(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Class: " + name;
  }
}
