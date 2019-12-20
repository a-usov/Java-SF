package parsing;

import domain.Field;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.FieldDeclContext;

import static util.TypeResolverUtils.getFromTypeName;

public class FieldVisitor extends jsfBaseVisitor<Field> {

  @Override
  public Field visitFieldDecl(final FieldDeclContext ctx) {
    final String name = ctx.ID().getText();
    final Type type = getFromTypeName(ctx.type().getText());

    return new Field(name, type, ctx);
  }
}
