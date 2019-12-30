package domain.type;

import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.misc.Pair;
import util.TyperHelper;

public class BooleanType {

  private final Type type;
  private final Boolean not;
  private final Pair<BooleanConnective, BooleanType> connective;

  /**
   * Create a boolean type that does not have a boolean connective.
   *
   * @param type Type of type
   * @param not Whether to not the set of values of this type
   */
  public BooleanType(final Type type, final Boolean not) {
    this.type = type;
    this.not = not;
    this.connective = null;
  }

  /**
   * Create a boolean type that has a boolean type connected to it.
   *
   * @param type Type of type
   * @param not Whether to not the set of values of this type
   * @param type2 A pair of the boolean connective (and/or) and the other boolean type
   */
  public BooleanType(final Type type, final Boolean not,
                     final Pair<BooleanConnective, BooleanType> type2) {
    this.type = type;
    this.not = not;
    this.connective = type2;
  }

  /**
   * For a given Boolean type, calculate the set of values of this type.
   *
   * @return Set of types that are sub-classes of this boolean type
   */
  public Set<Type> getSet() {
    Set<Type> result = null;

    if (this.getConnective() == null) {
      result = TyperHelper.SUB_CLASSES.get(this.type);
    } else {
      final var connective = this.getConnective().a;
      final var type2 = this.getConnective().b;

      switch (connective) {
        case INTERS:
          final var intersection = new HashSet<>(TyperHelper.SUB_CLASSES.get(this.type));
          intersection.retainAll(type2.getSet());
          result = intersection;
          break;
        case UNION:
          final var union = new HashSet<>(TyperHelper.SUB_CLASSES.get(this.type));
          union.addAll(type2.getSet());
          result = union;
          break;
        default:
          break;
      }
    }

    if (this.isNot()) {
      final var result2 = new HashSet<>(TyperHelper.UNIVERSE_SET);
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