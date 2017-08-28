package Mils;

import java.util.Scanner;

public class MilsInteractiveParser extends Mils {
  private MilsAgent agent;
  private String definition;
  private Scanner inputScanner;

  /**
   * Name: MilsInteractiveParser
   * Description: Provides user interaction with the MILSAgent MILS Message operator
   * Author: Matthew Blough-Wayles
   * Last Edited: 08/25/2017
   */
  MilsInteractiveParser() {
    setAgent(new MilsAgent());
    setInputScanner(new Scanner(System.in));
  }

  /**
   * Begin the MILS message Parse process
   * @param args Initial parameters used to run the application
   */
  Boolean parse(String[] args) {
    MilsMsg milsMsgObj;
    String formatFile;
    String result;
    String userMessage;

    formatFile = args[1];
    userMessage = args[2];
    
    if (userMessage.length() != 80) {
      System.err.println("The provided MILS message is invalid. Please try again.");
      displayParseOptions(formatFile, userMessage);
      return true;
    }
    setDefinition(getAgent().scan(formatFile, userMessage.substring(0, 3)));
    if (getDefinition() != null) {
      milsMsgObj = getAgent().populateMilsMsg(userMessage, getDefinition());
      if (!thirdParamExists(args, milsMsgObj)) {
        if (!milsMsgObj.getData().keySet().isEmpty()) {
          result = getUserSelection(milsMsgObj);
          if (!validateUserSelection(result)) {

            return false;
          }
        }
      }
    }
    displayParseOptions(formatFile, userMessage);
    return true;
  }

  /**
   * Special method for instances when the application is run with an extra parse parameter.
   * In this situation, the result is instantly returned with no user interaction
   * @param args The initial parameters supplied to the application
   * @param milsMessageObject The MILS Message
   * @return  Boolean value indicating whether a third parse parameter was used
   */
  Boolean thirdParamExists(String[] args, MilsMsg milsMessageObject) {
    String result;

    if (args.length == 4 && !args[3].isEmpty()) {
      result = milsMessageObject.get(args[3]);
      if (result != null) {
        System.out.println(result);
        return true;
      }
      else {
        System.err.println("That message field does not exist in this message definition.");
      }
    }
    return false;
  }
  
  /**
   * After presenting the user with their parsed MILS message, give them options for proceeding without
   * re-initializing the application
   * @param file  The format file used to extract definitions
   * @param message The MILS message used in the last parse operation
   */
  private void displayParseOptions(String file, String message) {
    String userInput;

    System.out.println();
    System.out.println("[1] New Format File");
    System.out.println("[2] Parse New MILS Message");
    System.out.println("[3] New Message Field From This MILS Message");
    System.out.println("[4] Build New MILS Message");
    System.out.println("[5] Rebuild This MILS Message");
    System.out.println("[6] Exit");
    System.out.println();
    System.out.print("Please enter your selection:  ");
    userInput = getInputScanner().nextLine();
    switch (userInput) {
      case "1":
        System.out.print("Enter path of new format file: ");
        userInput = getInputScanner().nextLine();
        parse(new String[]{"/parse", userInput, message});
        break;
      case "2":
        System.out.print("Enter New Message:  ");
        userInput = getInputScanner().nextLine();
        parse(new String[]{"/parse", file, userInput});
        break;
      case "3":
        parse(new String[]{"/parse", file, message});
        break;
      case "4":
        System.out.print("Enter TYPE of New MILS Message [First 1-3 characters]:  ");
        userInput = getInputScanner().nextLine();
        MilsInteractiveBuilder builder = new MilsInteractiveBuilder();
        builder.setInputScanner(getInputScanner());
        builder.build(new String[]{"/build", file, userInput});
        break;
      case "5":
        System.out.println(message);
        displayParseOptions(file, message);
      case "6":
        return;
      default:
        System.err.println("That selection was not recognized.");
        displayParseOptions(file, message);
    }
  }

  /**
   * Query the user for their choice after presenting a list of valid message options
   * @param milsMsgObj  The MILS Message object containign the data being extracted
   * @return  The user's selection
   */
  private String getUserSelection(MilsMsg milsMsgObj) {
    String messageField;

    System.out.print("Which message field would you like to retrieve?");
    System.out.println();
    for (String key : milsMsgObj.getData().keySet()) {
      System.out.println("  " + key);
    }
    System.out.println();
    System.out.print("Selection:  ");
    messageField = getInputScanner().nextLine();
    return milsMsgObj.get(messageField);
  }

  /**
   * Ensure the user input is a valid message field string
   * @param result  The user's selection
   */
  private Boolean validateUserSelection(String result) {
    if (result != null) {
      System.out.println(result);
      System.out.println();
      return true;
    }
    else {
      System.err.println("That message field was not recognized. Please try again.");
      System.out.println();
    }
    return false;
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
