package parsing;

import java.util.ArrayList;
import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.SuperDeclContext;


public class SuperVisitor extends jsfBaseVisitor<List<String>> {

  @Override
  public List<String> visitSuperDecl(final SuperDeclContext ctx) {
    final var parameters = new ArrayList<String>();
    ctx.ID().forEach(p -> parameters.add(p.getText()));
    return parameters;
  }
}
