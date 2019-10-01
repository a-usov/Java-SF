package parsing;

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
  private final List<Parameter> parameters = new ArrayList<>();
  private final List<Pair<String, String>> fieldAssignments = new ArrayList<>();

  public ConstructorVisitor(final String owner) {
    super();
    this.owner = owner;
  }

  @Override
  public Constructor visitConstructordecl(final jsfParser.ConstructordeclContext ctx) {
    if (!ctx.constructorname.getText().equals(owner)) {
      System.out.println("Oops");
    }

    if (ctx.objecttype().size() > 0) {
      IntStream.range(0, ctx.objecttype().size()).forEach(i -> {
        parameters.add(new Parameter(ctx.ID(i + 1).getText(), ctx.objecttype(i).getText()));
      });
    }

    final SuperVisitor superVisitor = new SuperVisitor();
    final List<Parameter> superParameters = ctx.superdecl().accept(superVisitor);

    final FieldAssignmentVisitor fieldAssignmentVisitor = new FieldAssignmentVisitor();
    ctx.fieldassignment().forEach(a -> {
      fieldAssignments.add(a.accept(fieldAssignmentVisitor));
    });

    return new Constructor(parameters, superParameters, fieldAssignments);
  }

  public String getOwner() {
    return owner;
  }

  public List<Pair<String, String>> getFieldAssignments() {
    return fieldAssignments;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }
}
