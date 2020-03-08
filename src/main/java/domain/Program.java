package domain;

import java.util.Map;
import jsf.jsfParser.ExpressionContext;

public class Program {

  private final Map<String, Class> classes;
  private final ExpressionContext expression;

  /**
   * Create a program with a set of classes, and expression to evaluate.
   *
   * @param classes    set of classes of the program
   * @param expression expression of the program to evaluate
   */
  public Program(final Map<String, Class> classes, ExpressionContext expression) {
    super();
    this.classes = classes;
    this.expression = expression;
  }

  public Map<String, Class> getClasses() {
    return classes;
  }

  public ExpressionContext getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    final var output = new StringBuilder();
    for (final var c : classes.values()) {
      output.append(c).append("\n\n");
    }
    return output.toString();
  }
}
