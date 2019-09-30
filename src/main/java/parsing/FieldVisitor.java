package parsing;

import domain.Field;
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
  public Field visitFielddecl(final jsfParser.FielddeclContext ctx) {
    final String name = ctx.ID().getText();
    final String type = ctx.objecttype().getText();
    final String owner = getOwner();

    return new Field(name, type, owner);
  }
}
