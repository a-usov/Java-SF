package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jsf.jsfBaseVisitor;
import jsf.jsfParser.ProgramContext;
import util.TyperHelper;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  @Override
  public Program visitProgram(final ProgramContext ctx) {
    final var classes = new HashMap<String, Class>();

    final var classVisitor = new ClassVisitor();
    ctx.classDecl().forEach(classCtx -> {
      final Class c = classCtx.accept(classVisitor);
      if (classes.containsKey(c.getName())) {
        reportError("repeated class name: " + c.getName(), c.getCtx());
      } else {
        classes.put(c.getName(), c);
      }
    });

    final var program = new Program(classes);
    System.out.println(program);
    return program;
  }

  /**
   * Second round visit for a whole program.
   *
   * @param program the program we are visiting
   */
  public void visit(final Program program) {
    final var classVisitor = new ClassVisitor();

    generateRelation(program.getClasses().values());

    System.out.println("Relation: " + TyperHelper.SUB_CLASSES);
    System.out.println("Universe: " + TyperHelper.UNIVERSE_SET);

    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));
  }

  private void generateRelation(final Collection<Class> classes) {
    final var untyped = new ArrayList<Class>();

    for (final var c : classes) {
      if (!TyperHelper.addClass(c)) {
        untyped.add(c);
      }
    }

    if (classes.equals(untyped)) {
      // TODO fix me
      throw new RuntimeException("We have a circular dependency");
    } else if (!untyped.isEmpty()) {
      generateRelation(untyped);
    }
  }
}
