package domain;

import java.util.List;
import java.util.Map;
import jsf.jsfParser.ConstructorDeclContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class Constructor {
  private final Map<String, Parameter> parameterList;
  private final List<Parameter> superParameters;
  private final List<Pair<Token, Token>> fieldAssignments;
  private final ConstructorDeclContext ctx;

  /**
   * Creates a constructor that holds the parameters, parameters passed to super() and assignments.
   *
   * @param parameters       List of parameters passed to constructor
   * @param superParameters  List of parameters passed to super constructor
   * @param fieldAssignments list of Pairs of field and value parameter assigned to field
   */
  public Constructor(final Map<String, Parameter> parameters, final List<Parameter> superParameters,
                     final List<Pair<Token, Token>> fieldAssignments, final ConstructorDeclContext ctx) {
    super();
    this.parameterList = parameters;
    this.superParameters = superParameters;
    this.fieldAssignments = fieldAssignments;
    this.ctx = ctx;
  }

  public Map<String, Parameter> getParameterList() {
    return parameterList;
  }

  public List<Parameter> getSuperParameters() {
    return superParameters;
  }

  public List<Pair<Token, Token>> getFieldAssignments() {
    return fieldAssignments;
  }

  public ConstructorDeclContext getCtx() {
    return ctx;
  }

  @Override
  public String toString() {
    return "Constructor takes arguments : " + parameterList + "\n" + "Super takes arguments" + superParameters + "\n"
        + "fields assigned are: " + fieldAssignments;
  }
}
