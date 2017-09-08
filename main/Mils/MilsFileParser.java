package Mils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

class MilsFileParser extends MilsAgent {

  /**
   * Constructor method to create a MILS File Builder instance. Since there is no third input parameter,
   * this instance will print output to the console.
   * @param format The format file containing MILS definitions
   * @param input The input value containing either a single MILS message or a file containing MILS messages
   */
  MilsFileParser(String format, String input) {
    setFormatFile(format);
    setInputValue(input);
    setMessagesParsed(new int[2]);
    getMessagesParsed()[0] = 0;
    getMessagesParsed()[1] = 0;
  }

  /**
   * Constructor method to create a MILS File Builder instance. Since there is a third input parameter,
   * this instance will print output to the specified output file.
   * @param format The format file containing MILS definitions
   * @param input The input value containing either a single MILS message or a file containing MILS messages
   * @param output The output file to write parsed data into
   */
  MilsFileParser(String format, String input, File output) {
    setFormatFile(format);
    setInputValue(input);
    setOutputFile(output);
    setMessagesParsed(new int[2]);
    getMessagesParsed()[0] = 0;
    getMessagesParsed()[1] = 0;
  }

  /**
   * This method determines if the input value is a MILS message or a file, and distributes
   * the work accordingly
   */
  void parse()  {
    File inputFile;

    inputFile = new File(getInputValue());

    if (!inputFile.isFile()) {
      parseInputMessage(getInputValue());
    }
    else {
      readInputFile(inputFile);
    }
  }

  /**
   * If the user provides a filename as the second parameter, parse each MILS message contained within
   * @param inputFile The user-supplied input file
   */
  private void readInputFile(File inputFile) {
    List<String> fileMessages;

    try {
      fileMessages = Files.readAllLines(inputFile.toPath());
      for (String currentMessage : fileMessages) {
        if (getOutputFile() != null) {
          setInputValue(currentMessage);
          parse();
          if (currentMessage.equals(fileMessages.get(fileMessages.size() -1))) {
            System.out.println("\nOperation Completed with " + getMessagesParsed()[0] + " errors and " + getMessagesParsed()[1] +
              " messages successfully parsed to file." +
              "\nOutput Location: " + getOutputFile() + "\nBased on format rules from: " + getFormatFile());
          }
        }
        else {
          setInputValue(currentMessage);
          parse();
        }
      }
    } catch (Exception e) {
      System.err.println("Cannot read input file (" + e + ")");
    }
  }

  /**
   * If the user provides an 80-character MILS message as the second parameter, parse this message
   * @param input The user-supplied input file
   */
  private void parseInputMessage(String input) {
    MilsMsg milsMsgObj;
    String definition;

    if (input.length() != MESSAGE_LENGTH) {
      System.err.println("Invalid input detected: " + input);
      getMessagesParsed()[0]++;
      return;
    }
    definition = scan(getFormatFile(), input.substring(0, 3));
    if (definition != null) {
      milsMsgObj = populateMilsMsg(input, definition);
      if (!milsMsgObj.getData().keySet().isEmpty()) {
        if (getOutputFile() != null) {
          writeToFile(input, milsMsgObj);
          getMessagesParsed()[1]++;
          System.out.println("Successfully parsed " + input);
        }
        else {
          System.out.println(input);
          for (String key : milsMsgObj.getData().keySet()) {
            System.out.println(key + ": " + milsMsgObj.get(key));
          }
        }
      }
    }
    else {
      System.err.println(input);
    }
  }

  /**
   * If an output file is specified, create this file and write to it. Alternatively, append to an
   * existing file.
   * @param input The user-supplied input file
   * @param milsMsgObj  The user-supplied output file or filepath
   */
  private void writeToFile(String input, MilsMsg milsMsgObj) {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(getOutputFile(), true));
      writer.println(input);
      for (String key : milsMsgObj.getData().keySet()) {
        writer.println(key + ": " + milsMsgObj.get(key));
        }
        writer.close();
    }
    catch (IOException e) {
      System.err.println("Error writing to output file.");
      getMessagesParsed()[0]++;
      getMessagesParsed()[1]--;
    }
  }

}
