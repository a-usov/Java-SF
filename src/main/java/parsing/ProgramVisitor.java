package parsing;

import domain.Class;
import domain.Program;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;
import util.Typer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static util.TypeResolverUtils.reportError;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  @Override
  public Program visitProgram(final jsfParser.ProgramContext ctx) {
    final Map<String, Class> classes = new HashMap<>();

    final ClassVisitor classVisitor = new ClassVisitor();

    ctx.classDecl().forEach(c -> {
      final Class visitingClass = c.accept(classVisitor);
      if (classes.containsKey(visitingClass.getName())) {
        throw new RuntimeException(
            reportError("repeated class name: " + visitingClass.getName(), c));
      } else {
        classes.put(visitingClass.getName(), visitingClass);
      }
    });

    final Program program = new Program(classes);
    System.out.println(program);
    return program;
  }

  /**
   * Second round visit for a whole program.
   * @param program the program we are visiting
   */
  public void visit(final Program program) {
    final ClassVisitor classVisitor = new ClassVisitor();

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
      throw new RuntimeException("We have a circular dependency");
    } else if (! untyped.isEmpty()) {
        generateRelation(untyped);
    }
  }
}
