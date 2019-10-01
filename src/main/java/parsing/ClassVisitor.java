package parsing;

import domain.Class;
import domain.Constructor;
import domain.Field;
import java.util.HashSet;
import java.util.Set;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class ClassVisitor extends jsfBaseVisitor<Class> {

  private final Set<String> classNames = new HashSet<>();
  private final Set<Field> fields = new HashSet<>();

  @Override
  public Class visitClassdecl(final jsfParser.ClassdeclContext ctx) {
    final String name = ctx.classlbl.getText();

    if (classNames.contains(name)) {
      System.out.println("Oops");
    } else {
      classNames.add(name);
    }

    final FieldVisitor fieldVisitor = new FieldVisitor(name);
    ctx.fielddecl().forEach(f -> {
      final Field field = f.accept(fieldVisitor);
      if (fields.contains(field)) {
        System.out.println("Oops");
      } else {
        fields.add(field);
      }
    });

    final ConstructorVisitor constructorVisitor = new ConstructorVisitor(name);
    final Constructor constructor = ctx.constructordecl().accept(constructorVisitor);

    return new Class(name, fields, constructor);
  }

  public Set<Field> getFields() {
    return fields;
  }

  public Set<String> getClassNames() {
    return classNames;
  }
}
