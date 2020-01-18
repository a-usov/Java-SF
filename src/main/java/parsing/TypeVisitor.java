package parsing;

import static util.TypeResolverUtils.getFromTypeName;

import domain.type.BooleanType;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class TypeVisitor extends jsfBaseVisitor<BooleanType> {
  @Override
  public BooleanType visitBasic(jsfParser.BasicContext ctx) {
    return new BooleanType(getFromTypeName(ctx.getText()), false);
  }

  @Override
  public BooleanType visitClass(jsfParser.ClassContext ctx) {
    return new BooleanType(getFromTypeName(ctx.getText()), false);
  }

  @Override
  public BooleanType visitNotClass(jsfParser.NotClassContext ctx) {
    return new BooleanType(getFromTypeName(ctx.getText()), true);
  }

  @Override
  public BooleanType visitBoolean(jsfParser.BooleanContext ctx) {
    BooleanType type1 = visit(ctx.type1);
    BooleanType type2 = visit(ctx.type2);

    BooleanType.BooleanConnective connective;

    if (ctx.bool.getText().equals("and")) {
      connective = BooleanType.BooleanConnective.INTERS;
    } else {
      connective = BooleanType.BooleanConnective.UNION;
    }

    return new BooleanType(connective, type1, type2);
  }
}
