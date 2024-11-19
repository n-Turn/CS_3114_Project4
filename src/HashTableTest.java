import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the methods of the HashTable class
 * 
 * 
 * @author Nicolas Turner & Nimay Goradia
 * @version 11/13/24
 */
public class HashTableTest extends TestCase {

    private HashTable hashTable;

    /**
     * Sets up the test fixtures before each test method
     */
    public void setUp() {
        hashTable = new HashTable(1); // Start with size 1
    }


    /**
     * Tests the initial state of the hash table
     */
    public void testInitialization() {
        assertEquals(1, hashTable.getSize());
        assertEquals(0, hashTable.getNumberOfRecords());
        assertNotNull(hashTable.getRecords());
        assertEquals(1, hashTable.getRecords().length);
    }


    /**
     * Tests the basic insert functionality
     */
    public void testBasicInsert() {
        Handle handle1 = new Handle(0, 10);
        hashTable.insert(1, handle1);

        assertEquals(1, hashTable.getNumberOfRecords());
        hashTable.print();
        assertEquals(1, hashTable.search(1)); // Should be in slot 1

        // Test inserting another record
        Handle handle2 = new Handle(10, 10);
        hashTable.insert(2, handle2);

        assertEquals(2, hashTable.getNumberOfRecords());
        assertEquals(2, hashTable.search(2)); // Should be in slot 2
    }


    /**
     * Tests the hash table expansion
     */
    public void testExpansion() {
        // Insert enough records to trigger expansion
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 10);
        Handle handle3 = new Handle(20, 10);
        Handle handle4 = new Handle(0, 0);
        Handle handle5 = new Handle(100, 100);
        Handle handle6 = new Handle(200, 200);
        Handle handle7 = new Handle(400, 400);

        hashTable.insert(1, handle1);
        hashTable.insert(2, handle2);
        hashTable.insert(3, handle3); // This should trigger expansion

        assertEquals(8, hashTable.getSize()); // Size should double
        assertEquals(3, hashTable.getNumberOfRecords());

        // Verify all records are still accessible
        assertNotNull(hashTable.search(1));
        assertNotNull(hashTable.search(2));
        assertNotNull(hashTable.search(3));

        hashTable.delete(1);
        hashTable.delete(2);
        hashTable.delete(3);

        hashTable.insert(1, handle1); // This
        hashTable.insert(2, handle2); // This
        hashTable.insert(3, handle3); // This
        hashTable.insert(4, handle4); // This
        hashTable.insert(5, handle5); // This
        hashTable.insert(6, handle6); // This
        hashTable.insert(7, handle7); // This

        assertEquals(16, hashTable.getSize()); // Size should double
        assertEquals(7, hashTable.getNumberOfRecords());

