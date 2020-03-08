package parsing;

import static util.TypeResolverUtils.isNotValidSubtype;
import static util.TypeResolverUtils.reportError;

import domain.Method;
import domain.MethodParameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.BooleanType;
import domain.type.ClassType;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.MethodDeclContext;
import org.antlr.v4.runtime.misc.Pair;

public class MethodVisitor extends jsfBaseVisitor<Method> {

  @Override
  public Method visitMethodDecl(final MethodDeclContext ctx) {
    final var returnType = new TypeVisitor().visit(ctx.returntype);

    MethodParameter parameter;
    if (ctx.paramname != null && ctx.paramtype != null) {
      final var name = ctx.paramname.getText();

      var type = new MethodTypeVisitor().visit(ctx.paramtype);
      parameter = new MethodParameter(name, type, ctx.paramname);
    } else {
      parameter = new MethodParameter("", new Pair<>(new BooleanType(BasicType.VOID, false), null), ctx.paramname);
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
  public void visit(final Method method, final Program program, final ClassType owner) {
    final var expressionTypes = new ExpressionVisitor(program, method.getParameter(), owner).visit(method.getExpression());

    if (isNotValidSubtype(method.getReturnType().getSet(), expressionTypes)) {
      reportError("Return type of expression in method " + method.getName() + " does not match: " + expressionTypes + " != " + method.getReturnType().getSet(), method.getToken());
    }
  }
}
