package parsing;

import jsf.jsfBaseVisitor;
import jsf.jsfParser;

// TODO - finish this visitor
public class ExpressionVisitor extends jsfBaseVisitor<Object> {
  @Override
  public Object visitExpression(final jsfParser.ExpressionContext ctx) {
    ctx.e1.accept(this);

    String type = "none";

    if (ctx.op != null && ctx.e2 != null) {
      type = ctx.op.getText();
      ctx.e2.accept(this);
    }

    System.out.println("Operation: " + type);
    return null;
  }

  @Override
  public Object visitNum(final jsfParser.NumContext ctx) {
    return null;
  }

  @Override
  public Object visitVar(final jsfParser.VarContext ctx) {
    return null;
  }

  @Override
  public Object visitField(final jsfParser.FieldContext ctx) {
    return null;
  }

  @Override
  public Object visitMethod(final jsfParser.MethodContext ctx) {
    return null;
  }

  @Override
  public Object visitObject(final jsfParser.ObjectContext ctx) {
    return null;
  }

}
