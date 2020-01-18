package domain.type;

import java.util.HashSet;
import java.util.Set;

import util.TyperHelper;

public class BooleanType {
  private final boolean leafNode;

  private final BooleanConnective connective;
  private final BooleanType left;
  private final BooleanType right;

  private final Type type;
  private final Boolean not;

  public BooleanType(BooleanConnective connective, BooleanType left, BooleanType right) {
    this.leafNode = false;
    this.connective = connective;
    this.left = left;
    this.right = right;

    this.type = null;
    this.not = null;
  }

  public BooleanType(Type type, boolean not) {
    this.leafNode = true;
    this.type = type;
    this.not = not;

    this.connective = null;
    this.left = null;
    this.right = null;
  }

  public Set<Type> getSet() {
    Set<Type> result = null;

    if (this.isLeafNode()) {
      result = new HashSet<>(TyperHelper.SUB_CLASSES.get(this.getType()));

      if (this.isNot()) {
        final var universe = new HashSet<>(TyperHelper.UNIVERSE_SET);
        universe.removeAll(result);
        return universe;
      } else {
        return result;
      }
    } else {
      switch (this.getConnective()) {
        case UNION:
          final var union1 = this.getLeft().getSet();
          final var union2 = this.getRight().getSet();

          union1.addAll(union2);
          result = union1;
          break;
        case INTERS:
          final var inter1 = this.getLeft().getSet();
          final var inter2 = this.getRight().getSet();

          inter1.retainAll(inter2);
          result = inter1;
      }

      return result;
    }
  }

  public boolean isLeafNode() {
    return leafNode;
  }

  public Boolean isNot() {
    return not;
  }

  public Type getType() {
    return type;
  }

  public BooleanConnective getConnective() {
    return connective;
  }

  public BooleanType getLeft() {
    return left;
  }

  public BooleanType getRight() {
    return right;
  }

  public enum BooleanConnective {
    INTERS,
    UNION,
  }
}
