package parsing;

import domain.Field;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.FieldDeclContext;

public class FieldVisitor extends jsfBaseVisitor<Field> {

  @Override
  public Field visitFieldDecl(final FieldDeclContext ctx) {
    final var name = ctx.ID().getText();
    final var type = new TypeVisitor().visit(ctx.type());

    return new Field(name, type, ctx.start);
  }
}
