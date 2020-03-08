package parsing;

import static util.TypeResolverUtils.getFromTypeName;

import domain.type.BooleanType;
import domain.type.BooleanType.BooleanConnective;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.BasicContext;
import jsf.jsfParser.BooleanContext;
import jsf.jsfParser.ClassContext;
import jsf.jsfParser.NotClassContext;

public class TypeVisitor extends jsfBaseVisitor<BooleanType> {
  @Override
  public BooleanType visitBasic(final BasicContext ctx) {
    return new BooleanType(getFromTypeName(ctx.getText()), false);
  }

  @Override
  public BooleanType visitClass(final ClassContext ctx) {
    return new BooleanType(getFromTypeName(ctx.getText()), false);
  }

  @Override
  public BooleanType visitNotClass(final NotClassContext ctx) {
    return new BooleanType(getFromTypeName(ctx.classlbl.getText()), true);
  }

  @Override
  public BooleanType visitBoolean(final BooleanContext ctx) {
    final var type1 = visit(ctx.type1);
    final var type2 = visit(ctx.type2);

    BooleanConnective connective = ctx.bool.getText().equals("and") ? BooleanConnective.INTERS : BooleanConnective.UNION;

    return new BooleanType(connective, type1, type2);
  }
}
