package parsing;

import domain.Method;
import domain.Parameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

import static util.TypeResolverUtils.getFromTypeName;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final jsfParser.MethodDeclContext ctx) {
    final Type returnType = getFromTypeName(ctx.returntype.getText());

    Parameter parameter = new Parameter("empty", BasicType.VOID);
    if (ctx.paramname != null && ctx.paramtype != null) {
      parameter = new Parameter(ctx.paramname.getText(), getFromTypeName(ctx.paramtype.getText()));
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
