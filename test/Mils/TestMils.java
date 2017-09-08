package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestMils {
  Mils mils;
  private String file;
  private String message;
  private String expectedOut;
  private String expectedErr;
  private String userInput;
  private String output;
  private ByteArrayOutputStream actualErr = new ByteArrayOutputStream();
  private ByteArrayOutputStream actualOut = new ByteArrayOutputStream();

  @BeforeEach
  void setUp() {
    file = "/format";
    System.setErr(new PrintStream(actualErr));
    System.setOut(new PrintStream(actualOut));
    mils = new Mils();
  }

  @AfterEach
  void tearDown() {
    System.setErr(null);
    System.setOut(null);
  }
  @Test
  void mainTooManyParams() {
    message = "DYYRME=&M,)QG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    mils.main(new String[] {"/parse", file, message, "random", "param", "moreeee"});
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
    mils.main(new String[] {"/parse", file});
    expectedErr = "You have provided the wrong number of parameters.\nThe correct format is: " +
      "java Mils </parse|/parseFile|/build> <format file> <MILS message|type|inputFile> [message field|outputFile]\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainNotParseOrBuild() {
    message = "DYYRME=&M,)QGG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    mils.main(new String[] {"/invalid", file, message});
    expectedErr = "Operation parameter incorrect - use /parse or /build\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainParseValidMessage() {
    message = "DYYRME=&M,)QG*VD<<?Z>>2Y-*0Z SU4>PA|BK.T<1^M<_B6@(CQ%J2'{<7AT}B31Y/L&=-JGNJ8{K=^";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    mils.main(new String[] {"/parse", file, message});
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
    MilsInteractiveParser parser = new MilsInteractiveParser(file, message);
    parser.parse();
    String expectedErr = "That message field was not recognized. Please try again.\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void mainParseInvalidMessage() {
    message = "THISISANINVALIDMESSAGEOHNO";
    userInput = "6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    mils.main(new String[] {"/parse", file, message});
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
    mils.main(new String[] {"/build", file, message});
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
    mils.main(new String[] {"/build", file, message});
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
  void mainParseFileNoOutput() {
    message = "/home/matt/Desktop/inputfiles/singleMilsMessage";
    mils.main(new String[] {"/parseFile", file, message});
    expectedOut = "Y2$UD)DQEIY</P)M&|@Z(WI\\$Q;XH-+#A]\\'L>N/1-UT.];E900[:.5TPX@^188I@AB *JV5C)W+1234\n" +
      "Type: Y2\nInventory: UD)DQE\nName: Y</\n" +
      "Company: P)M&|@Z\nDODAC: Q;XH-+#A]\\'L>N\nFlag: -UT.];E900[:.5T\nSerial: PX@^\nLocation: 188I@AB *JV5C)\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void mainParseFileOutput() {
    String actual = "";
    String expected;
    File outputFile;
    message = "/home/matt/Desktop/inputfiles/multipleValidMilsMsgs";
    output = "/home/matt/Desktop/outputFile";
    mils.main(new String[] {"/parseFile", file, message, output});
    outputFile = new File(output);
    List<String> lines;

    try {
      lines = Files.readAllLines(outputFile.toPath());
      for (String line : lines) {
        actual += line + "\n";
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    expected = "A?#[J3#L^9:2BOD.]N.PN(.>X0M<H@_@B8A#(2T3GXD8N)C=GE,?S, +U<;OJCFSWM>J$N 3>_LGW=KX\nType: A\nLocation: #[\n" +
      "Company: :2BOD.\nName: ]N.PN(\nFlag: N(.>X0M<H@_@B8A#(2T\nInventory: 3GXD8N)C=GE\nDODAC: ,?S, +U<;OJ\nSerial: N 3>_LGW=KX\n" +
      "DS#P1SH5O&U:|:I,&W'<_,)}85C-_9X:SLBYB1E06FVMR<T :|V%R &0 &'8)B[CY.,PUF<S+'0*IL5K\nType: D\nDODAC: DS\nCompany: #P\n" +
      "Inventory: 1S\nSerial: H5\nFlag: O&\nLocation: U:\nName: |:I,&W\n" +
      "C3F;6PJ?]\\XHEV0;-(S#Q8S7V'Z@Y\\QNZ'\\I ]:C%1L1S615XIQ@+TC{|:K6IKG636''C;OV*B*O?^X8\nType: C\nFlag: 3F\nCompany: ;6PJ?]\n" +
      "DODAC: \\XHEV0;\nLocation: (S#Q8S7V'Z@Y\\QNZ'\\I ]:\nName: C%1L1S615XIQ@+TC{|\nInventory: 6IKG636'\n" +
      "Serial: 'C;OV*B*O?^\n";
    expectedOut = "Successfully parsed A?#[J3#L^9:2BOD.]N.PN(.>X0M<H@_@B8A#(2T3GXD8N)C=GE,?S, +U<;OJCFSWM>J$N 3>_LGW=KX\n" +
      "Successfully parsed DS#P1SH5O&U:|:I,&W'<_,)}85C-_9X:SLBYB1E06FVMR<T :|V%R &0 &'8)B[CY.,PUF<S+'0*IL5K\n" +
      "Successfully parsed C3F;6PJ?]\\XHEV0;-(S#Q8S7V'Z@Y\\QNZ'\\I ]:C%1L1S615XIQ@+TC{|:K6IKG636''C;OV*B*O?^X8\n" +
      "\nOperation Completed with 0 errors and 3 messages successfully parsed to file.\nOutput Location: " +
      "/home/matt/Desktop/outputFile\nBased on format rules from: /format\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expected, actual);
    assertEquals(expectedOut, actualOut.toString());
  }
  /**
   * ThirdParamExists Tests - Called during a parse operation, this method checks to see if a third parameter was input
   * by the user. This parameter identifies a specific field to extract the value for. If the parameter exists, display
   * the value to the user immediately and present options to move forward. The method is only called after all parameters
   * have been validated, and the extracted message definition is proven non-null. The method has a couple of conditions,
   * tested through the following tests:
   *   Third parameter exists with a valid, non-empty value
   *   Third parameter exists but is an empty string
   *   Third parameter exists, but is not a valid field present in the object's hash map
   *   Third parameter does not exist
   */
  @Test
  void thirdParamExistsAndNotEmpty() {
    message = "BHVKYU{C4V]IN8=_7M>X+7LE_61M%^ *LTZO|I*_[210NXK3[>>OPQ/F?+99DD^'32/L>? 4M=*O%/+-";
    expectedOut = "LTZO|I*_[210NXK3[>>OPQ/F?\n";
    mils.main(new String[] {"/parse", file, message, "Company"});
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void thirdParamExistsButIsntValidField() {
    message = "BHVKYU{C4V]IN8=_7M>X+7LE_61M%^ *LTZO|I*_[210NXK3[>>OPQ/F?+99DD^'32/L>? 4M=*O%/+-";
    expectedErr = "That message field does not exist in this message definition.\n";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    mils.main(new String[] {"/parse", file, message, "InvalidField"});
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void thirdParamDNE() {
    message = "BHVKYU{C4V]IN8=_7M>X+7LE_61M%^ *LTZO|I*_[210NXK3[>>OPQ/F?+99DD^'32/L>? 4M=*O%/+-";
    userInput = "Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    mils.main(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  Inventory\n" +
      "  Serial\n" +
      "  Flag\n" +
      "  Name\n" +
      "  Company\n" +
      "  Location\n" +
      "  DODAC\n" +
      "\n" +
      "Selection:  +7LE_61M%^ *\n" +
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

}