import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import student.TestCase;

/**
 * This class tests the Memory Manager class
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.13/2024
 */
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
        assertEquals(100, h2.getPosition());
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
        assertTrue(output.contains("(0,60)"));
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


    /**
     * Tests the insert and remove with merge
     */
    public void testInsertAndRemoveWithMerge() {
        MemoryManager manager = new MemoryManager(100);

        // Insert 3 blocks of data
        Handle handle1 = manager.insert(new byte[20], 20);
        Handle handle2 = manager.insert(new byte[30], 30);
        Handle handle3 = manager.insert(new byte[10], 10);

        // Remove middle block and check free block list
        manager.remove(handle2);

        // Remove adjacent blocks to cause merge
        manager.remove(handle1);
        manager.remove(handle3);

        // Verify the entire memory is a single block again
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.dump();

        String expectedOutput = "Freeblock List:\n(0,100)\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    /**
     * Tests edge cases with merge
     */
    public void testExactFitAndPartialAllocation() {
        MemoryManager manager = new MemoryManager(100);

        // Insert data with exact fit
        Handle handle1 = manager.insert(new byte[50], 50);

        // Insert smaller data to create remaining block
        Handle handle2 = manager.insert(new byte[30], 30);

        // Verify free block size
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.dump();

        String expectedOutput = "Freeblock List:\n(80,20)\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    /**
     * tests merging previous and next blocks
     */
    public void testMergePrevAndNextBlocks() {
        MemoryManager manager = new MemoryManager(100);

        // Insert 3 blocks of data
        Handle handle1 = manager.insert(new byte[20], 20);
        Handle handle2 = manager.insert(new byte[30], 30);
        Handle handle3 = manager.insert(new byte[40], 40);

        // Remove first and last blocks
        manager.remove(handle1);
        manager.remove(handle3);

        // Remove middle block to trigger merge with both neighbors
        manager.remove(handle2);

        // Verify memory is back to a single block
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.dump();

        String expectedOutput = "Freeblock List:\n(0,100)\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    /**
     * Tests expanding the memory
     */
    public void testExpandMemory() {
        MemoryManager manager = new MemoryManager(50);

        // Fill initial memory
        Handle handle1 = manager.insert(new byte[50], 50);

        // Trigger memory expansion
        Handle handle2 = manager.insert(new byte[30], 30);

        // Verify free block after expansion
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.dump();

        String expectedOutput = "Freeblock List:\n(80,20)\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    /**
     * Tests the get method again for edge cases
     */
    public void testGet2() {
        MemoryManager manager = new MemoryManager(50);

        // Insert data and retrieve it
        byte[] data = "TestData".getBytes();
        Handle handle = manager.insert(data, data.length);

        byte[] retrievedData = new byte[data.length];
        int bytesCopied = manager.get(retrievedData, handle, data.length);

        // Verify the retrieved data matches the inserted data
        assertEquals(data.length, bytesCopied);
    }


    /**
     * Tests dumping the free block again for edge cases
     */
    public void testDumpEmptyFreeBlockList() {
        MemoryManager manager = new MemoryManager(50);

        // Allocate entire memory
        manager.insert(new byte[50], 50);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        manager.dump();

        String expectedOutput =
            "Freeblock List:\nThere are no freeblocks in the memory pool\n";
        assertEquals(expectedOutput, outContent.toString());
    }

}
