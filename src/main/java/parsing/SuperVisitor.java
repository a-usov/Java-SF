package parsing;

import domain.Parameter;
import java.util.ArrayList;
import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class SuperVisitor extends jsfBaseVisitor<List<Parameter>> {

  @Override
  public List<Parameter> visitSuperdecl(final jsfParser.SuperdeclContext ctx) {
    final List<Parameter> parameters = new ArrayList<>();
    ctx.ID().forEach(p -> parameters.add(new Parameter(p.getText(), "infer")));
    return parameters;
  }
}
