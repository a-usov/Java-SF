package parsing;

import domain.Field;
import domain.type.BooleanType;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.FieldDeclContext;

public class FieldVisitor extends jsfBaseVisitor<Field> {

  @Override
  public Field visitFieldDecl(final FieldDeclContext ctx) {
    final String name = ctx.ID().getText();

    final var typeVisitor = new TypeVisitor();
    final BooleanType type = typeVisitor.visit(ctx.type());

    return new Field(name, type, ctx.start);
  }
}
