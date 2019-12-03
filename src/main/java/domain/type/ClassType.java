package domain.type;

import domain.Field;
import domain.Method;
import org.objectweb.asm.Opcodes;
import java.util.HashMap;
import java.util.Map;

public class ClassType implements Type {
  private final String name;
  private final String internalName;
  private final Map<String, Type> fields;
  private final Map<String, Type> methods;

  public ClassType(final String name) {
    this.name = name;
    this.internalName = name.replace(".", "/");
    this.fields = new HashMap<>();
    this.methods = new HashMap<>();
  }

  public ClassType(final String name, Map<String, Field> fields, Map<String, Method> methods) {
    this.name = name;
    this.internalName = name.replace(".", "/");

    this.fields = new HashMap<>();
    for (var f : fields.values()) {
      this.fields.put(f.getName(), f.getType());
    }

    this.methods = new HashMap<>();
    for (var m : methods.values()) {
      this.fields.put(m.getName(), m.getReturnType());
    }
  }

  public Map<String, Type> getFields() {
    return fields;
  }

  public Map<String, Type> getMethods() {
    return methods;
  }

  public static ClassType createInteger() {
    return new ClassType("java.lang.Integer");
  }

  public static ClassType createDouble() {
    return new ClassType("java.lang.Double");
  }

  public static ClassType createBoolean() {
    return new ClassType("java.lang.Boolean");
  }

  public static ClassType createFloat() {
    return new ClassType("java.lang.Float");
  }

  public static Type createString() {
    return new ClassType("java.lang.String");
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public java.lang.Class<?> getTypeClass() {
    try {
      return java.lang.Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getDescriptor() {
    return "L" + getInternalName() + ";";
  }

  @Override
  public String getInternalName() {
    return internalName;
  }

  @Override
  public int getLoadVariableOpcode() {
    return Opcodes.ALOAD;
  }

  @Override
  public int getStoreVariableOpcode() {
    return Opcodes.ASTORE;
  }

  @Override
  public int getReturnOpcode() {
    return Opcodes.ARETURN;
  }

  @Override
  public int getAddOpcode() {
    throw new RuntimeException("Addition operation not supported for custom objects");
  }

  @Override
  public int getSubtractOpcode() {
    throw new RuntimeException("Subtraction operation not supported for custom objects");
  }

  @Override
  public int getMultiplyOpcode() {
    throw new RuntimeException("Multiplication operation not supported for custom objects");
  }

  @Override
  public int getDivideOpcode() {
    throw new RuntimeException("Division operation not supported for custom objects");
  }

  @Override
  public String toString() {
    return "ClassType: " + name;
  }
}