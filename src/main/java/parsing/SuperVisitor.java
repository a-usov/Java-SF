package parsing;

import domain.Parameter;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

import java.util.ArrayList;
import java.util.List;

public class SuperVisitor extends jsfBaseVisitor<List<Parameter>> {

  @Override
  public List<Parameter> visitSuperDecl(final jsfParser.SuperDeclContext ctx) {
    final List<Parameter> parameters = new ArrayList<>();
    ctx.ID().forEach(p -> parameters.add(new Parameter(p.getText(), null)));
    return parameters;
  }
}
