package domain.type;

// copied from https://github.com/JakubDziworski/Enkel-JVM-language
public interface Type {
  String getName();

  Class<?> getTypeClass();

  String getDescriptor();

  String getInternalName();

  int getLoadVariableOpcode();

  int getStoreVariableOpcode();

  int getReturnOpcode();

  int getAddOpcode();

  int getSubtractOpcode();

  int getMultiplyOpcode();

  int getDivideOpcode();
}

