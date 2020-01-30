package jsf;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
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
      final var inputStream = CharStreams.fromPath(
              Paths.get(System.getProperty("user.dir"), "src/main/resources/example.jsf"));

      final var lexer = new jsfLexer(inputStream);
      final var commonTokenStream = new CommonTokenStream(lexer);
      final var parser = new jsfParser(commonTokenStream);
      final var tree = parser.program();

      final var visitor = new ProgramVisitor();
      final var program = visitor.visit(tree);

      System.out.println(program);

      visitor.visit(program);
    } catch (IOException e) {
      if (LOGGER.isLoggable(Level.SEVERE)) {
        LOGGER.severe("Exception occurred: " + e);
      }
    }
  }
}