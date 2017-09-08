package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMilsInteractiveBuilder {
  private String file;
  private String message;
  private String expectedOut;
  private String expectedErr;
  private String userInput;
  private ByteArrayOutputStream actualErr = new ByteArrayOutputStream();
  private ByteArrayOutputStream actualOut = new ByteArrayOutputStream();

  @BeforeEach
  void setUp() {
    file = "/format";
    System.setErr(new PrintStream(actualErr));
    System.setOut(new PrintStream(actualOut));
  }

  @AfterEach
  void tearDown() {
    System.setErr(null);
    System.setOut(null);
  }
  @Test
  void buildValidInput() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n4";

    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildInvalidInput() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSERIAL\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "Invalid input. Please try again.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildInvalidType() {
    message = "IDK";
    userInput = "4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    expectedOut =
      "\n" +
      "[1] Build New MILS Message\n" +
      "[2] Parse New MILS Message\n" +
      "[3] Parse this MILS Message\n" +
      "[4] Exit\n" +
      "\n" +
      "Please enter your selection:  ";
    String expectedErr =  "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildNewMessageSelectionInvalidType() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n1\nJK\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter TYPE of New MILS Message [First 1-3 characters]:  " +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";

    String expectedErr =  "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildNewMessageSelectionValidType() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n1\nY\n" +
      "NA\nSE\nLO\nIN\nFL\nDO\nCO\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter TYPE of New MILS Message [First 1-3 characters]:  " +
        "Enter 2-character value for Name:  " +
        "Enter 2-character value for Serial:  " +
        "Enter 2-character value for Location:  " +
        "Enter 2-character value for Inventory:  " +
        "Enter 2-character value for Flag:  " +
        "Enter 2-character value for DODAC:  " +
        "Enter 2-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "Y                                                                 NASELOINFLDOCO\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";

    String expectedErr =  "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildParseNewMessageSelectionInvalidMsg() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n2\nINVALIDMSG\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter New Message:  " +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  ";

    String expectedErr =  "The provided MILS message is invalid. Please try again.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildParseNewMessageSelectionValidMsg() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n2\n" +
      "D1? WV]W-%0) C(BI{\\&{P5MST+|*|Q61BEK(R@ \\=:AB=PSYO|\\=Q32>EN|T+J?WM&\\IA>Z}@%<(\\.U\nCompany\n6";

    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter New Message:  " +
        "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Location\n" +
        "  Inventory\n" +
        "  Company\n" +
        "  DODAC\n" +
        "  Name\n" +
        "  Serial\n" +
        "  Flag\n" +
        "\n" +
        "Selection:  5MST+|\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  ";

    String expectedErr =  "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildParseThisMilsMsg() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n3\nCompany\n6";

    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Name\n" +
        "  Flag\n" +
        "  Serial\n" +
        "  Inventory\n" +
        "  DODAC\n" +
        "  Location\n" +
        "  Company\n" +
        "\n" +
        "Selection:  COMPANY00\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  ";

    String expectedErr =  "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void buildSelectionNotRecognized() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n8\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder builder = new MilsInteractiveBuilder(file, message);
    builder.build();
    String expectedOut =
      "Enter 6-character value for Name:  " +
        "Enter 3-character value for Flag:  " +
        "Enter 3-character value for Serial:  " +
        "Enter 3-character value for Inventory:  " +
        "Enter 19-character value for DODAC:  " +
        "Enter 15-character value for Location:  " +
        "Enter 9-character value for Company:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "A0ANAME00FLA     SER   INV     DODACDODACDODACDODA LOCATIONLOCATIO     COMPANY00\n" +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "\n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "That selection was not recognized.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
}
