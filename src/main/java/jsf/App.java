package jsf;

import domain.Program;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import domain.type.BooleanConnective;
import domain.type.BooleanType;
import domain.type.ClassType;
import domain.type.Type;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parsing.ProgramVisitor;
import util.Typer;

public final class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());

  private App() {
    // wont be called
  }

  /**
   * Main program that takes a jsf program file and compiles it to JVM bytecode.
   * @param args filename of program to compile
   */
  public static void main(final String[] args) {
    try {
      final ANTLRInputStream inputStream = new ANTLRInputStream(Files.newInputStream(
              Paths.get(System.getProperty("user.dir"), "src/main/resources/example.jsf")));

      final jsfLexer lexer = new jsfLexer(inputStream);
      final CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
      final jsfParser parser = new jsfParser(commonTokenStream);
      final ParseTree tree = parser.program();

      final ProgramVisitor visitor = new ProgramVisitor();
      final Program program = visitor.visit(tree);

      visitor.visit(program);

      Type variable = null;
      Type variable2 = null;
      for (var type : Typer.subClasses.keySet()) {
        if (Objects.equals(type.getName(), "asd")) {
          variable = type;
        }
        if (Objects.equals(type.getName(), "as")) {
          variable2 = type;
        }
      }
      var variable3 = new BooleanType(variable, true);
      //var variable3 = new BooleanType(variable, false, BooleanConnective.UNION, new BooleanType(variable2, false));
      System.out.println("IM here");
      System.out.println(variable3.getSet());

    } catch (IOException e) {
      if (LOGGER.isLoggable(Level.SEVERE)) {
        LOGGER.severe("Exception occurred: " + e);
      }
    }

  }
}