import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParseMilsTest {
    
    private String msg;
    private String  expected;
    private String actual;
    private String def;
    private String[] args = new String[2];
    private String file = "/home/matt/IdeaProjects/MilsMessageScanner/format";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        args[0] = null;
        args[1] = null;
    }

    //scan Tests
    @org.junit.jupiter.api.Test
    void testMostSpecific() {
        System.out.println("Testing Most Specific Format Rules (1 of 8)...");
        msg = "A0A45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "A0A, (3, 9, Name), (10, 12, Flag), (18, 20, Serial), (24, 26, Inventory), (32, 50, DODAC), (52, 66, Location), (72, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (2 of 8)...");
        msg = "B0B45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "B0B, (4, 7, Name), (8, 12, Flag), (13, 18, Serial), (29, 32, Inventory), (33, 51, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (3 of 8)...");
        msg = "CIC45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "CIC, (4, 7, Name), (8, 12, Flag), (13, 18, Serial), (29, 32, Inventory), (33, 51, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (4 of 8)...");
        msg = "CIX45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "CIX, (1, 2, Name), (3, 4, Flag), (5, 6, Serial), (7, 8, Inventory), (9, 10, DODAC), (11, 12, Location), (13, 18, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (5 of 8)...");
        msg = "C4X45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "C4X, (3, 5, Name), (6, 6, Flag), (8, 20, Serial), (21, 32, Inventory), (33, 57, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (6 of 8)...");
        msg = "D9D45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "D9D, (7, 9, Name), (11, 16, Flag), (18, 20, Serial), (21, 39, Inventory), (40, 50, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (7 of 8)...");
        msg = "Y2K45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "Y2K, (4, 7, Name), (8, 12, Flag), (13, 18, Serial), (29, 32, Inventory), (33, 51, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Most Specific Format Rules (8 of 8)...");
        msg = "ZZZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "ZZZ, (1, 2, Name), (3, 4, Flag), (5, 6, Serial), (7, 8, Inventory), (9, 10, DODAC), (11, 12, Location), (13, 18, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void testLessSpecific() {
        System.out.println("Testing Less Specific Format Rules (1 of 4)...");
        msg = "A0Z45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "A0, (8, 9, Name), (10, 15, Flag), (26, 28, Serial), (32, 36, Inventory), (47, 57, DODAC), (58, 73, Location), (76, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Less Specific Format Rules (2 of 4)...");
        msg = "C4I45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "C4, (7, 9, Name), (11, 16, Flag), (18, 20, Serial), (21, 39, Inventory), (40, 50, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Less Specific Format Rules (3 of 4)...");
        msg = "D1745678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "D1, (4, 7, Name), (8, 12, Flag), (13, 18, Serial), (29, 32, Inventory), (33, 51, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Less Specific Format Rules (4 of 4)...");
        msg = "Y2P45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "Y2, (7, 9, Name), (11, 16, Flag), (18, 20, Serial), (21, 39, Inventory), (40, 50, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);
    }

    @Test
    void testLeastSpecific() {
        System.out.println("Testing Least Specific Format Rules (1 of 5)...");
        msg = "ABC45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "A, (3, 4, Name), (11, 16, Flag), (17, 22, Serial), (21, 39, Inventory), (40, 50, DODAC), (51, 61, Location), (70, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Least Specific Format Rules (2 of 5)...");
        msg = "BBB45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "B, (3, 5, Name), (6, 6, Flag), (8, 20, Serial), (21, 32, Inventory), (33, 57, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Least Specific Format Rules (3 of 5)...");
        msg = "CI445678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "C, (3, 5, Name), (6, 6, Flag), (8, 20, Serial), (21, 32, Inventory), (33, 57, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Least Specific Format Rules (4 of 5)...");
        msg = "D0Y45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "D, (1, 2, Name), (3, 4, Flag), (5, 6, Serial), (7, 8, Inventory), (9, 10, DODAC), (11, 12, Location), (13, 18, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Least Specific Format Rules (5 of 5)...");
        msg = "Y3K45678901234567890123456789012345678901234567890123456789012345678901234567890";
        expected = "Y, (3, 5, Name), (6, 6, Flag), (8, 20, Serial), (21, 32, Inventory), (33, 57, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);
    }

    @Test
    void testNoMatch() {
        expected = "No matching definition for the provided message.";

        System.out.println("Testing Message w/ No Associated Definition (1 of 5)...");
        msg = "EFG45678901234567890123456789012345678901234567890123456789012345678901234567890";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Message w/ No Associated Definition (2 of 5)...");
        msg = "GHI45678901234567890123456789012345678901234567890123456789012345678901234567890";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Message w/ No Associated Definition (3 of 5)...");
        msg = "ZYK45678901234567890123456789012345678901234567890123456789012345678901234567890";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Message w/ No Associated Definition (4 of 5)...");
        msg = "???45678901234567890123456789012345678901234567890123456789012345678901234567890";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);

        System.out.println("Testing Message w/ No Associated Definition (5 of 5)...");
        msg = "XYZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        actual = ParseMils.scan(file, msg);
        assertEquals(expected, actual);
    }

    //isValid Tests
    @org.junit.jupiter.api.Test
    void testIsValidCharNum() {
        args[0] = file;

        System.out.println("Testing Message w/ Too Few Characters (1 of 1)...");
        args[1] = "TOO-FEW-CHARS";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Message w/ Too Many Characters (1 of 1)...");
        args[1] = "TOO-MANY-CHARS-45678901234567890123456789012345678901234567890123456789012345678901234567890";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Message w/ Correct Number of Characters (1 of 1)...");
        args[1] = "JUST-RIGHT-234567890123456789012345678901234567890123456789012345678901234567890";
        assertTrue(ParseMils.isValid(args));
    }

    @org.junit.jupiter.api.Test
    void testisValidParamNum() {

        System.out.println("Testing Incorrect Parameters - Null Message (1 of 7)...");
        args[0] = file;
        msg = null;
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Incorrect Parameters - Blank Message (2 of 7)...");
        args[1] = "";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Incorrect Parameters - Null File (3 of 7)...");
        args[0] = null;
        args[1] = "XYZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Incorrect Parameters - Blank File (4 of 7)...");
        args[0] = "";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Incorrect Parameters - Too Few (5 of 7)...");
        String[] args1 = new String[1];
        args1[0] = file;
        assertFalse(ParseMils.isValid(args1));

        System.out.println("Testing Incorrect Parameters - Too Many (6 of 7)...");
        String[] args2 = new String[]{file, msg, "Name", ""};
        assertFalse(ParseMils.isValid(args2));

        System.out.println("Testing Correct Parameters (7 of 7)...");
        args[0] = file;
        args[1] = "XYZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        assertTrue(ParseMils.isValid(args));
    }

    @org.junit.jupiter.api.Test
    void testisValidFilePath() {
        System.out.println("Testing Incorrect Path (1 of 2)...");
        args[0] = "/home/incorrect/path";
        args[1] = "XYZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        assertFalse(ParseMils.isValid(args));

        System.out.println("Testing Correct Path (2 of 2)...");
        args[0] = file;
        args[1] = "XYZ45678901234567890123456789012345678901234567890123456789012345678901234567890";
        assertTrue(ParseMils.isValid(args));
    }

    //Parse Tests
    @org.junit.jupiter.api.Test
    void testParseToken() {
        System.out.println("Testing Parse for Name (1 of 7)...");
        msg = "Y2TMZO8P270HRL5V30WCG51JCHK4E5MFPCVLETB3WZ8K0K8FGKBHB89LH3YCQZONG40ANSKP4GI9DNH6";
        expected = "8P2";
        def = "Y2, (7, 9, Name), (11, 16, Flag), (18, 20, Serial), (21, 39, Inventory), (40, 50, DODAC), (58, 67, Location), (68, 80, Company)";
        actual = ParseMils.parse(msg, def).get("Name");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for Flag (2 of 7)...");
        expected = "0HRL5V";
        actual = ParseMils.parse(msg, def).get("Flag");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for Serial (3 of 7)...");
        expected = "0WC";
        actual = ParseMils.parse(msg, def).get("Serial");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for Inventory (4 of 7)...");
        expected = "G51JCHK4E5MFPCVLETB";
        actual = ParseMils.parse(msg, def).get("Inventory");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for DODAC (5 of 7)...");
        expected = "3WZ8K0K8FGK";
        actual = ParseMils.parse(msg, def).get("DODAC");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for Location (6 of 7)...");
        expected = "3YCQZONG40";
        actual = ParseMils.parse(msg, def).get("Location");
        assertEquals(expected, actual);

        System.out.println("Testing Parse for Company (7 of 7)...");
        expected = "ANSKP4GI9DNH6";
        actual = ParseMils.parse(msg, def).get("Company");
        assertEquals(expected, actual);

    }
}