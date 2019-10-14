package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Constructor;
import domain.Field;
import domain.Method;
import domain.Program;
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
    final String superName = ctx.extendlbl == null ? "" : ctx.extendlbl.getText();

    final FieldVisitor fieldVisitor = new FieldVisitor(name);
    ctx.fieldDecl().forEach(f -> {
      final Field field = f.accept(fieldVisitor);
      if (fields.containsKey(field.getName())) {
        throw new RuntimeException(reportError("repeated field names: " + field.getName(), f));
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
        throw new RuntimeException(reportError("repeated method name: " + method.getName(), m));
      } else {
        methods.put(method.getName(), method);
      }
    });

    return new Class(name, fields, constructor, methods, superName);
  }

  /**
   * Visit method for second round contextual checking.
   * @param visitClass current class we are visiting
   * @param program the whole program structure
   */
  public void visit(final Class visitClass, final Program program) {
    final ConstructorVisitor constructorVisitor = new ConstructorVisitor(visitClass.getName());
    constructorVisitor.visit(visitClass.getConstructor(), program);

    final MethodVisitor methodVisitor = new MethodVisitor();

    visitClass.getMethods().values().forEach(m -> methodVisitor.visit(m, program));
  }
}
