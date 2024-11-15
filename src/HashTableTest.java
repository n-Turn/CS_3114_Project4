import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;
import java.lang.Object;


public class HashTableTest extends TestCase {

    private HashTable hashTable;

    /**
     * Sets up the test fixtures before each test method
     */
    public void setUp() {
        hashTable = new HashTable(4); // Start with size 4
    }


    /**
     * Tests the initial state of the hash table
     */
    public void testInitialization() {
        assertEquals(4, hashTable.getSize());
        assertEquals(0, hashTable.getNumberOfRecords());
        assertNotNull(hashTable.getRecords());
        assertEquals(4, hashTable.getRecords().length);
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

        hashTable.insert(1, handle1);
        hashTable.insert(2, handle2);
        hashTable.insert(3, handle3); // This should trigger expansion

        assertEquals(8, hashTable.getSize()); // Size should double
        assertEquals(3, hashTable.getNumberOfRecords());

        // Verify all records are still accessible
        assertNotNull(hashTable.search(1));
        assertNotNull(hashTable.search(2));
        assertNotNull(hashTable.search(3));
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
        
        //Normal delete test
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

}
