package parsing;

import static util.TypeResolver.getFromTypeName;

import domain.Field;
import domain.type.Type;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class FieldVisitor extends jsfBaseVisitor<Field> {

  private final String owner;

  public FieldVisitor(final String owner) {
    super();
    this.owner = owner;
  }

  public String getOwner() {
    return owner;
  }

  @Override
  public Field visitFieldDecl(final jsfParser.FieldDeclContext ctx) {
    final String name = ctx.ID().getText();
    final Type type = getFromTypeName(ctx.type().getText());
    final String owner = getOwner();

    return new Field(name, type, owner);
  }
}
