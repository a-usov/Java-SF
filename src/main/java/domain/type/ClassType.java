package domain.type;

import domain.Class;
import domain.Field;
import domain.Method;
import org.objectweb.asm.Opcodes;

import java.util.Map;

public class ClassType implements Type {
  private final String name;
  private final String internalName;
  private final Map<String, Field> fields;
  private final Map<String, Method> methods;



  public ClassType(final String name) {
    this.name = name;
    this.internalName = name.replace(".", "/");
    this.fields = null;
    this.methods = null;
  }

  public ClassType(final String name, Class newClass) {
    this.name = name;
    this.internalName = name.replace(".", "/");
    this.fields = newClass.getFields();
    this.methods = newClass.getMethods();
  }

  public Map<String, Field> getFields() {
    return fields;
  }

  public Map<String, Method> getMethods() {
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
    return  "L" + getInternalName() + ";";
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
