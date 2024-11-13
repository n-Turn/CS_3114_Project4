/**
 * This class is the MemoryManager and it keeps the seminars in serialized form
 * to help us find it later
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.13/2024
 */
public class MemoryManager {
    /**
     * Constructor for the class
     * 
     * @param memSize
     *            The size of the memory
     */
    public MemoryManager(int memSize) {
        // TODO Auto-generated constructor stub
    }


    /**
     * Inserts the serialized data into the memory
     * 
     * @param serializedData
     *            is the serialized data we are inserting into the hash
     * @param length
     *            is the length of the serialized data
     * @return the handle that is inserted
     */
    public Handle insert(byte[] serializedData, int length) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Removes a serialized record from the memory
     * 
     * @param handle
     *            is the handle that is removed
     */
    public void remove(Handle handle) {
        // TODO Auto-generated method stub

    }


    /**
     * Get a specific record when searching for it
     * 
     * @param space
     *            The space a record takes up
     * @param handle
     *            The handle of that record
     * @param length
     *            The length of the record
     */
    public void get(byte[] space, Handle handle, int length) {
        // TODO Auto-generated method stub

    }


    /**
     * Dumps a printout of the freeblock list
     */
    public void dump() {
        // TODO Auto-generated method stub

    }

}
