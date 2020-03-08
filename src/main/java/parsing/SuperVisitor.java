package parsing;

import static java.util.stream.Collectors.toList;

import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.SuperDeclContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class SuperVisitor extends jsfBaseVisitor<List<String>> {

  @Override
  public List<String> visitSuperDecl(final SuperDeclContext ctx) {
    return ctx.ID().stream().map(ParseTree::getText).collect(toList());
  }
}
