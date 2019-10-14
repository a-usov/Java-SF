package domain;

import java.util.Map;

public class Program {

  private final Map<String, Class> classes;

  public Program(final Map<String, Class> classes) {
    super();
    this.classes = classes;
  }

  public Map<String, Class> getClasses() {
    return classes;
  }

  @Override
  public String toString() {
    final StringBuilder output = new StringBuilder();
    for (final var eachclass : classes.values()) {
      output.append(eachclass).append("\n\n");
    }
    return output.toString();
  }
}
