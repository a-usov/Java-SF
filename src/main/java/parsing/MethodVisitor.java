package parsing;

import static util.TypeResolverUtils.isNotValidSubtype;
import static util.TypeResolverUtils.reportError;

import domain.Method;
import domain.Parameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.BooleanType;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.MethodDeclContext;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final MethodDeclContext ctx) {
    final var typeVisitor = new TypeVisitor();

    final var returnType = typeVisitor.visit(ctx.returntype);

    Parameter parameter;
    if (ctx.paramname != null && ctx.paramtype != null) {
      final var name = ctx.paramname.getText();
      parameter = new Parameter(name, typeVisitor.visit(ctx.paramtype), ctx.paramname);
    } else {
      parameter = new Parameter("", new BooleanType(BasicType.VOID, false), ctx.paramname);
    }

    final var name = ctx.name.getText();

    return new Method(returnType, name, parameter, ctx.expression(), ctx.start);
  }

  /**
   * Second round visit for a method.
   *
   * @param method  method we are visiting
   * @param program whole program context
   */
  public void visit(final Method method, final Program program) {
    final var expressionVisitor = new ExpressionVisitor(program, method.getParameter());
    final var expressionTypes = method.getExpression().accept(expressionVisitor);

    if (isNotValidSubtype(method.getReturnType().getSet(), expressionTypes)) {
      reportError("Return type of expression of method " + method.getName() + " does not match: "
              + expressionTypes + " != " + method.getReturnType(), method.getToken());
    }
  }
}
