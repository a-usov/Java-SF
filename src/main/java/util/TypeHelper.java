package util;

import domain.Class;
import domain.Field;
import domain.Program;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TypeHelper {

  public static final Map<Type, Set<Type>> SUB_CLASSES = new HashMap<>();
  public static final Set<Type> UNIVERSE_SET = new HashSet<>();

  private TypeHelper() {
  }

  static {
    SUB_CLASSES.put(BasicType.BOOLEAN, new HashSet<>(Collections.singletonList(BasicType.BOOLEAN)));

    SUB_CLASSES.put(BasicType.DOUBLE, new HashSet<>(Arrays.asList(BasicType.DOUBLE, BasicType.INT, BasicType.BYTE,
            BasicType.FLOAT, BasicType.LONG, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.FLOAT, new HashSet<>(Arrays.asList(BasicType.FLOAT, BasicType.INT, BasicType.BYTE,
            BasicType.LONG, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.LONG, new HashSet<>(Arrays.asList(BasicType.LONG, BasicType.INT, BasicType.BYTE,
            BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.INT, new HashSet<>(Arrays.asList(BasicType.INT, BasicType.BYTE, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.SHORT, new HashSet<>(Arrays.asList(BasicType.SHORT, BasicType.BYTE)));

    SUB_CLASSES.put(BasicType.BYTE, new HashSet<>(Collections.singletonList(BasicType.BYTE)));

    SUB_CLASSES.put(BasicType.VOID, new HashSet<>(Collections.singletonList(BasicType.VOID)));

    UNIVERSE_SET.addAll(SUB_CLASSES.keySet());
  }

  /**
   * Given a new class, put it into the static subtyping relation.
   *
   * @param newClass class to add
   * @return boolean if adding was successful
   */
  public static boolean addClass(final Class newClass, final Program program) {
    final Set<Type> subclasses = new HashSet<>();
    subclasses.add(newClass.getType());

    for (final var c : SUB_CLASSES.entrySet()) {
      if (c.getKey() instanceof ClassType) {

        final ClassType existing = (ClassType) c.getKey();
        final Class existingClass = program.getClasses().get(existing.getName());

        boolean isValid = true;
        for (final var field : newClass.getFields().values()) {
          for (final var type : field.getType().getTypes()) {
            if (!SUB_CLASSES.containsKey(type)) {
              return false;
            }
          }

          isValid = canAdd(existingClass, field);
        }
        if (isValid) {
          subclasses.add(existing);
        }

        isValid = true;
        for (final var field : existingClass.getFields().values()) {
          isValid = canAdd(newClass, field);
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

  private static boolean canAdd(final Class c, final Field field) {
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
}
