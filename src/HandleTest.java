import static org.junit.Assert.*;
import org.junit.Test;
import student.TestCase;

/**
 * This class tests the Handle class
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class HandleTest extends TestCase {

    private Handle normalHandle; // Normal case handle
    private Handle zeroHandle; // Zero values handle
    private Handle maxHandle; // Max values handle
    private Handle negHandle; // Negative values handle
    private Handle testHandle; // Handle for modification tests

    /**
     * Sets up the test environment before each test method
     */
    public void setUp() {
        normalHandle = new Handle(100, 50); // Normal case
        zeroHandle = new Handle(0, 0); // Zero values
        maxHandle = new Handle(Integer.MAX_VALUE, Integer.MAX_VALUE); // Max
                                                                      // values
        negHandle = new Handle(-1, -1); // Negative values
        testHandle = new Handle(200, 75); // For modification tests
    }


    /**
     * Tests the Handle constructor and initial state
     */
    public void testHandle() {
        // Test normal case
        assertEquals(100, normalHandle.getPosition());
        assertEquals(50, normalHandle.getLength());

        // Test zero values
        assertEquals(0, zeroHandle.getPosition());
        assertEquals(0, zeroHandle.getLength());

        // Test large values
        assertEquals(Integer.MAX_VALUE, maxHandle.getPosition());
        assertEquals(Integer.MAX_VALUE, maxHandle.getLength());

        // Test negative values
        assertEquals(-1, negHandle.getPosition());
        assertEquals(-1, negHandle.getLength());
    }

    /**
     * Tests the setPosition method
     */
    public void testSetPosition() {
        // Test setting to a new positive value
        testHandle.setPosition(300);
        assertEquals(300, testHandle.getPosition());

        // Test setting to zero
        testHandle.setPosition(0);
        assertEquals(0, testHandle.getPosition());

        // Test setting to maximum value
        testHandle.setPosition(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, testHandle.getPosition());

        // Test setting to negative value
        testHandle.setPosition(-1);
        assertEquals(-1, testHandle.getPosition());

        // Verify length remains unchanged after position changes
        assertEquals(75, testHandle.getLength());
    }


    /**
     * Tests multiple operations on the same handle
     */
    public void testCombinedOperations() {
        // Initial state
        assertEquals(200, testHandle.getPosition());
        assertEquals(75, testHandle.getLength());

        // Change position multiple times
        testHandle.setPosition(400);
        assertEquals(400, testHandle.getPosition());
        assertEquals(75, testHandle.getLength());

        testHandle.setPosition(500);
        assertEquals(500, testHandle.getPosition());
        assertEquals(75, testHandle.getLength());
    }
}
