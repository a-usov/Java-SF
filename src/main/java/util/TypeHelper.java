package util;

import domain.Class;
import domain.Field;
import domain.Method;
import domain.Program;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;
import java.util.*;

public final class TypeHelper {

  public static final Map<Type, Set<Type>> SUB_CLASSES = new HashMap<>();
  public static final Set<Type> UNIVERSE_SET = new HashSet<>();

  static {
    SUB_CLASSES.put(BasicType.BOOLEAN, new HashSet<>(Collections.singletonList(BasicType.BOOLEAN)));

    SUB_CLASSES.put(BasicType.DOUBLE, new HashSet<>(Arrays.asList(BasicType.DOUBLE, BasicType.INT, BasicType.BYTE,
        BasicType.FLOAT, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.FLOAT, new HashSet<>(Arrays.asList(BasicType.FLOAT, BasicType.BYTE, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.LONG, new HashSet<>(Arrays.asList(BasicType.LONG, BasicType.INT, BasicType.BYTE,
        BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.INT, new HashSet<>(Arrays.asList(BasicType.INT, BasicType.BYTE, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.SHORT, new HashSet<>(Arrays.asList(BasicType.SHORT, BasicType.BYTE)));

    SUB_CLASSES.put(BasicType.BYTE, new HashSet<>(Collections.singletonList(BasicType.BYTE)));

    SUB_CLASSES.put(BasicType.VOID, new HashSet<>(Collections.singletonList(BasicType.VOID)));

    UNIVERSE_SET.addAll(SUB_CLASSES.keySet());
  }

  private TypeHelper() {
  }

  /**
   * Given a new class, put it into the static subtyping relation.
   *
   * @param newClass class to add
   * @return boolean if adding was successful
   */
  public static boolean addClass(final Class newClass, final Program program) {
    final var subclasses = new HashSet<Type>();
    subclasses.add(newClass.getType());

    for (final var c : SUB_CLASSES.entrySet()) {
      if (c.getKey() instanceof ClassType) {

        final ClassType existing = (ClassType) c.getKey();
        final Class existingClass = program.getClasses().get(existing.getName());

        var isValid = true;
        for (final var field : newClass.getFields().values()) {
          for (final var type : field.getType().getTypes()) {
            if (!SUB_CLASSES.containsKey(type)) {
              return false;
            }
          }

          isValid = canAddField(existingClass, field);
        }

        if (isValid) {
          for (final var method : newClass.getMethods().values()) {
            isValid = canAddMethod(existingClass, method);
          }
        }

        if (isValid) {
          subclasses.add(existing);
        }


        isValid = true;
        for (final var field : existingClass.getFields().values()) {
          isValid = canAddField(newClass, field);
        }

        if (isValid) {
          for (final var method : existingClass.getMethods().values()) {
            isValid = canAddMethod(newClass, method);
          }
        }

        if (isValid) {
          SUB_CLASSES.get(existing).add(newClass.getType());
        }
      }
    }

    SUB_CLASSES.put(newClass.getType(), subclasses);
    UNIVERSE_SET.add(newClass.getType());
    return true;
  }

  private static boolean canAddField(final Class c, final Field field) {
    if (c.getFields().containsKey(field.getName())) {
      final var newSet = new HashSet<>(c.getFields().get(field.getName()).getType().getSet());

      for (final var type : field.getType().getSet()) {
        if (!newSet.contains(type)) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  private static boolean canAddMethod(final Class c, final Method method) {
    if (c.getMethods().containsKey(method.getName())) {
      var existingMethod = c.getMethods().get(method.getName());

      if (!existingMethod.getReturnType().equals(method.getReturnType())) {
        return false;
      }

      return existingMethod.getParameter().equals(method.getParameter());
    } else {
      return false;
    }
  }
}
