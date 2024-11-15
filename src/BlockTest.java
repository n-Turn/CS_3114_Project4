import static org.junit.Assert.*;
import org.junit.Test;
import student.TestCase;

/**
 * This class tests the Block class
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class BlockTest extends TestCase {

    private Handle normalBlock; // Normal case block
    private Handle zeroBlock; // Zero values block
    private Handle maxBlock; // Max values block
    private Handle negBlock; // Negative values block
    private Handle testBlock; // Handle for modification tests

    /**
     * Sets up the test environment before each test method
     */
    public void setUp() {
        normalBlock = new Handle(100, 50); // Normal case
        zeroBlock = new Handle(0, 0); // Zero values
        maxBlock = new Handle(Integer.MAX_VALUE, Integer.MAX_VALUE); // Max
                                                                      // values
        negBlock = new Handle(-1, -1); // Negative values
        testBlock = new Handle(200, 75); // For modification tests
    }


    /**
     * Tests the Handle constructor and initial state
     */
    public void testBlock() {
        // Test normal case
        assertEquals(100, normalBlock.getPosition());
        assertEquals(50, normalBlock.getLength());

        // Test zero values
        assertEquals(0, zeroBlock.getPosition());
        assertEquals(0, zeroBlock.getLength());

        // Test large values
        assertEquals(Integer.MAX_VALUE, maxBlock.getPosition());
        assertEquals(Integer.MAX_VALUE, maxBlock.getLength());

        // Test negative values
        assertEquals(-1, negBlock.getPosition());
        assertEquals(-1, negBlock.getLength());
    }

    /**
     * Tests the setPosition method
     */
    public void testSetPosition() {
        // Test setting to a new positive value
        testBlock.setPosition(300);
        assertEquals(300, testBlock.getPosition());

        // Test setting to zero
        testBlock.setPosition(0);
        assertEquals(0, testBlock.getPosition());

        // Test setting to maximum value
        testBlock.setPosition(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, testBlock.getPosition());

        // Test setting to negative value
        testBlock.setPosition(-1);
        assertEquals(-1, testBlock.getPosition());

        // Verify length remains unchanged after position changes
        assertEquals(75, testBlock.getLength());
    }


    /**
     * Tests multiple operations on the same handle
     */
    public void testCombinedOperations() {
        // Initial state
        assertEquals(200, testBlock.getPosition());
        assertEquals(75, testBlock.getLength());

        // Change position multiple times
        testBlock.setPosition(400);
        assertEquals(400, testBlock.getPosition());
        assertEquals(75, testBlock.getLength());

        testBlock.setPosition(500);
        assertEquals(500, testBlock.getPosition());
        assertEquals(75, testBlock.getLength());
    }

}
