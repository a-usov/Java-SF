package domain.type;

import domain.Program;
import util.TypeHelper;

import java.util.HashSet;
import java.util.Set;

public class MethodType {
  private final String name;
  private final BooleanType parameter;
  private final BooleanType returnType;
  private final boolean isNot;

  public MethodType(String name, BooleanType parameter, BooleanType returnType, boolean isNot) {
    this.name = name;
    this.parameter = parameter;
    this.returnType = returnType;
    this.isNot = isNot;
  }

  public String getName() {
    return name;
  }

  public BooleanType getParameter() {
    return parameter;
  }

  public BooleanType getReturnType() {
    return returnType;
  }

  public boolean isNot() {
    return isNot;
  }

  public Set<Type> getTypes(Program program) {
    // TODO make this static or something
    var set = new HashSet<Type>();

    for (var c : program.getClasses().values()) {
      if (c.getMethods().containsKey(this.getName())) {
        var method = c.getMethods().get(this.getName());

        if (method.getReturnType().equals(this.getReturnType())) {
          if (method.getParameter().getType().a != null && method.getParameter().getType().a.equals(this.getParameter())){
            set.add(c.getType());
          }
          // TODO FIX THIS
//          if (method.getParameter().getType().a == null && method.getParameter().getType().b.equals(this)) {
//            set.add(c.getType());
//          }
        }
      }
    }

    if (isNot) {
      final var universe = new HashSet<>(TypeHelper.UNIVERSE_SET);
      universe.removeAll(set);
      return universe;
    } else {
      return set;
    }
  }
}
