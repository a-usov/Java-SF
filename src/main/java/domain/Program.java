package domain;

import java.util.List;

public class Program {

  private final List<ClassDeclaration> classes;

  public Program(final List<ClassDeclaration> classes) {
    this.classes = classes;
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }
}
