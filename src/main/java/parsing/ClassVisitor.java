package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Constructor;
import domain.Field;
import domain.Method;
import domain.Program;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import jsf.jsfBaseVisitor;
import jsf.jsfParser.ClassDeclContext;
import org.antlr.v4.runtime.misc.Pair;

public class ClassVisitor extends jsfBaseVisitor<Class> {

  @Override
  public Class visitClassDecl(final ClassDeclContext ctx) {
    final var fields = new HashMap<String, Field>();
    final var methods = new HashMap<String, Method>();

    final String name = ctx.classlbl.getText();
    final String superName = ctx.extendlbl == null ? null : ctx.extendlbl.getText();

    AtomicBoolean isResolved = new AtomicBoolean(true);

    final var fieldVisitor = new FieldVisitor();
    ctx.fieldDecl().forEach(fieldCtx -> {
      final Field f = fieldCtx.accept(fieldVisitor);
      if (fields.containsKey(f.getName())) {
        reportError("repeated field names: " + f.getName(), f.getToken());
      } else {
        if (f.getType() == null) {
          isResolved.set(false);
        }
        fields.put(f.getName(), f);
      }
    });

    final var constructorVisitor = new ConstructorVisitor(name);
    final Pair<Constructor, Boolean> pair = ctx.constructorDecl().accept(constructorVisitor);
    final var constructor = pair.a;
    isResolved.set(pair.b);

    final var methodVisitor = new MethodVisitor();
    ctx.methodDecl().forEach(methodCtx -> {
      final Method m = methodCtx.accept(methodVisitor);
      if (methods.containsKey(m.getName())) {
        reportError("repeated method name: " + m.getName(), m.getToken());
      } else {
        if (m.getReturnType() == null || m.getParameter().getType() == null) {
          isResolved.set(false);
        }
        methods.put(m.getName(), m);
      }
    });

    return new Class(name, fields, constructor, methods, superName, ctx.classlbl, isResolved.get());
  }

  /**
   * Visit method for second round contextual checking.
   *
   * @param visitClass current class we are visiting
   * @param program    the whole program structure
   */
  public void visit(final Class visitClass, final Program program) {
    if (visitClass.getSuperName() != null && program.getClasses().get(visitClass.getSuperName()) == null) {
      reportError("The super class " + visitClass.getSuperName() + " of class " + visitClass.getName()
              + " does not exist", visitClass.getToken());
    }

    final var constructorVisitor = new ConstructorVisitor(visitClass.getName());
    constructorVisitor.visit(visitClass.getConstructor(), program);

    final var methodVisitor = new MethodVisitor();
    visitClass.getMethods().values().forEach(m -> methodVisitor.visit(m, program));
  }
}
