package Mils;

import java.io.File;
import java.util.ArrayList;

public class Mils {
  /**
   * Main method to run the operation held in the first supplied parameter
   * @param args  Initial parameters used to run the application
   */
  public static void main(String args[]) {
    String operation;
    String format;
    String message;
    String field;
    File output;
    Boolean exited;
    ArrayList<String> buildParams;

    if (args.length < 3) {
      System.err.println("You have provided the wrong number of parameters.");
      System.err.println("The correct format is: java Mils </parse|/parseFile|/build> <format file> " +
        "<MILS message|type|inputFile> [message field|outputFile]");
    } else {
      operation = args[0];
      format = args[1];
      message = args[2];
      exited = false;
      switch (operation) {
        case "/parse":
          while (!exited) {
            if (thirdParamExists(args)) {
              field = args[3];
              MilsInteractiveParser parser = new MilsInteractiveParser(format, message, field);
              exited = parser.parse();
            } else {
              MilsInteractiveParser parser = new MilsInteractiveParser(format, message);
              exited = parser.parse();
            }
          }
          break;
        case "/build":
          MilsInteractiveBuilder builder = new MilsInteractiveBuilder(format, message);
          builder.build();
          break;
        case "/parseFile":
          if (thirdParamExists(args)) {
            output = new File(args[3]);
            MilsFileParser fileParser = new MilsFileParser(format, message, output);
            fileParser.parse();
          }
          else {
            MilsFileParser fileParser = new MilsFileParser(format, message);
            fileParser.parse();
          }
          break;
        case "/buildFile":
          MilsFileBuilder fileBuilder;
          buildParams = new ArrayList<>();
          for (int i = 2; i < args.length; i++) {
            buildParams.add(args[i]);
          }
          fileBuilder = new MilsFileBuilder(format, message);
          fileBuilder.build(buildParams);

            default:
              System.err.println("Operation parameter incorrect - use /parse or /build");
              break;
          }
      }
    }

  /**
   * Special method for instances when the application is run with an extra parse parameter.
   * In this situation, the result is instantly returned with no user interaction
   * @param args The initial parameters supplied to the application
   * @return  Boolean value indicating whether a third parse parameter was used
   */
  static Boolean thirdParamExists(String[] args) {
    if (args.length == 4 && !args[3].isEmpty()) {
        return true;
      }
    return false;
  }
}

