package parsing;

import domain.ClassDeclaration;
import domain.Program;
import java.util.ArrayList;
import java.util.List;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class ProgramVisitor extends jsfBaseVisitor<Program> {

  private final List<ClassDeclaration> classes = new ArrayList<>();

  @Override
  public Program visitProgram(final jsfParser.ProgramContext ctx) {
    final ClassVisitor classVisitor = new ClassVisitor();

    ctx.classdecl().forEach(c -> classes.add(c.accept(classVisitor)));

    System.out.println(classes);
    return new Program(classes);
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }
}
