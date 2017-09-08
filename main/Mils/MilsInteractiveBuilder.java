package Mils;

import java.util.Scanner;

class MilsInteractiveBuilder extends MilsAgent {
  private String userType;
  private Scanner inputScanner;

  /**
   * Name: MilsInteractiveBuilder
   * Description: Provides user interaction with the MILSAgent MILS Message operator
   * Author: Matthew Blough-Wayles
   * Last Edited: 08/25/2017
   */
  MilsInteractiveBuilder(String format, String message) {
    setFormatFile(format);
    setUserType(message);
    setInputScanner(new Scanner(System.in));
  }

  /**
   * Begin the MILS message Build process
   */
  void build() {
    MilsMsg milsMsgObj;
    String definition;
    String milsMsgStr;

    milsMsgStr = "";

    definition = scan(getFormatFile(), getUserType());
    if (definition != null) {
      milsMsgObj = populateMilsMsg(getUserType(), definition);
      milsMsgStr = buildMilsMsgString(milsMsgObj);
      System.out.println();
      System.out.println("Your constructed MILS message is:");
      System.out.println();
      System.out.println(milsMsgStr);
    }
    displayBuildOptions(milsMsgStr);
  }

  /**
   * After presenting the user with their built MILS message, give them options for proceeding without
   * re-initializing the application
   * @param message The MILS message used in the last parse operation
   */
  private void displayBuildOptions(String message) {
    String userInput;

    System.out.println();
    System.out.println("[1] Build New MILS Message");
    System.out.println("[2] Parse New MILS Message");
    System.out.println("[3] Parse this MILS Message");
    System.out.println("[4] Exit");
    System.out.println();
    System.out.print("Please enter your selection:  ");
    userInput = getInputScanner().nextLine();
    MilsInteractiveParser parser = new MilsInteractiveParser(getFormatFile(), message);
    parser.setInputScanner(getInputScanner());
    switch (userInput) {
      case "1":
        System.out.print("Enter TYPE of New MILS Message [First 1-3 characters]:  ");
        setUserType(getInputScanner().nextLine());
        build();
        break;
      case "2":
        System.out.print("Enter New Message:  ");
        parser.setUserMessage(getInputScanner().nextLine());
        parser.parse();
        break;
      case "3":
        parser.parse();
        break;
      case "4":
        return;
      default:
        System.err.println("That selection was not recognized.");
        displayBuildOptions(message);
    }
  }


  /**
   * Re-Prompt the user if bad message build data is provided
   * @param field The message field to prompt for a value
   * @param len The acceptable number of characters required for this field
   * @return  The user input
   */
  String getMessageFieldInput(String field, int len) {
    String value;
    String userInput;

    System.out.print("Enter " + len + "-character value for " + field + ":  ");
    userInput = getInputScanner().nextLine();
    if (!userInput.isEmpty() && userInput.length() == len) {
      value = userInput;
    } else {
      System.err.println("Invalid input. Please try again.");
      value = getMessageFieldInput(field, len);
    }
    return value;
  }

  /**
   * Retrieve the instance input scanner
   * @return  The instance input scanner
   */
  private Scanner getInputScanner() {
    return inputScanner;
  }

  /**
   * Create the instance input scanner
   * @param sc  The instance input scanner
   */
  void setInputScanner(Scanner sc) {
    inputScanner = sc;
  }

  /**
   * Retrieve the MILS message agent to perform an operation
   * @return  The MILS message agent
   */
  private String getUserType() {
    return userType;
  }

  /**
   * Set the MILS message agent for this instance
   * @param um The user-supplied MILS message
   */
  private void setUserType(String um) {
    userType = um;
  }
}
