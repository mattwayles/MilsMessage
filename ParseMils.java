//region Import Statements
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
//endregion

/**
 * Name: ParseMils.java
 * Description: Retrieve a specific field from a MILS message. Expects 2 parameters; a definition file and a message.
 * Author: Matthew Blough-Wayles
 * Last Edited: 08/07/2017
 */
public class ParseMils {

    /**
     * Main method to guide user through command prompts and execute the Scan and Parse methods.
     * @param args  Command line arguments. Must be a valid file name containing message definitions, followed by an
     *              individual 80-character message, optionally followed by the message field
     */
    public static void main(String args[]) {
        //Check validity of command line parameters
        if (isValid(args)) {
            //region Declarations
            String result;
            String initialFile;
            String messageField;
            String userInputFile;
            Scanner inputScanner;
            String initialMessage;
            String userInputMessage;
            String messageDefinition;
            String userInputSelection;
            MilsMsg milsMessageObject;
            //endregion

            //region Initializations
            initialFile = args[0];
            initialMessage = args[1];
            inputScanner = new Scanner(System.in);
            messageDefinition = scan(initialFile, initialMessage.substring(0, 3)); //scan format file for matching definitions
            milsMessageObject = parse(initialMessage, messageDefinition); //parse definitions into MilsMsg hash map
            //endregion

            //region Functionality
            //If third parameter is provided, instantly return result
            if (args.length == 3 && !args[2].isEmpty()) {
                result = milsMessageObject.get(args[2]);
                if (result != null) {
                    System.out.println(result);
                    System.exit(0);
                }
                else {
                    System.out.println("That message field does not exist in this message definition.");
                }
            }
            //If matching definition is found, list field names
            if (!milsMessageObject.pairs.keySet().isEmpty()) {
                System.out.print("Which message field would you like to retrieve?");
                System.out.println();
                for (String key : milsMessageObject.pairs.keySet()) {
                    System.out.println("  " + key);
                }
                System.out.println();
                System.out.print("Selection:  ");
                messageField = inputScanner.nextLine();
                result = milsMessageObject.get(messageField);
                //Display message field from provided message token
                if (result != null) {
                    System.out.println(result);
                    System.out.println();
                    //Allow user to make selection on next move
                    System.out.println("[1] New Format File");
                    System.out.println("[2] New MILS Message");
                    System.out.println("[3] New Message Field From This MILS Message");
                    System.out.println("[4] Exit");
                    System.out.println();
                    System.out.print("Please enter your selection:  ");
                    userInputSelection = inputScanner.nextLine();
                    switch (userInputSelection) {
                        case "1": //New Format File
                            System.out.print("Enter path of new format file: ");
                            userInputFile = inputScanner.nextLine();
                            main(new String[]{userInputFile, initialMessage});
                            break;
                        case "2": //New MILS Message
                            System.out.print("Enter New Message:  ");
                            userInputMessage = inputScanner.nextLine();
                            main(new String[]{initialFile, userInputMessage});
                            break;
                        case "3": //New Field from existing MILS message
                            main(new String[]{initialFile, initialMessage});
                            break;
                        case "4": //Exit application
                            System.exit(0);
                            break;
                        default: //Any other input
                            System.out.println("That selection was not recognized.");
                            main(new String[]{initialFile, initialMessage});
                    }

                } else { //Message field input did not match choices
                    System.out.println("That message field was not recognized. Please try again.");
                    System.out.println();
                    main(new String[] {initialFile, initialMessage});
                }
            }
            else { //No definitions founds for provided message
                System.out.println("No matching definition found in this file.");
            }
            //endregion
        }
    }

    /**
     * Method that scans the provided format file for definitions matching the provided message type. Optimized to select
     * the most specific definition based on the input type.
     * @param file  The format file to scan
     * @param type  The first 1, 2, or 3 digits of the MILS message, which identifies its type
     * @return  The matching message definition from the format file
     */
    static String scan(String file, String type) {
        //region Declarations
        String definitionType;
        List<String> allFileDefinitions;
        //endregion

        //region Functionality
        try {
            //Open the file and make a list of all message definitions
            allFileDefinitions = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
           return "Could not read format file. Please try again.";
        }
        for (String currentDefinition : allFileDefinitions) {
            //Extract the message type from each definition
            definitionType = currentDefinition.substring(0,3).replaceAll("[^a-zA-Z0-9.\\-;]+", "");
            if (definitionType.equals(type)) {
                //Return the matching definition
                return currentDefinition;
            }
        }
        if (type.length() > 1) {
            //No matching definitions found, lower the specificity and try again
            return scan(file, type.substring(0, type.length() - 1));
        }
        //No matching definitions after lowest specificity has been scanned
        return "No matching definition for the provided message.";
        //endregion
    }

    /**
     * Parses the matching definition extracted from the Scan method. Using the definition constraints, this method pulls
     * each message field into a hash map. Each field value can be retrieved individually using the 'get' method.
     * @param message   The provided MILS message to be parsed and split.
     * @param definition    The MILS definition retrieved from the Scan method
     * @return  A MilsMsg object with a hash table containing all of the message field values
     */
    static MilsMsg parse(String message, String definition) {
        //region Declarations
        MilsMsg milsMsgObj;
        String messageValue;
        String[] definitionTokens;
        String[] definitionValues;
        //endregion

        //region Initializations
        milsMsgObj = new MilsMsg();//split def into tokens
        definitionTokens = definition.split(Pattern.quote("("));
        //endregion

        //region Functionality
        //Split the definition to retrieve start/end indices and field name
        for (String definitionField : definitionTokens) {
            //Clean up whitespace and non-alphanumeric characters
            definitionField = definitionField.replaceAll("[\\s )]", "");
            definitionValues = definitionField.split(Pattern.quote(","));
            if (definitionValues.length > 1) {
                //Split the MILS message for each rule provided in the message definition
                messageValue = message.substring(Integer.parseInt(definitionValues[0]) - 1,
                        Integer.parseInt(definitionValues[1]));
                milsMsgObj.add(definitionValues[2], messageValue);
            }
        }
        return milsMsgObj;
        //endregion
    }

    /**
     * Determine if application parameters are valid before continuing execution
     * @param params    Parameters provided during execution by the user. Needs to be a valid format filepath followed
     *                  by a valid 80-character MILS message (optionally followed by a message field name) to pass these checks.
     * @return  A Boolean value corresponding to the validity of the parameters
     */
    static Boolean isValid(String[] params) {
        //region Declarations
        File userInputFile;
        //endregion

        //region Functionality
        //Check for the correct number of paramters
        if (params.length != 3) {
            if (params.length != 2 || params[0] == null || params[1] == null)
            {
                System.out.println("You have provided the wrong number of arguments.");
                System.out.println("Correct syntax is 'java ParseMil <format file> <MILSMsg> [message field]'");
                return false;
            }
        }
        //Check existence of format file
        userInputFile = new File(params[0]);
        if (!userInputFile.exists()) {
            System.out.println("Invalid Parameter: No format definitions could be found in " + params[0] + ".");
            return false;
        }
        //Check MILS message length
        if (params[1].length() != 80) {
            System.out.println("Invalid Parameter: The provided MILS Message is Invalid.");
            return false;
        }
        return true;

        //endregion
    }
}
