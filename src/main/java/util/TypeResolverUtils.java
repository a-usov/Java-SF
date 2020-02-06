package util;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import domain.type.BasicType;
import domain.type.BooleanType;
import domain.type.ClassType;
import domain.type.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public final class TypeResolverUtils {

  private TypeResolverUtils() {
  }

  /**
   * From a type name as a string, tries to infer a basic type, or else creates a custom class type.
   *
   * @param typeName type name as a string
   * @return inferred basic type or custom class type
   */
  public static Type getFromTypeName(final String typeName) {
    final var basicType = getBasicType(typeName);
    return basicType.isPresent() ? basicType.get() : new ClassType(typeName);
  }

  /**
   * From a type name as string, tried to match it to a basic type.
   *
   * @param typeName type as string, assumed to be lowercase if it is due to lexing
   * @return returns an Optional basic type if can be matched, empty optional otherwise
   */
  private static Optional<BasicType> getBasicType(final String typeName) {
    return Arrays.stream(BasicType.values())
        .filter(type -> type.getName().equals(typeName))
        .findFirst();
  }

  /**
   * Given Boolean type t1, check if Boolean type t2 is a subtype by checking if set intersection is empty.
   *
   * @param   t1 Boolean type of super type
   * @param   t2 Boolean type which is checked if subtype of t1
   * @return  true if t2 is not a valid subtype of t1
   */
  public static boolean isNotValidSubtype(final BooleanType t1, final BooleanType t2) {
    final var intersection = t1.getSet();
    intersection.retainAll(t2.getSet());

    return intersection.isEmpty();
  }

  /**
   * Given a Boolean type t, check if the type represented by the set t1 is a subtype by checking if set intersection
   * is empty. The set passed is not modified but copied.
   *
   * @param t   Boolean type of super type
   * @param t1  Set of types representing the type which is being checked
   * @return    true if type represented by t1 is not a subtype of t
   */
  public static boolean isNotValidSubtype(final BooleanType t, final Set<Type> t1) {
    final var intersection = t.getSet();
    final var t2 = new HashSet<>(t1);

    intersection.retainAll(t2);

    return !intersection.isEmpty();
  }

  /**
   * Given a set representing type t, and a set representing type t1, check if t1 is a subtype of t by checking if set
   * intersection is empty. The sets passed are not modified by copied.
   * @param t   Set of types representing super type
   * @param t1  Set of types representing the type being checked
   * @return    true if type represented by t1 is not a subtype of t2
   */
  public static boolean isNotValidSubtype(final Set<Type> t, final Set<Type> t1) {
    final var intersection = new HashSet<>(t);
    final var t2 = new HashSet<>(t1);

    intersection.retainAll(t2);

    return !intersection.isEmpty();
  }

  /**
   * From a basic value as a string, infer its type.
   *
   * @param value value as string to infer
   * @return Type of value that we interpret
   */
  @SuppressWarnings("UnstableApiUsage")
  public static Type getFromValue(final String value) {
    if (Ints.tryParse(value) != null) {
      return BasicType.INT;
    } else if (Floats.tryParse(value) != null) {
      return BasicType.FLOAT;
    } else if (Doubles.tryParse(value) != null) {
      return BasicType.DOUBLE;
    } else {
      return BasicType.VOID;
    }
  }

  /**
   * Throw a Runtime Exception given a msg and a context in the program.
   *
   * @param message message to display with error
   * @param ctx     context of msg that has line numbers of error
   */
  public static void reportError(final String message, final ParserRuleContext ctx) {
    final var start = ctx.getStart();
    final var finish = ctx.getStop();
    final var startLine = start.getLine();
    final var startCol = start.getCharPositionInLine();
    final var finishLine = finish.getLine();
    final var finishCol = finish.getCharPositionInLine();

    throw new RuntimeException(startLine + ":" + startCol + "-" + finishLine + ":" + finishCol + " " + message);
  }

  /**
   * Throw a Runtime Exception given a single token of the AST instead of a context.
   *
   * @param message message to display with error
   * @param ctx     Token where error occurred
   */
  public static void reportError(final String message, final Token ctx) {
    final var startLine = ctx.getLine();
    final var startCol = ctx.getCharPositionInLine();

    throw new RuntimeException(startLine + ":" + startCol + " " + message);
  }

}
