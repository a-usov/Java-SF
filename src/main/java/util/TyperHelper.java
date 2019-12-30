package util;

import domain.Class;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TyperHelper {

  public static final Map<Type, Set<Type>> SUB_CLASSES = new HashMap<>();
  public static final Set<Type> UNIVERSE_SET = new HashSet<>();

  private TyperHelper(){
  }

  static {
    SUB_CLASSES.put(BasicType.BOOLEAN, new HashSet<>(Collections.singletonList(BasicType.BOOLEAN)));
    SUB_CLASSES.put(BasicType.DOUBLE, new HashSet<>(Arrays.asList(BasicType.DOUBLE, BasicType.INT,
        BasicType.BYTE, BasicType.FLOAT, BasicType.LONG, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.FLOAT, new HashSet<>(Arrays.asList(BasicType.FLOAT, BasicType.INT,
        BasicType.BYTE, BasicType.LONG, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.LONG, new HashSet<>(Arrays.asList(BasicType.LONG, BasicType.INT,
        BasicType.BYTE, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.INT, new HashSet<>(Arrays.asList(BasicType.INT, BasicType.BYTE, BasicType.SHORT)));

    SUB_CLASSES.put(BasicType.SHORT, new HashSet<>(Arrays.asList(BasicType.SHORT, BasicType.BYTE)));

    SUB_CLASSES.put(BasicType.BYTE, new HashSet<>(Collections.singletonList(BasicType.BYTE)));

    UNIVERSE_SET.addAll(SUB_CLASSES.keySet());
  }

  /**
   * Given a new class, put it into the static subtyping relation.
   *
   * @param newClass class to add
   * @return boolean if adding was succesful
   */
  public static boolean addClass(final Class newClass) {
    final Set<Type> subclasses = new HashSet<>();
    subclasses.add(newClass.getType());

    for (final var c : SUB_CLASSES.entrySet()) {
      if (c.getKey() instanceof ClassType) {

        final var existing = (ClassType) c.getKey();
        boolean isValid = true;
        boolean isValidSub = true;

        for (final var entry : newClass.getFields().entrySet()) {
          if (!SUB_CLASSES.containsKey(entry.getValue().getType())) {
            return false;
          }

          if (existing.getFields().containsKey(entry.getKey())) {
            if (!SUB_CLASSES.get(existing.getFields().get(entry.getKey()))
                .contains(entry.getValue().getType())) {
              isValidSub = false;
            }

            if (!SUB_CLASSES.get(entry.getValue().getType())
                .contains(existing.getFields().get(entry.getKey()))) {
              isValid = false;
            }
          } else {
            isValid = false;
          }
        }

        if (isValidSub) {
          SUB_CLASSES.get(existing).add(newClass.getType());
        }

        if (isValid) {
          subclasses.add(existing);
        }
      }
    }

    SUB_CLASSES.put(newClass.getType(), subclasses);
    UNIVERSE_SET.add(newClass.getType());
    return true;
  }

}
