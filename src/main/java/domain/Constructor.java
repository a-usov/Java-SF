package domain;

import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class Constructor {
  private final Map<String, Parameter> parameterList;
  private final List<Parameter> superArguments;
  private final List<Pair<Token, Parameter>> fieldAssignments;
  private final Token token;

  /**
   * Creates a constructor that holds the parameters, parameters passed to super() and assignments.
   *
   * @param parameters       List of parameters passed to constructor
   * @param superArguments   List of parameters passed to super constructor
   * @param fieldAssignments List of Pairs of field and value parameter assigned to field
   */
  public Constructor(final Map<String, Parameter> parameters, final List<Parameter> superArguments,
                     final List<Pair<Token, Parameter>> fieldAssignments, final Token token) {
    super();
    this.parameterList = parameters;
    this.superArguments = superArguments;
    this.fieldAssignments = fieldAssignments;
    this.token = token;
  }

  public Map<String, Parameter> getParameterList() {
    return parameterList;
  }

  public List<Parameter> getSuperArguments() {
    return superArguments;
  }

  public List<Pair<Token, Parameter>> getFieldAssignments() {
    return fieldAssignments;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return "Constructor takes arguments : " + parameterList.values() + "\n" + "Super takes arguments :"
        + superArguments + "\n" + "fields assigned are: " + fieldAssignments;
  }
}
