package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TestMilsInteractiveParser {
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
  void parseMatchingMessageValidInput() {
    message = "ZZZ?)H6N0;LA,2|4TUA-1Q}6$1X{$U+({/NU,&3[M BW8.9W@ZC4IU{ 7V 5M-KF,&7:&D<816WLRO&M";
    userInput = "Type" + System.getProperty("line.separator") + "6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  This\n" +
      "  Is\n" +
      "  A\n" +
      "  Completely\n" +
      "  Random\n" +
      "  Value\n" +
      "  List\n" +
      "\n" +
      "Selection:  ZZZ\n" +
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
  void parseMatchingMessageInvalidInput() {
    message = "APC$4$^0{$>}BB'2TO*_D_.}],{//Y3XHA{2C@T|>U.<?D_CQFBZ./%#P.L*)*G=:Z5M,$QYGJX,4&|'";
    userInput = "Invalid\nName"
      + System.getProperty("line.separator") + "6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] { "/parse", file, message});
    String expectedErr = "That message field was not recognized. Please try again.\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseNonMatchingMessage() {
    message = "6.UBS5W$]1< X%}6FZ;_=.(R-@V&}G+I22[&/;>3E>%^LE-%]#DN9}Z4>1,84=DL*}4OG0QJ9^6]VER|";
    userInput = "6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file,message});
    expectedErr = "No matching definition for the provided message.\n";
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseSelectNewFormatFileInvalidFile() {
    message = "A0A@ HE+W:T UO[5{G1GIUK}BG_LNB{F 6{?HY6#GXGT8=8.'B2FPWP*^W0KIWU;],?R7_Q1{)8+G$KK";
    userInput = "Name\n1\n/Invalid/Path/Name\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  Name\n" +
      "  Flag\n" +
      "  Serial\n" +
      "  Inventory\n" +
      "  DODAC\n" +
      "  Location\n" +
      "  Company\n" +
      "\n" +
      "Selection:  @ HE+W\n" +
      "\n" +
      "\n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  " +
      "Enter path of new format file: \n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  ";
    expectedErr = "Not a valid MILS format file. Please try again.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseSelectNewFormatFileValidNonMilsFile() {
    message = "A0'1O3MC%3?; XK=_]_MCX%_HY/XH;1ESQPGLA|@3}PWYP$1${G&^D7##NP}4/ S[V9XJ]1KPRU+3M'F";
    userInput = "Serial\n1\n/home/matt/Desktop/fakeTest\n6\n6\n7";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  Company\n" +
      "  Location\n" +
      "  DODAC\n" +
      "  Inventory\n" +
      "  Serial\n" +
      "  Flag\n" +
      "  Name\n" +
      "\n" +
      "Selection:  $1${G&^D7##\n" +
      "\n" +
      "\n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  " +
      "Enter path of new format file: \n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  ";
    expectedErr = "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseSelectNewFormatFileValidMilsFile() {
    message = "A>5='Y7:*WH5 _.GQ'70S)|AD_1)7P(_&T_ @ZY#UH^CR09?DZSIYLPRY<_O^56DB}49/1EMYR 22>^+";
    userInput = "DODAC\n1\n/format\nType\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    expectedOut = "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  Location\n" +
      "  Company\n" +
      "  Name\n" +
      "  Flag\n" +
      "  Inventory\n" +
      "  DODAC\n" +
      "  Serial\n" +
      "\n" +
      "Selection:  SIYLPRY<_O^\n" +
      "\n" +
      "\n" +
      "[1] New Format File\n" +
      "[2] Parse New MILS Message\n" +
      "[3] New Message Field From This MILS Message\n" +
      "[4] Build New MILS Message\n" +
      "[5] Rebuild This MILS Message\n" +
      "[6] Exit\n" +
      "\n" +
      "Please enter your selection:  " +
      "Enter path of new format file: " +
      "Which message field would you like to retrieve?\n" +
      "  Type\n" +
      "  Location\n" +
      "  Company\n" +
      "  Name\n" +
      "  Flag\n" +
      "  Inventory\n" +
      "  DODAC\n" +
      "  Serial\n" +
      "\n" +
      "Selection:  A\n" +
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
    assertEquals("", actualErr.toString());
  }
  @Test
  void parseNewMilsMsgInvalidMsg() {
    message = "B0B  )69.%>>PD?()6;OG7E'G (2G{ M[N}:(P9WU(P_D7H8({X%16JO_O]>*XVW(CF?@@&W;,OUXS1X";
    userInput = "Flag\n2\nATHISISAFAKEMILSMESSAGE\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Serial\n" +
        "  Location\n" +
        "  Flag\n" +
        "  DODAC\n" +
        "  Name\n" +
        "  Company\n" +
        "  Inventory\n" +
        "\n" +
        "Selection:  PD?()6\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter New Message:  \n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "The provided MILS message is invalid. Please try again.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseNewMilsMsgValidMsgNoDef() {
    message = "B/K^@PUK5];L}-4IX,6,9G4.I;M3BQ@VD:^[*&IQ#8&P08Z+'_PH8] -R)R18P*MV@_E) H_CD6A-8<L";
    userInput = "Inventory\n2\n&9@DCQPZA4'%[VI, W<JL846AKMZ2.-(,2[7F+./579B4EINY=/[+682F>^H<'B2=]9)40E{EDY@O{;8\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Inventory\n" +
        "  Serial\n" +
        "  Flag\n" +
        "  Name\n" +
        "  Company\n" +
        "  Location\n" +
        "  DODAC\n" +
        "\n" +
        "Selection:  K^@\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
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
    String expectedErr = "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseNewMilsMsgValidMsgWithDef() {
    message = "C,*6=^>I< R(/^E,QV%P8-^CQ${F|{;4NKEFZ?HO*EQ6.H?70HAW0^,W/D#A#[V,$@S[PHKLW>}(}_0Q";
    userInput = "Company\n2\nZZZ@DCQPZA4'%[VI, W<JL846AKMZ2.-(,2[7F+./579B4EINY=/[+682F>^H<'B2=]9)40E{EDY@O{8\nValue\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Flag\n" +
        "  Company\n" +
        "  DODAC\n" +
        "  Location\n" +
        "  Name\n" +
        "  Inventory\n" +
        "  Serial\n" +
        "\n" +
        "Selection:  6=^>I<\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter New Message:  " +
        "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  This\n" +
        "  Is\n" +
        "  A\n" +
        "  Completely\n" +
        "  Random\n" +
        "  Value\n" +
        "  List\n" +
        "\n" +
        "Selection:  4'\n" +
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
    String expectedErr = "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseNewMilsMsgFromThisMilsMsg() {
    message = "C4K=:R}N<U|CQTB0[5$@6DC/D5;;7@R+(4Y0D6GAX+U2|O_M[3N'#D<CC;>,^:)-LY^NR 2&7EY/K;N,";
    userInput = "DODAC\n3\nInventory\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  DODAC\n" +
        "  Serial\n" +
        "  Inventory\n" +
        "  Name\n" +
        "  Location\n" +
        "  Company\n" +
        "  Flag\n" +
        "\n" +
        "Selection:  }N<\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  DODAC\n" +
        "  Serial\n" +
        "  Inventory\n" +
        "  Name\n" +
        "  Location\n" +
        "  Company\n" +
        "  Flag\n" +
        "\n" +
        "Selection:  5$@\n" +
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
    String expectedErr = "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseBuildNewMilsMsgInvalidType() {
    message = "CIC+:O}$&@|.VDZ<58[O47W$FJ.1J<Q|*,@>G/H-,8:Y<J?<:J.>'[]ZE5A/7XLY0>@%{^N#>I)XP6Z5";
    userInput = "Company\n4\nINV\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Name\n" +
        "  Company\n" +
        "  Flag\n" +
        "  Location\n" +
        "  Serial\n" +
        "  DODAC\n" +
        "  Inventory\n" +
        "\n" +
        "Selection:  VD\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter TYPE of New MILS Message [First 1-3 characters]:  \n" +
        "[1] Build New MILS Message\n" +
        "[2] Parse New MILS Message\n" +
        "[3] Parse this MILS Message\n" +
        "[4] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "No matching definition for the provided message.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseBuildNewMilsMsgValidType() {
    message = "CIX:@,#Z>OL^B(|SCP_C_M6B^*5?.<X_].*U8$#O*7?N.T,AEC@:GJ|WVUPV<3EH<M>30SRWF-Z ;^RT";
    userInput = "Inventory\n4\nD9D\nSERIALSERI\nDODACDODAC\nFLAGFLAGFL\nNAMENAMENA\nINVENTORY0\nCOMPANY\nLOCATION00\n4";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Location\n" +
        "  Inventory\n" +
        "  DODAC\n" +
        "  Name\n" +
        "  Flag\n" +
        "  Serial\n" +
        "  Company\n" +
        "\n" +
        "Selection:  ^B(|\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "Enter TYPE of New MILS Message [First 1-3 characters]:  " +
        "Enter 10-character value for Serial:  " +
        "Enter 10-character value for DODAC:  " +
        "Enter 10-character value for Flag:  " +
        "Enter 10-character value for Name:  " +
        "Enter 10-character value for Inventory:  " +
        "Enter 7-character value for Company:  " +
        "Enter 10-character value for Location:  \n" +
        "Your constructed MILS message is:\n" +
        "\n" +
        "D9D   SERIALSERI DODACDODAC FLAGFLAGFL NAMENAMENA INVENTORY0  COMPANY LOCATION00\n" +
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
  void parseRebuildThisMsg() {
    message = "C4X2DUMWC }=BUC*]*J}[58T8DKW:&={L'B.AB]IOB?JVUT#I^1:23CX9U=L#^:NCX3#+N*{'{$9L9@#";
    userInput = "Flag\n5\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Flag\n" +
        "  Name\n" +
        "  Inventory\n" +
        "  Serial\n" +
        "  Company\n" +
        "  DODAC\n" +
        "  Location\n" +
        "\n" +
        "Selection:  2DUMW\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "C4X2DUMWC }=BUC*]*J}[58T8DKW:&={L'B.AB]IOB?JVUT#I^1:23CX9U=L#^:NCX3#+N*{'{$9L9@#" +
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
    String expectedErr = "";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void parseSelectionNotRecognized() {
    message = "C4X2DUMWC }=BUC*]*J}[58T8DKW:&={L'B.AB]IOB?JVUT#I^1:23CX9U=L#^:NCX3#+N*{'{$9L9@#";
    userInput = "Flag\n5\n9\n6";
    System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    MilsInteractiveParser parser = new MilsInteractiveParser();
    parser.parse(new String[] {"/parse", file, message});
    String expectedOut =
      "Which message field would you like to retrieve?\n" +
        "  Type\n" +
        "  Flag\n" +
        "  Name\n" +
        "  Inventory\n" +
        "  Serial\n" +
        "  Company\n" +
        "  DODAC\n" +
        "  Location\n" +
        "\n" +
        "Selection:  2DUMW\n" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "C4X2DUMWC }=BUC*]*J}[58T8DKW:&={L'B.AB]IOB?JVUT#I^1:23CX9U=L#^:NCX3#+N*{'{$9L9@#" +
        "\n" +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  " +
        "\n" +
        "[1] New Format File\n" +
        "[2] Parse New MILS Message\n" +
        "[3] New Message Field From This MILS Message\n" +
        "[4] Build New MILS Message\n" +
        "[5] Rebuild This MILS Message\n" +
        "[6] Exit\n" +
        "\n" +
        "Please enter your selection:  ";
    String expectedErr = "That selection was not recognized.\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }

  //////////////////////////////////////////////Build Tests

}