package parsing;

import static util.TypeResolver.getFromTypeName;

import domain.Constructor;
import domain.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import org.antlr.v4.runtime.misc.Pair;

public class ConstructorVisitor extends jsfBaseVisitor<Constructor> {

  private final String owner;

  public ConstructorVisitor(final String owner) {
    super();
    this.owner = owner;
  }

  @Override
  public Constructor visitConstructorDecl(final jsfParser.ConstructorDeclContext ctx) {
    final List<Parameter> parameters = new ArrayList<>();
    final List<Pair<String, String>> fieldAssignments = new ArrayList<>();


    if (!ctx.constructorname.getText().equals(owner)) {
      System.out.println("Oops");
    }

    if (ctx.type().size() > 0) {
      IntStream.range(0, ctx.type().size()).forEach(i ->
          parameters.add(new Parameter(
              ctx.ID(i + 1).getText(), getFromTypeName(ctx.type(i).getText()))));
    }

    final SuperVisitor superVisitor = new SuperVisitor();
    final List<Parameter> superParameters = ctx.superDecl().accept(superVisitor);

    final FieldAssignmentVisitor fieldAssignmentVisitor = new FieldAssignmentVisitor();
    ctx.fieldAssignment().forEach(a -> fieldAssignments.add(a.accept(fieldAssignmentVisitor)));

    return new Constructor(parameters, superParameters, fieldAssignments);
  }

  public String getOwner() {
    return owner;
  }
}
