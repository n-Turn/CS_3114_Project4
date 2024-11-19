import student.TestCase;

/**
 * This class tests the DoubleLL class.
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.13.2024
 */
public class DoubleLLTest extends TestCase {

    private DoubleLL list;

    /**
     * Makes a new doublell for the tests to use
     */
    public void setUp() {
        list = new DoubleLL();
    }


    /**
     * Tests adding a Block to the list
     */
    public void testAddBlock() {
        Block block1 = new Block(0, 100);
        Block block2 = new Block(100, 50);
        list.add(block1);
        list.add(block2);

        assertEquals(2, list.size());
        assertEquals(block1, list.get(0));
        assertEquals(block2, list.get(1));
    }


    /**
     * Tests removing a Block by position
     */
    public void testRemoveBlock() {
        Block block1 = new Block(0, 100);
        Block block2 = new Block(100, 50);
        list.add(block1);
        list.add(block2);

        list.remove(0);
        assertEquals(1, list.size());
        assertEquals(block2, list.get(0));
    }


    /**
     * Tests removing a middle Block from the list
     */
    public void testRemoveMiddleBlock() {
        Block block1 = new Block(0, 100);
        Block block2 = new Block(100, 50);
        Block block3 = new Block(150, 25);
        list.add(block1);
        list.add(block2);
        list.add(block3);

        list.remove(1);
        assertEquals(2, list.size());
        assertEquals(block1, list.get(0));
        assertEquals(block3, list.get(1));
    }


    /**
     * Tests removing last block
     */
    public void testRemoveLastBlock() {
        Block block1 = new Block(0, 100);
        list.add(block1);
        assertEquals(1, list.size());
        assertEquals(block1, list.get(0));
        list.remove(0);
        assertEquals(0, list.size());
    }


    /**
     * Tests clearing the list
     */
    public void testClear() {
        list.add(new Block(0, 100));
        list.add(new Block(100, 50));
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }
}
