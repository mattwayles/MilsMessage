package Mils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMilsFileParser {
  private String file;
  private String message;
  private File output;
  private String actual;
  private String expected;
  private String expectedOut;
  private String expectedErr;
  private MilsFileParser fParser;
  private ByteArrayOutputStream actualErr = new ByteArrayOutputStream();
  private ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
  @BeforeEach
  void setUp() {
    file = "/format";
    output = new File("");
    actual = "";
    System.setErr(new PrintStream(actualErr));
    System.setOut(new PrintStream(actualOut));
  }

  @AfterEach
  void tearDown() {
    System.setErr(null);
    System.setOut(null);
    try {
      Files.delete(output.toPath());
    }
    catch (IOException ignored) {}
  }

  @Test
  void paramsAreFormatAndValidMilsMessage() {
    message = "Y2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedOut = "Y2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G\n" +
      "Type: Y2K\nInventory: CN?*,O\nFlag: CCG\n" +
      "Location: W:E}%H>AEW2U*\nSerial: 61T\nDODAC: }9AA\nName: +E6\nCompany: 2#\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void paramsAreFormatAndValidMilsMessageWithoutDefinition() {
    message = "P2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "No matching definition for the provided message.\n" +
    "P2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndInvalidMilsMessage() {
    message = "THISRIGHTHEREISANINVALIDMILSMESSAGEOFLESSTHAN80CHARACTERS";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "Invalid input detected: THISRIGHTHEREISANINVALIDMILSMESSAGEOFLESSTHAN80CHARACTERS\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatValidMilsMessageAndOutputFile() {
    message = "Y2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G";
    output = new File("/home/matt/Desktop/outputFile");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    List<String> lines;

    try {
      lines = Files.readAllLines(output.toPath());
      for (String line : lines) {
        actual += line + "\n";
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    expected = "Y2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G\n" +
      "Type: Y2K\nInventory: CN?*,O\nFlag: CCG\n" +
      "Location: W:E}%H>AEW2U*\nSerial: 61T\nDODAC: }9AA\nName: +E6\nCompany: 2#\n";
    expectedOut="Successfully parsed Y2K&G7}YY[W=C)RY=DS8$TDDCN?*,ONOQ]#HJ{+B[CCG02.W:E}%H>AEW2U*61TDBB%}9AAD|+E6S2#G\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expected, actual);
    assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  void paramsAreFormatInvalidMilsMessageAndOutputFile() {
    message = "ThisIsAnInvalidMilsMessage";
    fParser = new MilsFileParser(file, message);
    output = new File("/home/matt/Desktop/outputFile");
    fParser.parse();
    List<String> lines;

    try {
      lines = Files.readAllLines(output.toPath());
      for (String line : lines) {
        actual += line;
      }
    } catch (Exception ignored) {
    }
    expectedErr = "Invalid input detected: ThisIsAnInvalidMilsMessage\n";
    assertTrue(actual.isEmpty());
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }

  @Test
  void paramsAreFormatAndValidFileWithSingleMilsMsg() {
    message = "/home/matt/Desktop/inputfiles/singleMilsMessage";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedOut = "Y2$UD)DQEIY</P)M&|@Z(WI\\$Q;XH-+#A]\\'L>N/1-UT.];E900[:.5TPX@^188I@AB *JV5C)W+1234\n" +
      "Type: Y2\nInventory: UD)DQE\nName: Y</\n" +
      "Company: P)M&|@Z\nDODAC: Q;XH-+#A]\\'L>N\nFlag: -UT.];E900[:.5T\nSerial: PX@^\nLocation: 188I@AB *JV5C)\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  void paramsAreFormatAndValidFileWithMilsMsgs() {
    message = "/home/matt/Desktop/inputfiles/multipleValidMilsMsgs";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedOut = "A?#[J3#L^9:2BOD.]N.PN(.>X0M<H@_@B8A#(2T3GXD8N)C=GE,?S, +U<;OJCFSWM>J$N 3>_LGW=KX\nType: A\nLocation: #[\n" +
      "Company: :2BOD.\nName: ]N.PN(\nFlag: N(.>X0M<H@_@B8A#(2T\nInventory: 3GXD8N)C=GE\nDODAC: ,?S, +U<;OJ\nSerial: N 3>_LGW=KX\n" +
      "DS#P1SH5O&U:|:I,&W'<_,)}85C-_9X:SLBYB1E06FVMR<T :|V%R &0 &'8)B[CY.,PUF<S+'0*IL5K\nType: D\nDODAC: DS\nCompany: #P\n" +
        "Inventory: 1S\nSerial: H5\nFlag: O&\nLocation: U:\nName: |:I,&W\n" +
      "C3F;6PJ?]\\XHEV0;-(S#Q8S7V'Z@Y\\QNZ'\\I ]:C%1L1S615XIQ@+TC{|:K6IKG636''C;OV*B*O?^X8\nType: C\nFlag: 3F\nCompany: ;6PJ?]\n" +
      "DODAC: \\XHEV0;\nLocation: (S#Q8S7V'Z@Y\\QNZ'\\I ]:\nName: C%1L1S615XIQ@+TC{|\nInventory: 6IKG636'\n" +
        "Serial: 'C;OV*B*O?^\n";
    assertTrue(actualErr.toString().isEmpty());
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void paramsAreFormatAndValidFileWithSingleInvalidMilsMsg() {
    message = "/home/matt/Desktop/inputfiles/validInputWithSingleInvalid";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedOut = "A1A|YW&@|ZA.Y]?* %?R%)3G/MP86ZO.>NJR5S3<0I-?H+W\\&|\\/3'XF^XAQRFLKABV$II@ZR9O@BM1.\n" +
      "Type: A\nLocation: A|\nCompany: A.Y]?*\nName:  %?R%)\nFlag: %)3G/MP86ZO.>NJR5S3\nInventory: <0I-?H+W\\&|\n" +
      "DODAC: \\/3'XF^XAQR\nSerial: I@ZR9O@BM1.\n" +
      "ZZZFPS:[.2H%*+/F66$XW9QBA6P'{$0L?(H(15KY8/^3)V*?0ZP|LA]\\&L$2%Q?D6;G@?G.V^/Y2@:*C\nType: ZZZ\nThis: ZZ\nIs: ZF\n" +
      "A: PS\nCompletely: :[\nRandom: .2\nValue: H%\nList: *+/F66\n";
    expectedErr = "Invalid input detected: NOTAVALIDMILSMESSAGE\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndValidFileWithSingleMsgNoDef() {
    message = "/home/matt/Desktop/inputfiles/validInputWithSingleNoDef";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedOut = "A1A|YW&@|ZA.Y]?* %?R%)3G/MP86ZO.>NJR5S3<0I-?H+W\\&|\\/3'XF^XAQRFLKABV$II@ZR9O@BM1.\n" +
      "Type: A\nLocation: A|\nCompany: A.Y]?*\nName:  %?R%)\nFlag: %)3G/MP86ZO.>NJR5S3\nInventory: <0I-?H+W\\&|\n" +
      "DODAC: \\/3'XF^XAQR\nSerial: I@ZR9O@BM1.\n" +
      "ZZZFPS:[.2H%*+/F66$XW9QBA6P'{$0L?(H(15KY8/^3)V*?0ZP|LA]\\&L$2%Q?D6;G@?G.V^/Y2@:*C\nType: ZZZ\nThis: ZZ\nIs: ZF\n" +
      "A: PS\nCompletely: :[\nRandom: .2\nValue: H%\nList: *+/F66\n";
    expectedErr = "No matching definition for the provided message.\n" +
      "KEQSTTTDQ4/,7H{/[/AA87$7U2?L>*8KL70B_$:P#L?(+47E}R*6R;0K36}*><;J]M24B;CIV/I W?>+\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndValidFileWithValidMilsMsgsAndSingleNoDef() {
    message = "/home/matt/Desktop/inputfiles/singleNoDef";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "No matching definition for the provided message.\n" +
      ":FO3>SPHOK%=M2B6AQ{C>U?2(_:NYJ} G7>;[N#2KLEZ1GS8) '|'V2JKG],-8CHBX4*\\:22A0R<)D@[\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndValidFileWithoutMilsMsgs() {
    message = "/home/matt/Desktop/inputfiles/validFileWithoutMilsMsgs";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "Invalid input detected: THISISANINVALIDFILE And it contains information that is useless\n" +
      "Invalid input detected: to\n" +
      "Invalid input detected: the \n" +
      "Invalid input detected: MILS\n" +
      "Invalid input detected: message\n" +
      "Invalid input detected: Scanner\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndInvalidFile() {
    message = "/home/matt/Desktop/inputfiles/invalidFile";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "Invalid input detected: /home/matt/Desktop/inputfiles/invalidFile\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatAndEmptyFile() {
    message = "/home/matt/Desktop/inputfiles/emptyFile";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    assertTrue(actualOut.toString().isEmpty());
    assertTrue(actualErr.toString().isEmpty());
  }
  @Test
  void paramsAreFormatValidFileWithMilsMsgsAndOutputFile() {
    message = "/home/matt/Desktop/inputfiles/multipleValidMilsMsgs";
    output = new File("/home/matt/Desktop/outputFile");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    List<String> lines;

    try {
      lines = Files.readAllLines(output.toPath());
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
  @Test
  void paramsAreFormatValidFileWithSingleInvalidMilsMsgAndOutputFile() {
    message = "/home/matt/Desktop/inputfiles/validInputWithSingleInvalid";
    output = new File("/home/matt/Desktop/outputFile");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    List<String> lines;

    try {
      lines = Files.readAllLines(output.toPath());
      for (String line : lines) {
        actual += line + "\n";
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    expected = "A1A|YW&@|ZA.Y]?* %?R%)3G/MP86ZO.>NJR5S3<0I-?H+W\\&|\\/3'XF^XAQRFLKABV$II@ZR9O@BM1.\n" +
      "Type: A\nLocation: A|\nCompany: A.Y]?*\nName:  %?R%)\nFlag: %)3G/MP86ZO.>NJR5S3\nInventory: <0I-?H+W\\&|\n" +
      "DODAC: \\/3'XF^XAQR\nSerial: I@ZR9O@BM1.\n" +
      "ZZZFPS:[.2H%*+/F66$XW9QBA6P'{$0L?(H(15KY8/^3)V*?0ZP|LA]\\&L$2%Q?D6;G@?G.V^/Y2@:*C\nType: ZZZ\nThis: ZZ\nIs: ZF\n" +
      "A: PS\nCompletely: :[\nRandom: .2\nValue: H%\nList: *+/F66\n";
    expectedErr = "Invalid input detected: NOTAVALIDMILSMESSAGE\n";
    expectedOut = "Successfully parsed A1A|YW&@|ZA.Y]?* %?R%)3G/MP86ZO.>NJR5S3<0I-?H+W\\&|\\/3'XF^XAQRFLKABV$II@ZR9O@BM1.\n" +
      "Successfully parsed ZZZFPS:[.2H%*+/F66$XW9QBA6P'{$0L?(H(15KY8/^3)V*?0ZP|LA]\\&L$2%Q?D6;G@?G.V^/Y2@:*C\n" +
      "\nOperation Completed with 1 errors and 2 messages successfully parsed to file.\nOutput Location: " +
      "/home/matt/Desktop/outputFile\nBased on format rules from: /format\n";
    assertEquals(expected, actual);
    assertEquals(expectedErr, actualErr.toString());
    assertEquals(expectedOut, actualOut.toString());
  }
  @Test
  void paramsAreFormatValidFileWithoutMilsMsgsAndOutputFile() {
    message = "/home/matt/Desktop/inputfiles/validFileWithoutMilsMsgs";
    output = new File("/home/matt/Desktop/outputFile");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    List<String> lines;

    try {
      lines = Files.readAllLines(output.toPath());
      for (String line : lines) {
        actual += line + "\n";
      }
    } catch (Exception ignored) {}

    expectedErr = "Invalid input detected: THISISANINVALIDFILE And it contains information that is useless\n" +
      "Invalid input detected: to\n" +
      "Invalid input detected: the \n" +
      "Invalid input detected: MILS\n" +
      "Invalid input detected: message\n" +
      "Invalid input detected: Scanner\n";
    expectedOut = "\nOperation Completed with 6 errors and 0 messages successfully parsed to file.\nOutput Location: " +
      "/home/matt/Desktop/outputFile\nBased on format rules from: /format\n";
    assertTrue(actual.isEmpty());
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void paramsAreFormatInvalidFileAndOutputFile() {
    message = "/home/matt/Desktop/inputfiles/invalidFile";
    output = new File("/home/matt/Desktop/outputFile");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    expectedErr = "Invalid input detected: /home/matt/Desktop/inputfiles/invalidFile\n";
    assertFalse(output.isFile());
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void protectedInputFile() {
    message = "/home/matt/Desktop/inputfiles/protectedFile";
    fParser = new MilsFileParser(file, message);
    fParser.parse();
    expectedErr = "Cannot read input file (java.nio.file.AccessDeniedException: " +
    "/home/matt/Desktop/inputfiles/protectedFile)\n";
    assertTrue(actualOut.toString().isEmpty());
    assertEquals(expectedErr, actualErr.toString());
  }
  @Test
  void protectedOutputFile() {
    message = "/home/matt/Desktop/inputfiles/multipleValidMilsMsgs";
    output = new File("");
    fParser = new MilsFileParser(file, message, output);
    fParser.parse();
    expectedErr = "Error writing to output file.\nError writing to output file.\n" +
    "Error writing to output file.\n";
    expectedOut = "Successfully parsed A?#[J3#L^9:2BOD.]N.PN(.>X0M<H@_@B8A#(2T3GXD8N)C=GE,?S, +U<;OJCFSWM>J$N 3>_LGW=KX\n" +
      "Successfully parsed DS#P1SH5O&U:|:I,&W'<_,)}85C-_9X:SLBYB1E06FVMR<T :|V%R &0 &'8)B[CY.,PUF<S+'0*IL5K\n" +
      "Successfully parsed C3F;6PJ?]\\XHEV0;-(S#Q8S7V'Z@Y\\QNZ'\\I ]:C%1L1S615XIQ@+TC{|:K6IKG636''C;OV*B*O?^X8\n" +
      "\nOperation Completed with 3 errors and 0 messages successfully parsed to file.\nOutput Location: " +
      "\nBased on format rules from: /format\n";
    assertEquals(expectedOut, actualOut.toString());
    assertEquals(expectedErr, actualErr.toString());
  }
}