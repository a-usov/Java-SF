package parsing;

import domain.Parameter;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.SuperDeclContext;

import java.util.ArrayList;
import java.util.List;

public class SuperVisitor extends jsfBaseVisitor<List<Parameter>> {

  @Override
  public List<Parameter> visitSuperDecl(final SuperDeclContext ctx) {
    final var parameters = new ArrayList<Parameter>();
    ctx.ID().forEach(p -> parameters.add(new Parameter(p.getText(), null, p.getSymbol())));
    return parameters;
  }
}
