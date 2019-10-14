package util;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;
import java.util.Arrays;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public final class TypeResolverUtils {

  private TypeResolverUtils(){

  }

  /**
   * From a type name as a string, tries to infer a basic type, or else creates a custom class type.
   * @param typeName type name as a string
   * @return infered basic type or custom class type
   */
  public static Type getFromTypeName(final String typeName) {
    final Optional<? extends Type> basicType = getBasicType(typeName);
    return basicType.isPresent() ? basicType.get() : new ClassType(typeName);
  }

  /**
   * From a type name as string, tried to match it to a basic type.
   * @param typeName type as string, assumed to be lowercase if it is due to lexing
   * @return returns an Optional basic type if can be matched, empty optional otherwise
   */
  private static Optional<BasicType> getBasicType(final String typeName) {
    return Arrays.stream(BasicType.values())
        .filter(type -> type.getName().equals(typeName))
        .findFirst();
  }

  // TODO - no support for anything other than ints right now

  /**
   * From a basic value as a string, infer its type.
   * @param value value as string to infer
   * @return Type of value that we interpret
   */
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
   * Create a error message string given a msg and a context in the program.
   * @param message message to display with error
   * @param ctx context of msg that has line numbers of error
   * @return formatted error msg
   */
  public static String reportError(final String message, final ParserRuleContext ctx) {
    // Print an error message relating to the given part of the AST.
    final Token start = ctx.getStart();
    final Token finish = ctx.getStop();
    final int startLine = start.getLine();
    final int startCol = start.getCharPositionInLine();
    final int finishLine = finish.getLine();
    final int finishCol = finish.getCharPositionInLine();
    return startLine + ":" + startCol + "-" + finishLine + ":" + finishCol + " " + message;
  }

}
