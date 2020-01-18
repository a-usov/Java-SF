package parsing;

import static util.TypeResolverUtils.getFromTypeName;
import static util.TypeResolverUtils.reportError;

import domain.Method;
import domain.Parameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.MethodDeclContext;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final MethodDeclContext ctx) {
    final Type returnType = getFromTypeName(ctx.returntype.getText());

    Parameter parameter = new Parameter("empty", BasicType.VOID, ctx.paramname);
    if (ctx.paramname != null && ctx.paramtype != null) {
      final var name = ctx.paramname.getText();
      parameter = new Parameter(name, getFromTypeName(name), ctx.paramname);
    }

    final String name = ctx.name.getText();

    return new Method(returnType, name, parameter, ctx.expression(), ctx.start, ctx.returntype.getText());
  }

  /**
   * Second round visit for a method.
   *
   * @param method  method we are visiting
   * @param program whole program context
   */
  public void visit(final Method method, final Program program) {
    final var expressionVisitor = new ExpressionVisitor(program, method.getParameter());
    final Type typeExpression = method.getExpression().accept(expressionVisitor);

    if (!typeExpression.equals(method.getReturnType())) {
      reportError("Return type of expression of method " + method.getName() + " does not match: "
              + typeExpression + " != " + method.getReturnType(), method.getToken());
    }
  }
}
