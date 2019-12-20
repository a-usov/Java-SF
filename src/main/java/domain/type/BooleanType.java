package domain.type;

import org.antlr.v4.runtime.misc.Pair;
import util.Typer;

import java.util.HashSet;
import java.util.Set;

public class BooleanType {

  private final Type type;
  private final Boolean not;
  private final Pair<BooleanConnective, BooleanType> connective;

  public BooleanType(Type type, Boolean not) {
    this.type = type;
    this.not = not;
    this.connective = null;
  }

  public BooleanType(Type type, Boolean not, Pair<BooleanConnective, BooleanType> type2) {
    this.type = type;
    this.not = not;
    this.connective = type2;
  }

  public Set<Type> getSet() {
    Set<Type> result = null;

    if (this.getConnective() == null) {
      result = Typer.subClasses.get(this.type);
    } else {
      var connective = this.getConnective().a;
      var type2 = this.getConnective().b;

      switch (connective) {
        case INTERS:
          var intersection = new HashSet<>(Typer.subClasses.get(this.type));
          intersection.retainAll(type2.getSet());
          result = intersection;
          break;
        case UNION:
          var union = new HashSet<>(Typer.subClasses.get(this.type));
          union.addAll(type2.getSet());
          result = union;
          break;
      }
    }

    if (this.isNot()) {
      var result2 = new HashSet<>(Typer.universeSet);
      result2.removeAll(result);
      return result2;
    } else {
      return result;
    }
  }

  public Pair<BooleanConnective, BooleanType> getConnective() {
    return connective;
  }

  public Type getType() {
    return type;
  }

  public Boolean isNot() {
    return not;
  }

  private enum BooleanConnective {
    INTERS,
    UNION,
  }
}