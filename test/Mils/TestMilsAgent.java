package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TestMilsAgent {
  private String file;
  private String message;
  private String expected;
  private String actual;
  private MilsAgent agent = new MilsAgent();
  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  @BeforeEach
  void setUp() {
    file = "/home/matt/IdeaProjects/MilsMessageScanner/format";
    System.setErr(new PrintStream(errContent));
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    file = null;
    System.setErr(null);
    System.setOut(null);
  }

  /**
   * Scan Tests - Receives a file and a MILS message and returns the most specific MILS message definition from the file.
   * The areParamsValid() method already checks for parameter validity; so these tests focus on:
   *   Returning the most specific definition
   *   Proper error handling for a valid message with no definition
   *   Proper error handling for a valid file with no message definitions
   *   Proper error handling for a valid file that can't be accessed due to permissions
   */
  @Test
  void scanMostSpecificExample() {
    message = "Y2KT4-/]M|YJK 3\\+[A@/]<@:^8&$$]6L8)+>FS88|BR\\0JN|FI^^}MP=R[4R4+3[2F3)XDNN,,1$1*3";
    expected = "Y2K, (25, 30, Inventory), (42, 44, Flag), (48, 60, Location), (61, 63, Serial), (68, 71, DODAC), (74, 76, Name), (78, 79, Company)";
    actual = agent.scan(file, message);
    assertEquals(expected, actual);
  }
  @Test
  void scanLessSpecificExample() {
    message = "Y24>,[8J=%?U'&)@KJ6@[U\\.H%RAX@/XI11@&0N5RMCYF46*)*'4V@:O4Z=N=0=S8>ZJB+JY&%=|,@J;";
    expected = "Y2, (4, 9, Inventory), (11, 13, Name), (14, 20, Company), (26, 39, DODAC), (42, 56, Flag), (57, 60, Serial), (61, 74, Location)";
    actual = agent.scan(file, message);
    assertEquals(expected, actual);
  }
  @Test
  void scanLeastSpecificExample() {
   message = "Y<I3B*<_12@->BGC>ZS#:<.L[5&V;I-0#&+/;Y}\\4U.S@.S/D{F1)QE{\\9F]LI[&A%\\6B'</YXA|DT;-";
   expected = "Y, (67, 68, Name), (69, 70, Serial), (71, 72, Location), (73, 74, Inventory), (75, 76, Flag), (77, 78, DODAC), (79, 80, Company)";
   actual = agent.scan(file, message);
   assertEquals(expected, actual);
  }
  @Test
  void scanValidMessageWithNoAssociatedRule() {
    message = ");#+_ZMT,?BX00W\\{L:IRI%P5VX5KM;.^2QT8BSE,EH',FO8$SPVYZ?0=*2+{]$GO<[<>A?5%][^IW$0";
    expected = "No matching definition for the provided message.\n";
    actual = agent.scan(file, message);
    assertEquals(expected, errContent.toString());
    assertEquals(actual, null);
  }
  @Test
  void scanExistingFileThatDoesntHaveFormatRules() {
    file = "/home/matt/IdeaProjects/MilsMessageScanner/src/main.Mils/MilsMsg.java";
    message = "$V:X:>| >,PE<.&{(1F?  T[I2$>)62Y+T_:>O3]IZW ENPUT^DJ|%TL6#QE(GAD0W(${Y=(%^%BMV>[";
    expected = "Not a valid MILS format file. Please try again.\n";
    actual = agent.scan(file, message);
    assertEquals(expected, errContent.toString());
    assertEquals(actual, null);
  }
  @Test
  void scanFileWithNoPermissionToRead() {
    file = "/home/matt/Desktop/test";
    message = "$V:X:>| >,PE<.&{(1F?  T[I2$>)62Y+T_:>O3]IZW ENPUT^DJ|%TL6#QE(GAD0W(${Y=(%^%BMV>[";
    expected = "Not a valid MILS format file. Please try again.\n";
    actual = agent.scan(file, message);
    assertEquals(expected, errContent.toString());
    assertEquals(actual, null);
  }

  /**
   * PopulateMilsMsg Tests - Creates a MILS message object and populates its LinkedHashTable based on definition
   * constraints. This method cannot be called unless all parameters are validated and 'definition' is non-null.
   * Behaves differently when asked to parse vs. build, so two tests exist:
   *   Parse - Take the indices from each definition token and extract the message field from the provided message
   *   Build - Apply the indices from each definition token into the HashMap's 'value' field. This allows us to
   *     compute the field length when querying for user input, ensuring correct input
   */
  @Test
  void populateMilsMsgParse() {
    message = "C4UU&O%>SD59@[{T8R%8.?[&_RN]YO_JRH>AY,Q4GK>'9O'_*C ]S[*%N5*V70G.BQ9#{UE5)A9<,R]^";
    MilsMsg testMsg = new MilsMsg();
    testMsg.add("Type", "C4");
    testMsg.add("DODAC", "%>S");
    testMsg.add("Serial", "59@[{T");
    testMsg.add("Inventory", "R%8");
    testMsg.add("Name", ".?[&_RN]YO_JRH>AY,Q");
    testMsg.add("Location", "4GK>'9O'_*C");
    testMsg.add("Company", "5*V70G.BQ9");
    testMsg.add("Flag", "#{UE5)A9<,R]^");
    MilsMsg actualMsg = agent.populateMilsMsg(message, agent.scan(file, message));
    assertEquals(testMsg.getData(), actualMsg.getData());
  }

  @Test
  void populateMilsMsgBuild() {
    message = "D9D6}@,5}4UJ3 &.R|[B2^W<1]{04HOK?}+&L&,@4 D{UYC%]/XO[8CIZT_.:|B=6EM[*G>?KI^%/}C?";
    MilsMsg testMsg = new MilsMsg();
    testMsg.add("Type", "D9D");
    testMsg.add("Serial", ",5}4UJ3 &.");
    testMsg.add("DODAC", "|[B2^W<1]{");
    testMsg.add("Flag", "4HOK?}+&L&");
    testMsg.add("Name", "@4 D{UYC%]");
    testMsg.add("Inventory", "XO[8CIZT_.");
    testMsg.add("Company", "B=6EM[*");
    testMsg.add("Location", ">?KI^%/}C?");
    MilsMsg actualMsg = agent.populateMilsMsg(message, agent.scan(file, message));
    assertEquals(testMsg.getData(), actualMsg.getData());
  }

  /**
   * BuildMilsMsgString Test - Takes a created MilsMsg object and gathers user input to construct a new MILS message.
   * Error-checking is done within the method, and loops until correct data is input. Therefore, the only test to run is:
   *   -Building valid MILS message from valid user input
   */
  @Test
  void buildMilsMsgString() {
    String userInput = "LOCATIONLOCATI\nINV\nCOMPAN\nDOD\nN\nSERIAL\nFLAGFLAGFLA";
    message = "D1E";
    String[] params = new String[] {"/build", file, message};
    MilsMsg actualObj = agent.populateMilsMsg(message, agent.scan(file, message));
    expected = "D1LOCATIONLOCATI   INVCOMPAN           DOD        N    SERIAL       FLAGFLAGFLA ";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveBuilder milsInt = new MilsInteractiveBuilder(file, message);
    String actual = agent.buildMilsMsgString(actualObj);
    assertEquals(expected, actual);
  }
}