package jsf;

import domain.Program;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parsing.ProgramVisitor;

public final class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());

  private App() {
    // wont be called
  }

  /**
   * Main program that takes a jsf program file and compiles it to JVM bytecode.
   *
   * @param args filename of program to compile
   */
  public static void main(final String[] args) {
    try {
      // TODO change this to get input from args
      final CharStream inputStream = CharStreams.fromPath(
          Paths.get(System.getProperty("user.dir"), "src/main/resources/example.jsf"));

      final jsfLexer lexer = new jsfLexer(inputStream);
      final CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
      final jsfParser parser = new jsfParser(commonTokenStream);
      final ParseTree tree = parser.program();

      final ProgramVisitor visitor = new ProgramVisitor();
      final Program program = visitor.visit(tree);

      visitor.visit(program);

    } catch (Exception e) {
      if (LOGGER.isLoggable(Level.SEVERE)) {
        LOGGER.severe("Exception occurred: " + e);
      }
    }
  }
}