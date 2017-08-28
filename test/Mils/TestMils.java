package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TestMils {
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
  void mainParseValidMessage() {
    message = "DYYRME=&M,)QG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    Mils.main(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  DODAC\n" +
      "  Company\n" +
      "  Inventory\n" +
      "  Serial\n" +
      "  Flag\n" +
      "  Location\n" +
      "  Name\n" +
      "\n" +
      "Selection:  G*VD<<\n" +
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
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void mainParseInvalidInput() {
    message = "APC$4$^0{$>}BB'2TO*_D_.}],{//Y3XHA{2C@T|>U.<?D_CQFBZ./%#P.L*)*G=:Z5M,$QYGJX,4&|'";
    userInput = "Invalid\nName\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedErr = "That message field was not recognized. Please try again.\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainParseInvalidMessage() {
    message = "THISISANINVALIDMESSAGEOHNO";
    userInput = "6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    Mils.main(new String[] {"/parse", file, message});
    expectedOut =
      "\n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  ";
    expectedErr = "The provided MILS message is invalid. Please try again.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainBuildValidType() {
    message = "A0A";
    userInput = "NAME00\nFLA\nSER\nINV\nDODACDODACDODACDODA\nLOCATIONLOCATIO\nCOMPANY00\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    Mils.main(new String[] {"/build", file, message});
    expectedOut = "Enter 6-character value for Name:  " +
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
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void mainBuildInvalidType() {
    message = "MMM";
    userInput = "4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    Mils.main(new String[] {"/build", file, message});
    expectedOut =
      "\n" +
      "[1] Build New MILS Message\n" +
      "[2] Parse New MILS Message\n" +
      "[3] Parse this MILS Message\n" +
      "[4] Exit\n" +
      "\n" +
      "Please enter your selection:  ";
    expectedErr = "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainNotParseOrBuild() {
    message = "DYYRME=&M,)QGG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    Mils.main(new String[] {"/invalid", file, message});
    expectedErr = "Operation parameter incorrect - use /parse or /build\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainTooManyParams() {
    message = "DYYRME=&M,)QG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    Mils.main(new String[] {"/parse", file, message, "random", "param", "moreeee"});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  DODAC\n" +
      "  Company\n" +
      "  Inventory\n" +
      "  Serial\n" +
      "  Flag\n" +
      "  Location\n" +
      "  Name\n" +
      "\n" +
      "Selection:  G*VD<<\n" +
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
    assertEquals(expectedOut, actualOut.toString());
    expectedErr = "";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainNotEnoughParams() {
    Mils.main(new String[] {"/parse", file});
    expectedErr = "You have provided the wrong number of parameters.\nThe correct format is: " +
      "java Mils </parse|/build> <format file> <MILS message|type> [message field]\n";
    assertEquals(expectedErr, actualErr.toString());
  }

}