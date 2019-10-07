package parsing;

import domain.Class;
import domain.Program;
import java.util.ArrayList;
import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  private final List<Class> classes = new ArrayList<>();

  @Override
  public Program visitProgram(final jsfParser.ProgramContext ctx) {
    final ClassVisitor classVisitor = new ClassVisitor();

    ctx.classDecl().forEach(c -> classes.add(c.accept(classVisitor)));

    final Program program = new Program(classes);
    System.out.println(program);
    return program;
  }

  public List<Class> getClasses() {
    return classes;
  }
}
