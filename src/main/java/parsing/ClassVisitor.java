package parsing;

import domain.Class;
import domain.Constructor;
import domain.Field;
import domain.Method;
import java.util.HashMap;
import java.util.Map;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class ClassVisitor extends jsfBaseVisitor<Class> {

  @Override
  public Class visitClassDecl(final jsfParser.ClassDeclContext ctx) {
    final Map<String, Field> fields = new HashMap<>();
    final Map<String, Method> methods = new HashMap<>();

    final String name = ctx.classlbl.getText();

    final FieldVisitor fieldVisitor = new FieldVisitor(name);
    ctx.fieldDecl().forEach(f -> {
      final Field field = f.accept(fieldVisitor);
      if (fields.containsKey(field.getName())) {
        System.out.println("Oops");
      } else {
        fields.put(field.getName(), field);
      }
    });

    final ConstructorVisitor constructorVisitor = new ConstructorVisitor(name);
    final Constructor constructor = ctx.constructorDecl().accept(constructorVisitor);

    final MethodVisitor methodVisitor = new MethodVisitor();
    ctx.methodDecl().forEach(m -> {
      final Method method = m.accept(methodVisitor);
      if (methods.containsKey(method.getName())) {
        System.out.println("Oops");
      } else {
        methods.put(method.getName(), method);
      }
    });

    return new Class(name, fields, constructor, methods);
  }
}
