package Mils;

import java.util.Scanner;

class MilsInteractiveParser extends MilsAgent {
  private Scanner inputScanner;
  private String userMessage;
  private String messageField;
  private final static int MESSAGE_LENGTH = 80;

  /**
   * Name: MilsInteractiveParser
   * Description: Provides user interaction with the MILSAgent MILS Message operator
   * Author: Matthew Blough-Wayles
   * Last Edited: 08/25/2017
   */
  MilsInteractiveParser(String format, String message) {
    setFormatFile(format);
    setUserMessage(message);
    setMessageField(null);
    setInputScanner(new Scanner(System.in));
  }

  /**
   * Name: MilsInteractiveParser
   * Description: Provides user interaction with the MILSAgent MILS Message operator
   * Author: Matthew Blough-Wayles
   * Last Edited: 08/25/2017
   */
  MilsInteractiveParser(String format, String message, String field) {
    setFormatFile(format);
    setUserMessage(message);
    setMessageField(field);
    setInputScanner(new Scanner(System.in));
  }

  /**
   * Begin the MILS message Parse process with instant output
   */
  Boolean parse() {
    MilsMsg milsMsgObj;
    String definition;
    String result;


    if (getUserMessage().length() != MESSAGE_LENGTH) {
      System.err.println("The provided MILS message is invalid. Please try again.");
      displayParseOptions();
      return true;
    }
    definition = scan(getFormatFile(), getUserMessage().substring(0, 3));
    if (definition != null) {
      milsMsgObj = populateMilsMsg(getUserMessage(), definition);
      if (!milsMsgObj.getData().keySet().isEmpty()) {
        if (getMessageField() != null) {
          if (messageFieldInput(milsMsgObj, getMessageField())) {
            return true;
          }
        }
        result = getUserSelection(milsMsgObj);
        if (!validateUserSelection(result)) {
          return false;
        }
      }
    }
    displayParseOptions();
    return true;
  }

  private boolean messageFieldInput(MilsMsg milsMsgObj, String messageField) {
    String result;
    result = milsMsgObj.get(messageField);
    if (result != null) {
      System.out.println(result);
      return true;
    } else {
      System.err.println("That message field does not exist in this message definition.");
    }
    return false;
  }


  /**
   * After presenting the user with their parsed MILS message, give them options for proceeding without
   * re-initializing the application
   */
  private void displayParseOptions() {
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
        setFormatFile(getInputScanner().nextLine());
        parse();
        break;
      case "2":
        System.out.print("Enter New Message:  ");
        setUserMessage(getInputScanner().nextLine());
        parse();
        break;
      case "3":
        parse();
        break;
      case "4":
        System.out.print("Enter TYPE of New MILS Message [First 1-3 characters]:  ");
        userInput = getInputScanner().nextLine();
        MilsInteractiveBuilder builder = new MilsInteractiveBuilder(getFormatFile(), userInput);
        builder.setInputScanner(getInputScanner());
        builder.build();
        break;
      case "5":
        System.out.println(getUserMessage());
        displayParseOptions();
      case "6":
        return;
      default:
        System.err.println("That selection was not recognized.");
        displayParseOptions();
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

  private String getUserMessage() { return userMessage; }

  void setUserMessage(String um) {
    userMessage = um;
  }

  private String getMessageField() { return messageField; }

  private void setMessageField(String mf) { messageField = mf; }
}


