package util;

import domain.Class;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;

import java.util.*;

public class Typer {

  public static Map<Type, Set<Type>> subClasses = new HashMap<>();
  public static Set<Type> universeSet = new HashSet<>();

  static {
    subClasses.put(BasicType.BOOLEAN, new HashSet<>(Collections.singletonList(BasicType.BOOLEAN)));
    subClasses.put(BasicType.DOUBLE, new HashSet<>(Arrays.asList(BasicType.DOUBLE, BasicType.INT, BasicType.BYTE, BasicType.FLOAT,
        BasicType.LONG, BasicType.SHORT)));

    subClasses.put(BasicType.FLOAT, new HashSet<>(Arrays.asList(BasicType.FLOAT, BasicType.INT, BasicType.BYTE,
        BasicType.LONG, BasicType.SHORT)));

    subClasses.put(BasicType.LONG, new HashSet<>(Arrays.asList(BasicType.LONG, BasicType.INT, BasicType.BYTE, BasicType.SHORT)));

    subClasses.put(BasicType.INT, new HashSet<>(Arrays.asList(BasicType.INT, BasicType.BYTE, BasicType.SHORT)));

    subClasses.put(BasicType.SHORT, new HashSet<>(Arrays.asList(BasicType.SHORT, BasicType.BYTE)));

    subClasses.put(BasicType.BYTE, new HashSet<>(Collections.singletonList(BasicType.BYTE)));

    universeSet.addAll(subClasses.keySet());
  }

  public static boolean addClass(Class newClass) {
    Set<Type> subclasses = new HashSet<>();
    subclasses.add(newClass.getType());

    for (var c : subClasses.entrySet()) {
      if (c.getKey() instanceof ClassType) {

        var existing = (ClassType) c.getKey();
        boolean isValid = true;
        boolean isValidSub = true;

        for (var entry : newClass.getFields().entrySet()) {
          if (! subClasses.containsKey(entry.getValue().getType())) {
            return false;
          }

          if (existing.getFields().containsKey(entry.getKey())) {
            if (!subClasses.get(existing.getFields().get(entry.getKey())).contains(entry.getValue().getType())) {
              isValidSub = false;
            }

            if (!subClasses.get(entry.getValue().getType()).contains(existing.getFields().get(entry.getKey()))) {
              isValid = false;
            }
          } else {
            isValid = false;
          }
        }

        if (isValidSub) {
          subClasses.get(existing).add(newClass.getType());
        }

        if (isValid) {
          subclasses.add(existing);
        }
      }
    }

    subClasses.put(newClass.getType(), subclasses);
    universeSet.add(newClass.getType());
    return true;
  }

}