        // Verify all records are still accessible
        assertNotNull(hashTable.search(1));
        assertNotNull(hashTable.search(2));
        assertNotNull(hashTable.search(3));
        assertNotNull(hashTable.search(4));
        assertNotNull(hashTable.search(5));
        assertNotNull(hashTable.search(6));
        assertNotNull(hashTable.search(7));

    }


    /**
     * Tests the quadratic probing
     */
    public void testQuadraticProbing() {
        // Insert records that will collide
        Handle handle1 = new Handle(0, 10);
        Handle handle5 = new Handle(10, 10); // Will hash to same slot as 1

        hashTable.insert(1, handle1);
        hashTable.insert(5, handle5); // Should probe to next position

        assertEquals(2, hashTable.getNumberOfRecords());
        assertFalse(hashTable.search(5) == hashTable.search(1));
        assertTrue(hashTable.search(5) != hashTable.search(1));
    }


    /**
     * Tests the delete functionality
     */
    public void testDelete() {
        Handle handle1 = new Handle(0, 10);
        hashTable.insert(1, handle1);

        // Test deleting non-existent record
        assertFalse(hashTable.delete(99));

        // Normal delete test
        assertTrue(hashTable.delete(1));
        assertEquals(-1, hashTable.search(1));
        assertEquals(0, hashTable.getNumberOfRecords());

        assertFalse(hashTable.delete(99));

    }


    /**
     * Tests searching for non-existent records
     */
    public void testSearchNonExistent() {
        assertEquals(-1, hashTable.search(1));

        // Insert and delete a record, then search for it
        Handle handle1 = new Handle(0, 10);
        hashTable.insert(1, handle1);
        hashTable.delete(1);
        assertEquals(-1, hashTable.search(1));
    }


    /**
     * Tests handling of tombstones
     */
    public void testTombstones() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 10);

        hashTable.insert(1, handle1);
        hashTable.insert(2, handle2);
        hashTable.delete(1);
        hashTable.print();

        // Insert new record, should reuse tombstone slot
        Handle handle3 = new Handle(20, 10);
        hashTable.insert(3, handle3);

        assertTrue(hashTable.search(3) != -1);
        assertEquals(2, hashTable.getNumberOfRecords());
    }


    /**
     * Tests the print functionality
     */
    public void testPrint() {
        Handle handle1 = new Handle(0, 10);
        hashTable.insert(1, handle1);

        // Capture system output
        systemOut().clearHistory();
        hashTable.print();
        String output = systemOut().getHistory();

        assertTrue(output.contains("Hashtable:"));
        assertTrue(output.contains("1"));
        assertTrue(output.contains("total records: 1"));
    }


    /**
     * Tests the hash function
     */
    public void testHashFunction() {
        assertEquals(1, HashTable.h(5, 4)); // 5 mod 4 = 1
        assertEquals(0, HashTable.h(4, 4)); // 4 mod 4 = 0
        assertEquals(2, HashTable.h(6, 4)); // 6 mod 4 = 2
    }


    /**
     * Tests inserting records after delete
     */
    public void testInsertAfterDelete() {
        Handle handle1 = new Handle(0, 10);
        hashTable.insert(1, handle1);
        hashTable.delete(1);

        Handle handle2 = new Handle(10, 10);
        hashTable.insert(1, handle2);
        hashTable.print();
        assertEquals(1, hashTable.getNumberOfRecords());
        assertTrue(hashTable.search(1) != -1);
    }


    /**
     * Tests collisions during rehashing
     */
    public void testRehashCollisions() {
        // Create a hash table of size 4
        HashTable table = new HashTable(4);

        // Insert records that will definitely collide after rehashing
        // When table size is 8, these will all hash to the same initial
        // position
        Handle h1 = new Handle(0, 10);
        Handle h2 = new Handle(10, 10);
        Handle h3 = new Handle(20, 10);

        // These IDs are chosen to cause collisions:
        // When table size is 8, 8 % 8 = 0, 16 % 8 = 0, 24 % 8 = 0
        table.insert(8, h1);
        table.insert(16, h2); // This will trigger resize and cause collision
        table.insert(24, h3); // This will cause another collision

        // Verify all records were inserted correctly
        assertEquals(8, table.getSize()); // Table should have doubled
        assertTrue(table.search(8) != -1);
        assertTrue(table.search(16) != -1);
        assertTrue(table.search(24) != -1);

        // Verify the records are in different slots
        assertFalse(table.search(8) == table.search(16));
        assertFalse(table.search(16) == table.search(24));
        assertFalse(table.search(8) == table.search(24));
    }


    /**
     * Tests rehashing with no collisions
     */
    public void testRehashNoCollisions() {
        HashTable table = new HashTable(4);

        // Insert records that won't collide after rehashing
        Handle h1 = new Handle(0, 10);
        Handle h2 = new Handle(10, 10);
        Handle h3 = new Handle(20, 10);

        table.insert(1, h1); // Will hash to 1
        table.insert(2, h2); // Will hash to 2
        table.insert(3, h3); // Will trigger resize but won't cause collisions

        // Verify all records were inserted correctly
        assertEquals(8, table.getSize());
        assertEquals(1, table.search(1));
        assertEquals(2, table.search(2));
        assertEquals(3, table.search(3));
    }


    /**
     * Tests rehashing with tombstones
     */
    public void testRehashWithTombstones() {
        HashTable table = new HashTable(4);

        Handle h1 = new Handle(0, 10);
        Handle h2 = new Handle(10, 10);
        Handle h3 = new Handle(20, 10);

        table.insert(1, h1);
        table.insert(2, h2);
        table.delete(1); // Create a tombstone

        // This insert should trigger rehashing
        // Verify tombstones are not carried over
        table.insert(3, h3);

        assertEquals(4, table.getSize());
        assertEquals(2, table.getNumberOfRecords());
        assertEquals(-1, table.search(1)); // Should not find deleted record
    }
}
