package Mils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MilsFileBuilder extends MilsAgent {

  /**
   * Constructor method to create a MILS File Parser instance. Since there is no third input parameter,
   * this instance will print output to the console.
   * @param format The format file containing MILS definitions
   * @param input The input value containing either a single MILS message or a file containing MILS messages
   */
  MilsFileBuilder(String format, String input) {
    setFormatFile(format);
    setInputValue(input);
    setMessagesParsed(new int[2]);
    getMessagesParsed()[0] = 0;
    getMessagesParsed()[1] = 0;
  }

  void build(ArrayList<String> params) {
    File input;
    File output;

    input = new File(getInputValue());
    output = new File(params.get(params.size() -1));

    if (output.isFile()) { //output file is included
      setOutputFile(output);
      if (input.isFile()) {
        //[input,output]
      }
      else {
        //[<key>=<value>,<key>=<value>, output]
      }
    }
    else { //no output file
      if (input.isFile()) {
        //buildFromFile();
      }
      else {
        buildFromKeyPairs(params);
        // [<key>=<value>,<key>=<value>,...]
      }
    }
  }

  private void buildFromKeyPairs(ArrayList<String> pairs) {
    MilsMsg milsMsgObj = new MilsMsg();
    String[] pairArr;
    String def = null;
    String milsMsgStr;
    for (String pair : pairs) {
      pairArr = pair.split("=");
      if (pairArr[0].equals("Type")) {
        def = scan(getFormatFile(), pairArr[1]);
      }
    }
    MilsMsg defObj = populateMilsMsg(milsMsgObj.get("Type"), def);
    //milsMsgStr = buildMilsMsgString(pairArr, defObj);
  }



  /*
  /**
   * If the user provides a filename as the second parameter, parse each MILS message contained within
   * @param inputFile The user-supplied input file

  private void readFromFile(File inputFile) {
    List<String> fileMessages;

    try {
      fileMessages = Files.readAllLines(inputFile.toPath());
      for (String currentMessage : fileMessages) {
        if (getOutputFile() != null) {
          setInputValue(currentMessage);
          build();
          if (currentMessage.equals(fileMessages.get(fileMessages.size() -1))) {
            System.out.println("\nOperation Completed with " + getMessagesParsed()[0] + " errors and " + getMessagesParsed()[1] +
              " messages successfully parsed to file." +
              "\nOutput Location: " + getOutputFile() + "\nBased on format rules from: " + getFormatFile());
          }
        }
        else {
          setInputValue(currentMessage);
          //build();
        }
      }
    } catch (Exception e) {
      System.err.println("Cannot read input file (" + e + ")");
    }
  }

  /**
   * If the user provides an 80-character MILS message as the second parameter, parse this message
   * @param input The user-supplied input file

  private void buildInputMessage(String input) {
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
          //writeToFile(input, milsMsgObj);
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
  */
}
