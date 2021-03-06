package parsing;

import domain.type.BasicType;
import domain.type.BooleanType;
import domain.type.MethodType;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.MethodtypeContext;
import jsf.jsfParser.NormalContext;
import org.antlr.v4.runtime.misc.Pair;

public class MethodTypeVisitor extends jsfBaseVisitor<Pair<BooleanType, MethodType>> {

  @Override
  public Pair<BooleanType, MethodType> visitNormal(NormalContext ctx) {
    var type = new TypeVisitor().visit(ctx.type());
    return new Pair<>(type, null);
  }

  @Override
  public Pair<BooleanType, MethodType> visitMethodtype(MethodtypeContext ctx) {
    var typeVisitor = new TypeVisitor();

    BooleanType param;
    if (ctx.proper != null) {
      param = typeVisitor.visit(ctx.proper);
    } else {
      param = new BooleanType(BasicType.VOID, false);
    }
    var returnType = typeVisitor.visit(ctx.returnType);

    boolean isNot = ctx.NOT() != null;

    var methodType = new MethodType(ctx.ID().getText(), param, returnType, isNot);
    return new Pair<>(null, methodType);
  }
}
