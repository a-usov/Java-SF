package domain;

import java.util.List;

public class Program {

  private final List<Class> classes;

  public Program(final List<Class> classes) {
    super();
    this.classes = classes;
  }

  public List<Class> getClasses() {
    return classes;
  }

  @Override
  public String toString() {
    final StringBuilder output = new StringBuilder();
    for (final var eachclass : classes) {
      output.append(eachclass).append("\n\n");
    }
    return output.toString();
  }
}
