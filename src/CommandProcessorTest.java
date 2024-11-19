import student.TestCase;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

/**
 * Test class for CommandProcessor
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.14.24
 */
public class CommandProcessorTest extends TestCase {
    private CommandProcessor processor;

    /**
     * Sets up the tests that follow
     */
    public void setUp() {
        // Default setup with reasonable memory and hash table sizes
        String input = ""; // Empty input initially
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);
    }


    /**
     * Tests basic insert command
     */
    public void testBasicInsert() {
        String input = "insert 1\n" + "Test Seminar\n"
            + "0610051600 90 10 10 45\n" + "HCI Computer_Science VT\n"
            + "This is a test seminar\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);

        // Clear any previous output
        systemOut().clearHistory();

        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Successfully inserted record with ID 1"));
        assertTrue(output.contains("Test Seminar"));
        assertTrue(output.contains("This is a test seminar"));
    }


    /**
     * Tests duplicate insert
     */
    public void testDuplicateInsert() {
        String input = "insert 1\n" + "First Seminar\n"
            + "0610051600 90 10 10 45\n" + "HCI Computer_Science VT\n"
            + "First seminar\n" + "insert 1\n" + // Duplicate ID
            "Second Seminar\n" + "0610051600 90 10 10 45\n"
            + "HCI Computer_Science VT\n" + "Second seminar\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Successfully inserted record with ID 1"));
        assertTrue(output.contains("Insert FAILED"));
        assertTrue(output.contains("already a record with ID 1"));
    }


    /**
     * Tests search command
     */
    public void testSearch() {
        String input = "insert 1\n" + "Test Seminar\n"
            + "0610051600 90 10 10 45\n" + "HCI Computer_Science VT\n"
            + "This is a test seminar\n" + "search 1\n" + // Should find this
            "search 2\n"; // Should not find this

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Found record with ID 1"));
        assertTrue(output.contains("Search FAILED"));
        assertTrue(output.contains("no record with ID 2"));
    }


    /**
     * Tests delete command
     */
    public void testDelete() {
        String input = "insert 1\n" + "Test Seminar\n"
            + "0610051600 90 10 10 45\n" + "HCI Computer_Science VT\n"
            + "This is a test seminar\n" + "delete 1\n" + // Should succeed
            "delete 1\n" + // Should fail (already deleted)
            "delete 2\n"; // Should fail (never existed)

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("successfully deleted"));
        assertTrue(output.contains("Delete FAILED"));
    }


    /**
     * Tests print commands
     */
    public void testPrint() {
        String input = "insert 1\n" + "Test Seminar\n"
            + "0610051600 90 10 10 45\n" + "HCI Computer_Science VT\n"
            + "This is a test seminar\n" + "print hashtable\n"
            + "print blocks\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(200, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Hashtable:"));
        assertTrue(output.contains("Freeblock List:"));
    }


    /**
     * Tests memory expansion
     */
    public void testMemoryExpansion() {
        // Create input that will force memory expansion
        StringBuilder input = new StringBuilder();

        // First insert
        input.append("insert 1\n").append("Seminar One\n").append(
            "0610051600 90 10 10 45\n").append("HCI Computer_Science VT\n")
            .append("First seminar with long description to use memory\n");

        // Second insert that should force expansion
        input.append("insert 2\n").append("Seminar Two\n").append(
            "0610051600 90 10 10 45\n").append("HCI Computer_Science VT\n")
            .append("Second seminar with long description\n");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .toString().getBytes());
        Scanner scanner = new Scanner(inputStream);
        // Start with small memory size to force expansion
        processor = new CommandProcessor(50, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Memory pool expanded"));
        assertTrue(output.contains("Successfully inserted record with ID 1"));
        assertTrue(output.contains("Successfully inserted record with ID 2"));
    }


    /**
     * Tests invalid commands
     */
    public void testInvalidCommands() {
        String input = "invalid 1\n" + // Invalid command
            "print invalid\n" + // Invalid print type
            "insert invalid\n"; // Invalid insert format

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input
            .getBytes());
        Scanner scanner = new Scanner(inputStream);
        processor = new CommandProcessor(100, 4, scanner);

        systemOut().clearHistory();
        processor.processCommands();
        String output = systemOut().getHistory();

        // Should handle invalid commands gracefully
        assertFalse(output.contains("Exception"));
    }
}
