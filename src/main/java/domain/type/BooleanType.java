package domain.type;

import util.Typer;

import java.util.HashSet;
import java.util.Set;

public class BooleanType {

  private Type type;
  private BooleanConnective connective;
  private Boolean isNot;
  private BooleanType type2;

  public BooleanType(Type type, Boolean isNot) {
    this.type = type;
    this.isNot = isNot;
    this.connective = null;
    this.type2 = null;
  }

  public BooleanType(Type type, Boolean isNot, BooleanConnective connective, BooleanType type2) {
    this.type = type;
    this.isNot = isNot;
    this.connective = connective;
    this.type2 = type2;
  }

  public Set<Type> getSet() {
    if (this.getConnective() == null) {
      if (this.getNot()) {
        var result = new HashSet<>(Typer.universeSet);
        var mine = Typer.subClasses.get(this.type);
        for (var type : result) {
          for (var type2 : mine)
            if (type.getName().equals(type2.getName())) {
              result.remove(type);
            }
        }
        result.removeAll(mine);
        return result;
      } else {
        return Typer.subClasses.get(this.type);
      }
    } else {

      Set<Type> result = null;
      switch (this.getConnective()) {
        case INTERS:
          var intersection = new HashSet<>(Typer.subClasses.get(this.type));
          intersection.retainAll(getType2().getSet());
          result = intersection;
          break;
        case UNION:
          var union = new HashSet<>(Typer.subClasses.get(this.type));
          union.addAll(getType2().getSet());
          result = union;
          break;
        case COMP:
          var complement = new HashSet<>(Typer.subClasses.get(this.type));
          complement.removeAll(getType2().getSet());
          result = complement;
          break;
      };

      if (this.getNot()) {
        var result2 = new HashSet<>(Typer.universeSet);
        result2.removeAll(result);
        return result2;
      } else {
        return result;
      }
    }
  }

  public BooleanType getType2() {
    return type2;
  }

  public BooleanConnective getConnective() {
    return connective;
  }

  public Type getType() {
    return type;
  }

  public Boolean getNot() {
    return isNot;
  }
}