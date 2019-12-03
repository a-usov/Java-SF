package domain.type;

public enum BasicType implements Type {

  BOOLEAN("bool", boolean.class, "Z", TypeSpecificOpcodes.INT),
  INT("int", int.class, "I", TypeSpecificOpcodes.INT),
  CHAR("char", char.class, "C", TypeSpecificOpcodes.INT),
  BYTE("byte", byte.class, "B", TypeSpecificOpcodes.INT),
  SHORT("short", short.class, "S", TypeSpecificOpcodes.INT),
  LONG("long", long.class, "J", TypeSpecificOpcodes.LONG),
  FLOAT("float", float.class, "F", TypeSpecificOpcodes.FLOAT),
  DOUBLE("double", double.class, "D", TypeSpecificOpcodes.DOUBLE),
  VOID("void", void.class, "V", TypeSpecificOpcodes.VOID);
//  STRING ("string", String.class,"Ljava/lang/String;", TypeSpecificOpcodes.OBJECT),
//  BOOLEAN_ARR("bool[]",boolean[].class,"[B", TypeSpecificOpcodes.OBJECT),
//  INT_ARR ("int[]", int[].class,"[I", TypeSpecificOpcodes.OBJECT),
//  CHAR_ARR ("char[]", char[].class,"[C", TypeSpecificOpcodes.OBJECT),
//  BYTE_ARR ("byte[]", byte[].class,"[B", TypeSpecificOpcodes.OBJECT),
//  SHORT_ARR ("short[]", short[].class,"[S", TypeSpecificOpcodes.OBJECT),
//  LONG_ARR ("long[]", long[].class,"[J", TypeSpecificOpcodes.OBJECT),
//  FLOAT_ARR ("float[]", float[].class,"[F", TypeSpecificOpcodes.OBJECT),
//  DOUBLE_ARR ("double[]", double[].class,"[D", TypeSpecificOpcodes.OBJECT),
//  STRING_ARR ("string[]", String[].class,"[Ljava/lang/String;", TypeSpecificOpcodes.OBJECT),
//  NONE("", null,"", TypeSpecificOpcodes.OBJECT),

  private final String name;
  private final Class<?> typeClass;
  private final String descriptor;
  private final TypeSpecificOpcodes opcodes;

  BasicType(final String name, final Class<?> typeClass,
            final String descriptor, final TypeSpecificOpcodes opcodes) {
    this.name = name;
    this.typeClass = typeClass;
    this.descriptor = descriptor;
    this.opcodes = opcodes;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Class<?> getTypeClass() {
    return typeClass;
  }

  @Override
  public String getDescriptor() {
    return descriptor;
  }

  @Override
  public String getInternalName() {
    return getDescriptor();
  }

  @Override
  public int getLoadVariableOpcode() {
    return opcodes.getLoad();
  }

  @Override
  public int getStoreVariableOpcode() {
    return opcodes.getStore();
  }

  @Override
  public int getReturnOpcode() {
    return opcodes.getReturn();
  }

  @Override
  public int getAddOpcode() {
    return opcodes.getAdd();
  }

  @Override
  public int getSubtractOpcode() {
    return opcodes.getSubtract();
  }

  @Override
  public int getMultiplyOpcode() {
    return opcodes.getMultiply();
  }

  @Override
  public int getDivideOpcode() {
    return opcodes.getDivide();
  }
}
