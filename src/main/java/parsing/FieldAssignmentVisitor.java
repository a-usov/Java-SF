package parsing;

import jsf.jsfBaseVisitor;
import jsf.jsfParser.FieldAssignmentContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class FieldAssignmentVisitor extends jsfBaseVisitor<Pair<Token, Token>> {

  @Override
  public Pair<Token, Token> visitFieldAssignment(final FieldAssignmentContext ctx) {
    return new Pair<>(ctx.field, ctx.parameter);
  }
}
