package parsing;

import domain.Class;
import domain.Program;
import jsf.jsfBaseVisitor;
import jsf.jsfParser.ProgramContext;
import util.Typer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import static util.TypeResolverUtils.reportError;

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
   * @param program the program we are visiting
   */
  public void visit(final Program program) {
    final var classVisitor = new ClassVisitor();

    generateRelation(program.getClasses().values());

    System.out.println("Relation: " + Typer.subClasses);
    System.out.println("Universe: " + Typer.universeSet);

    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));
  }

  private void generateRelation(Collection<Class> classes) {
    var untyped = new ArrayList<Class>();

    for (var class_ : classes) {
      if (! Typer.addClass(class_)) {
        untyped.add(class_);
      }
    }

    if (classes.equals(untyped)) {
      // TODO fix me
      throw new RuntimeException("We have a circular dependency");
    } else if (! untyped.isEmpty()) {
        generateRelation(untyped);
    }
  }
}
