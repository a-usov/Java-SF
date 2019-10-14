package parsing;

import static util.TypeResolverUtils.reportError;

import domain.Class;
import domain.Program;
import java.util.HashMap;
import java.util.Map;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

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

    program.getClasses().values().forEach(c -> classVisitor.visit(c, program));
  }
}
