package Mils;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Name: MilsAgent.java
 * Description: Retrieve a specific field from a MILS message. Will eventually support both interactive
 *  user-supplied values and data extracted from files.
 * Author: Matthew Blough-Wayles
 * Last Edited: 08/25/2017
 */
class MilsAgent {

  /**
   * Method that scans the provided format file for definitions matching the provided message type. Optimized to select
   * the most specific definition based on the input type.
   *
   * @param file The format file to scan
   * @param type The first 1, 2, or 3 digits of the MILS message, which identifies its type
   * @return The matching message definition from the format file
   */
  String scan(String file, String type) {
    String definitionType;
    List<String> allFileDefinitions;

    try {
      allFileDefinitions = Files.readAllLines(Paths.get(file));
      for (String currentDefinition : allFileDefinitions) {
        definitionType = currentDefinition.substring(0, 3).replaceAll("[^a-zA-Z0-9.\\-;]+", "");
        if (definitionType.equals(type)) {
          return currentDefinition;
        }
      }
    } catch (Exception e) {
      System.err.println("Not a valid MILS format file. Please try again.");
      return null;
    }
    if (type.length() > 1) {
      return scan(file, type.substring(0, type.length() - 1));
    }
    System.err.println("No matching definition for the provided message.");
    return null;
  }

  /**
   * Using the constraints in the supplied definition, this method pulls each message field into a hash map.
   * Each field value can be retrieved individually using the 'get' method.
   *
   * @param message The MILS message being built or parsed in this operation
   * @param definition The definition associated with the supplied message
   * @return A MilsMsg object with a hash map containing all of the message field values
   */

  MilsMsg populateMilsMsg(String message, String definition) {
    int startIndex;
    int endIndex;
    String fieldName;
    MilsMsg milsMsgObj;
    String messageValue;
    String[] definitionValues;

    milsMsgObj = new MilsMsg();

    String[] definitionTokens = definition.split(Pattern.quote("("));
    for (String definitionField : definitionTokens) {
      definitionField = definitionField.replaceAll("[\\s )]", "");
      definitionValues = definitionField.split(Pattern.quote(","));
      if (definitionValues.length == 1) {
        milsMsgObj.add("Type", definitionValues[0]);
      } else {
        startIndex = Integer.parseInt(definitionValues[0]);
        endIndex = Integer.parseInt(definitionValues[1]);
        fieldName = definitionValues[2];
        if (message.length() > 3) {
          messageValue = message.substring(startIndex - 1, endIndex);
          milsMsgObj.add(fieldName, messageValue);
        } else {
          milsMsgObj.add(fieldName, startIndex + " " + endIndex);
        }
      }
    }
    return milsMsgObj;
  }

  /**
   * Create a string from a built MILS message character array, while preserving whitespace
   * @param milsInt The MILS Interactive Builder object constructing the message
   * @param milsMsgObj The object containing the message fields and value indices
   * @return A string representation of the MILS Message character array
   */
  String buildMilsMsgString(MilsInteractiveBuilder milsInt, MilsMsg milsMsgObj) {
    char[] milsMsg = buildMilsMsg(milsInt, milsMsgObj);
    StringBuilder milsMsgStr = new StringBuilder();
    for (char c : milsMsg) {
      if (c == 0) {
        c = 32;
      }
      milsMsgStr.append(c);
    }
    return milsMsgStr.toString();
  }

  /**
   * Build a MILS message by inserting MILSMsg Object HashMap values into a character array at specified
   * indices.
   * @param milsInt The MILS Interactive Builder object constructing the message
   * @param milsMsgObj The object containing the message fields and value indices
   * @return  The MILS Message character array
   */
  private char[] buildMilsMsg(MilsInteractiveBuilder milsInt, MilsMsg milsMsgObj) {
    int i;
    int[] indices;
    char[] currentField;
    char[] milsMsgChar;

    milsMsgChar = new char[80];

    for (String field : milsMsgObj.getData().keySet()) {
      if (field.equals("Type")) {
        currentField = milsMsgObj.get(field).toCharArray();
        for (i = 0; i < currentField.length; i++) {
          milsMsgChar[i] = currentField[i];
        }
      } else {
        indices = getIndices(milsMsgObj.get(field));
        if (indices.length != 1) {
          milsMsgObj.add(field, milsInt.getMessageFieldInput(field, (indices[1] - indices[0]) + 1));

        }
        currentField = milsMsgObj.get(field).toCharArray();
        for (i = 0; i <= currentField.length - 1; i++) {
          milsMsgChar[indices[0] - 1 + i] = currentField[i];
        }
      }
    }
    return milsMsgChar;
  }

  /**
   * Convert string index values to integers for mathematical operations
   * @param str The string message value containing the two indices separated by a space
   * @return  An integer array containing the two indices
   */
  private int[] getIndices(String str) {
    String[] strArr;
    int[] intArr;

    strArr = str.split(" ");
    intArr = new int[strArr.length];
    for (int i = 0; i < strArr.length; i++) {
      intArr[i] = Integer.parseInt(strArr[i]);
    }
    return intArr;
  }
}
