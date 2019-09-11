package jsf;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class AppTest {
  
  @Test public void testAppHasAGreeting() {
    final App classUnderTest = new App("Hello world");
    assertNotNull("app should have a greeting", classUnderTest.getGreeting());
  }
}
