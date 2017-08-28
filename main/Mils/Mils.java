package Mils;

public class Mils {
  /**
   * Main method to run the operation held in the first supplied parameter
   * @param args  Initial parameters used to run the application
   */
  public static void main(String args[]) {
    Boolean exited;
    String operation;

    exited = false;
    operation = args[0];

    if (args.length < 3) {
      System.err.println("You have provided the wrong number of parameters.");
      System.err.println("The correct format is: java Mils </parse|/build> <format file> <MILS message|type> [message field]");
    }
    else {
      switch (operation) {
        case "/parse":
          MilsInteractiveParser parser = new MilsInteractiveParser();
          while (!exited) {
            exited = parser.parse(args);
          }
          break;
        case "/build":
          MilsInteractiveBuilder builder = new MilsInteractiveBuilder();
          builder.build(args);
          break;
        default:
          System.err.println("Operation parameter incorrect - use /parse or /build");
          break;
      }
    }
  }
}

