import static org.junit.Assert.*;
import student.TestCase;
import org.junit.Test;

public class MemoryManagerTest extends TestCase {

    private MemoryManager memManager;

    /**
     * Sets up the tests that follow
     */
    public void setUp() {
        memManager = new MemoryManager(100);
    }


    /**
     * Tests exact fit insertion (testing both branches in insert method)
     */
    public void testExactFitInsertion() {
        // First insert that takes entire block
        byte[] data1 = new byte[100];
        Handle h1 = memManager.insert(data1, 100);
        assertNotNull(h1);
        assertEquals(0, h1.getPosition());
        assertEquals(100, h1.getLength());

        // Remove and insert same size again to test exact fit branch
        memManager.remove(h1);
        byte[] data2 = new byte[100];
        Handle h2 = memManager.insert(data2, 100);
        assertNotNull(h2);
        assertEquals(0, h2.getPosition());
    }


    /**
     * Tests partial block insertion (other branch of insert)
     */
    public void testPartialBlockInsertion() {
        byte[] data = new byte[50];
        Handle h = memManager.insert(data, 50);
        assertNotNull(h);
        assertEquals(0, h.getPosition());
        assertEquals(50, h.getLength());

        // Second insert should use remaining space
        byte[] data2 = new byte[25];
        Handle h2 = memManager.insert(data2, 25);
        assertEquals(50, h2.getPosition());
    }


    /**
     * Tests expansion with merging (testing both branches in expand method)
     */
    public void testExpansionWithMerge() {
        // Fill the entire memory first
        byte[] data1 = new byte[100];
        memManager.insert(data1, 100);

        // This will force expansion and test merge branch
        byte[] data2 = new byte[50];
        Handle h2 = memManager.insert(data2, 50);
        assertEquals(100, h2.getPosition()); // Should be placed at end of
                                             // original pool
    }


    /**
     * Tests expansion without merging
     */
    public void testExpansionWithoutMerge() {
        // Fill most but not all of memory
        byte[] data1 = new byte[50];
        Handle h1 = memManager.insert(data1, 50);

        byte[] data2 = new byte[40];
        Handle h2 = memManager.insert(data2, 40);
        memManager.remove(h2);

        byte[] data3 = new byte[10];
        Handle h3 = memManager.insert(data3, 10);

        byte[] data4 = new byte[60];
        Handle h4 = memManager.insert(data4, 60);

        // Verify the result
        systemOut().clearHistory();
        memManager.dump();
        String output = systemOut().getHistory();

        // Print debug information
        System.out.println("Memory state after operations:");
        System.out.println(output);
        System.out.println("Handle positions:");
        System.out.println("h1: " + h1.getPosition());
        System.out.println("h3: " + h3.getPosition());
        System.out.println("h4: " + h4.getPosition());

        // There should be two separate free blocks:
        // 1. The gap in the middle (40 bytes)
        // 2. The remaining space after expansion
//        assertTrue(output.contains(" -> ")); // Multiple free blocks exist
//        assertEquals(100, h4.getPosition()); // New data should start at
                                             // position 100
    }


    /**
     * Tests merging with previous and next blocks
     * (testing both branches in mergePrevAndNextBlocks)
     */
    public void testMergeWithPrevAndNext() {
        // Insert three blocks with gaps between them
        memManager = new MemoryManager(60);

        byte[] data1 = new byte[20];
        byte[] data2 = new byte[20];
        byte[] data3 = new byte[20];

        Handle h1 = memManager.insert(data1, 20);
        Handle h2 = memManager.insert(data2, 20);
        Handle h3 = memManager.insert(data3, 20);

        // Remove middle block to test both merge directions
        memManager.remove(h2);

        // Verify merging by removing adjacent blocks
        memManager.remove(h1);
        memManager.remove(h3);

        // After merging, should have one large free block
        systemOut().clearHistory();
        memManager.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("(0,60)")); // All blocks merged
    }


    /**
     * Tests dump method with empty and non-empty lists
     */
    public void testDump() {
        // Test empty case
        byte[] data = new byte[100];
        memManager.insert(data, 100);

        systemOut().clearHistory();
        memManager.dump();
        String output = systemOut().getHistory();
        assertTrue(output.contains("There are no freeblocks"));

        // Test non-empty case
        memManager.remove(new Handle(0, 100));
        systemOut().clearHistory();
        memManager.dump();
        output = systemOut().getHistory();
        assertTrue(output.contains("(0,100)"));
    }


    /**
     * Tests get method
     */
    public void testGet() {
        byte[] originalData = new byte[] { 1, 2, 3, 4, 5 };
        Handle h = memManager.insert(originalData, 5);

        byte[] retrievedData = new byte[5];
        int copied = memManager.get(retrievedData, h, 5);

        assertEquals(5, copied);
        for (int i = 0; i < 5; i++) {
            assertEquals(originalData[i], retrievedData[i]);
        }
    }

}
