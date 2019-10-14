package parsing;

import static util.TypeResolverUtils.getFromTypeName;

import domain.Method;
import domain.Program;
import domain.type.BasicType;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import org.antlr.v4.runtime.misc.Pair;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final jsfParser.MethodDeclContext ctx) {
    final Type returnType = getFromTypeName(ctx.returntype.getText());

    Pair<Type, String> parameter = new Pair<>(BasicType.VOID, "empty");
    if (ctx.paramname != null && ctx.paramtype != null) {
      parameter = new Pair<>(getFromTypeName(ctx.paramtype.getText()), ctx.paramname.getText());
    }

    final String name = ctx.name.getText();

    return new Method(returnType, name, parameter, ctx.expression());
  }

  /**
   * Second round visit for a method.
   * @param method method we are visiting
   * @param program whole program context
   */
  public void visit(final Method method, final Program program) {
    final var expressionVisitor = new ExpressionVisitor(program, method.getParameter());
    final Type typeExpression = method.getExpression().accept(expressionVisitor);

    if (!typeExpression.getName().equals(method.getReturnType().getName())) {
      throw new RuntimeException("Return type of expression of method " + method.getName()
          + " does not match: " + typeExpression + " != " + method.getReturnType());
    }
  }
}
