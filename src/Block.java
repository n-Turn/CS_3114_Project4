/**
 * Block class for the Memory Manager
 * Stores the position and length of a record in freeblock list
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class Block {
    private int position; // Starting position in memory pool
    private int length; // Length of the record in bytes

    /**
     * Creates a new handle
     * 
     * @param pos
     *            Starting position in memory pool
     * @param len
     *            Length of the record
     */
    public Block(int pos, int len) {
        this.position = pos;
        this.length = len;
    }


    /**
     * Gets the starting position in memory
     * 
     * @return Starting position
     */
    public int getPosition() {
        return position;
    }


    /**
     * Gets the length of the record
     * 
     * @return Record length
     */
    public int getLength() {
        return length;
    }


    /**
     * Sets a new position for this handle
     * 
     * @param newPos
     *            New memory position
     */
    public void setPosition(int newPos) {
        this.position = newPos;
    }


    /**
     * Sets a new length for this handle
     * 
     * @param newLen
     *            New memory position
     */
    public void setLength(int newLen) {
        this.length = newLen;
    }
}
