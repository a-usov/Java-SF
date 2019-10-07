package parsing;

import static util.TypeResolver.getFromTypeName;

import domain.Method;
import domain.type.BasicType;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import org.antlr.v4.runtime.misc.Pair;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final jsfParser.MethodDeclContext ctx) {
    final Type returnType = getFromTypeName(ctx.returntype.getText());

    Pair<Type, String> parameter = new Pair<>(BasicType.VOID, "void");
    if (ctx.paramname != null && ctx.paramtype != null) {
      parameter = new Pair<>(getFromTypeName(ctx.paramtype.getText()), ctx.paramname.getText());
    }

    final String name = ctx.name.getText();

    final ExpressionVisitor expressionVisitor = new ExpressionVisitor();
    ctx.expression().accept(expressionVisitor);

    return new Method(returnType, name, parameter);
  }
}
