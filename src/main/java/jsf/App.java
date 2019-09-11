package jsf;

public class App {

  private String greeting;

  public App(final String greeting) {
    this.greeting = greeting;
  }

  public void setGreeting(final String greeting) {
    this.greeting = greeting;
  }

  public String getGreeting() {
    return greeting;
  }

  public static void main(final String[] args) {
    System.out.println(new App("Hello world").getGreeting());
  }
}
