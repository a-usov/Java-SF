package parsing;

import static util.TypeResolver.getFromTypeName;

import domain.Parameter;
import java.util.ArrayList;
import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class SuperVisitor extends jsfBaseVisitor<List<Parameter>> {

  @Override
  public List<Parameter> visitSuperDecl(final jsfParser.SuperDeclContext ctx) {
    final List<Parameter> parameters = new ArrayList<>();
    ctx.ID().forEach(p -> parameters.add(new Parameter("infer", getFromTypeName(p.getText()))));
    return parameters;
  }
}
