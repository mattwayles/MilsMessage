package Mils;

import java.util.Scanner;

public class MilsInteractiveBuilder extends Mils {
  private MilsAgent agent;
  private String definition;
  private Scanner inputScanner;

  /**
   * Name: MilsInteractiveBuilder
   * Description: Provides user interaction with the MILSAgent MILS Message operator
   * Author: Matthew Blough-Wayles
   * Last Edited: 08/25/2017
   */
  MilsInteractiveBuilder() {
    setAgent(new MilsAgent());
    setInputScanner(new Scanner(System.in));
  }

  /**
   * Begin the MILS message Build process
   * @param args  Initial parameters used to run the application
   */
  void build(String[] args) {
    MilsMsg milsMsgObj;
    String formatFile;
    String userMessage;
    String milsMsgStr;

    formatFile = args[1];
    milsMsgStr = "";
    userMessage = args[2];

    setDefinition(getAgent().scan(formatFile, userMessage));
    if (getDefinition() != null) {
      milsMsgObj = getAgent().populateMilsMsg(userMessage, getDefinition());
      milsMsgStr = getAgent().buildMilsMsgString(this, milsMsgObj);
      System.out.println();
      System.out.println("Your constructed MILS message is:");
      System.out.println();
      System.out.println(milsMsgStr);
    }
    displayBuildOptions(formatFile, milsMsgStr);
  }

  /**
   * After presenting the user with their built MILS message, give them options for proceeding without
   * re-initializing the application
   * @param file  The format file used to extract definitions
   * @param message The MILS message used in the last parse operation
   */
  private void displayBuildOptions(String file, String message) {
    String userInput;

    System.out.println();
    System.out.println("[1] Build New MILS Message");
    System.out.println("[2] Parse New MILS Message");
    System.out.println("[3] Parse this MILS Message");
    System.out.println("[4] Exit");
    System.out.println();
    System.out.print("Please enter your selection:  ");
    userInput = getInputScanner().nextLine();
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.setInputScanner(getInputScanner());
    switch (userInput) {
      case "1":
        System.out.print("Enter TYPE of New MILS Message [First 1-3 characters]:  ");
        userInput = getInputScanner().nextLine();
        build(new String[]{"/build", file, userInput});
        break;
      case "2":
        System.out.print("Enter New Message:  ");
        userInput = getInputScanner().nextLine();
        parser.parse(new String[]{"/parse", file, userInput});
        break;
      case "3":
        parser.parse(new String[]{"/parse", file, message});
        break;
      case "4":
        return;
      default:
        System.err.println("That selection was not recognized.");
        displayBuildOptions(file, message);
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
   * Retrieve the matching definition gathered from the scan method
   * @return  The definition that matches the provided MILS message type
   */
  private String getDefinition() {
    return definition;
  }

  /**
   * Set the message definition when a match has been found
   * @param def The new message definition
   */
  private void setDefinition(String def) {
    definition = def;
  }

  /**
   * Retrieve the MILS message agent to perform an operation
   * @return  The MILS message agent
   */
  private MilsAgent getAgent() {
    return agent;
  }

  /**
   * Set the MILS message agent for this instance
   * @param ag The MILS message agent
   */
  private void setAgent(MilsAgent ag) {
    agent = ag;
  }
}
