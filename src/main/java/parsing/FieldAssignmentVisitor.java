package parsing;

import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import org.antlr.v4.runtime.misc.Pair;

public class FieldAssignmentVisitor extends jsfBaseVisitor<Pair<String, String>> {

  @Override
  public Pair<String, String> visitFieldassignment(final jsfParser.FieldassignmentContext ctx) {
    return new Pair<>(ctx.field.getText(), ctx.parameter.getText());
  }
}
