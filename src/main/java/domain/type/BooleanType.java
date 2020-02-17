package domain.type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import util.TypeHelper;

public class BooleanType {
  private final boolean leafNode;

  private final BooleanConnective connective;
  private final BooleanType left;
  private final BooleanType right;

  private final Type type;
  private final Boolean not;

  /**
   * Creates a node in the boolean type tree that is a connective.
   *
   * @param connective The boolean connective of the expression, AND/OR
   * @param left       The left sub-expression it applies to
   * @param right      The right sub-expression it applies to
   */
  public BooleanType(final BooleanConnective connective, final BooleanType left, final BooleanType right) {
    this.leafNode = false;
    this.connective = connective;
    this.left = left;
    this.right = right;

    this.type = null;
    this.not = null;
  }

  /**
   * Creates a node in the boolean type tree that is a concrete type with no moe sub-expressions.
   *
   * @param type The concrete type in an expression
   * @param not  Whether this type has a NOT operator applied to it
   */
  public BooleanType(final Type type, final boolean not) {
    this.leafNode = true;
    this.type = type;
    this.not = not;

    this.connective = null;
    this.left = null;
    this.right = null;
  }

  /**
   * Returns the set of all subclasses according to the boolean type expression.
   *
   * @return Set of applicable subtypes
   */
  public Set<Type> getSet() {
    Set<Type> result = null;

    if (this.isLeafNode()) {
      if (!TypeHelper.SUB_CLASSES.containsKey(this.getType())) {
        throw new RuntimeException("Trying to use class that does not exist: " + this.getType().getName());
      }
      result = new HashSet<>(TypeHelper.SUB_CLASSES.get(this.getType()));

      if (this.isNot()) {
        final var universe = new HashSet<>(TypeHelper.UNIVERSE_SET);
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
          break;
        default:
      }

      return result;
    }
  }

  /**
   * Gets all the unique concrete types in the boolean expressions, this ignores connectives.
   *
   * @return set of all types included in the expression
   */
  public Set<Type> getTypes() {
    final var set = new HashSet<Type>();

    typeGetHelper(set);

    return set;
  }

  private void typeGetHelper(final Set<Type> set) {
    if (this.isLeafNode()) {
      if (!this.isNot()) {
        set.add(this.getType());
      }
    } else {
      this.getLeft().typeGetHelper(set);
      this.getRight().typeGetHelper(set);
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

  @Override
  public String toString() {
    if (this.isLeafNode()) {
      return "Type= " + type + ", not=" + not;
    } else {
      return connective + ": " + left + ", " + right;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BooleanType that = (BooleanType) o;
    return leafNode == that.leafNode &&
        connective == that.connective &&
        Objects.equals(left, that.left) &&
        Objects.equals(right, that.right) &&
        Objects.equals(type, that.type) &&
        Objects.equals(not, that.not);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leafNode, connective, left, right, type, not);
  }

  public enum BooleanConnective {
    INTERS,
    UNION,
  }
}
