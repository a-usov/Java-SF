package util;

import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;
import java.util.Arrays;
import java.util.Optional;

public final class TypeResolver {

  private TypeResolver(){

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
}
