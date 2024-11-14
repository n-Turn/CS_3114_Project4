/**
 * This class is the MemoryManager and it keeps the seminars in serialized form
 * to help us find it later
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.13/2024
 */
public class MemoryManager {

    private byte[] memoryPool;
    private DoubleLL freeBlockList;
    private int initialMemSize;

    /**
     * Constructor for the class
     * 
     * @param memSize
     *            The size of the memory
     */
    public MemoryManager(int memSize) {
        initialMemSize = memSize;
        memoryPool = new byte[memSize];
        freeBlockList = new DoubleLL();
        // Initially, the entire memory is free
        freeBlockList.add(new Block(0, memSize));
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
    public Handle insert(byte[] serializedData, int size) {
        Boolean isInserted = false;
        for (int i = 0; i < freeBlockList.size(); i++) {
            if (freeBlockList.get(i).getLength() >= size) {
                if (freeBlockList.get(i).getLength() == size) {
                    freeBlockList.remove(i);
                }
                else {
                    freeBlockList.get(i).setPosition(i + size);
                }
                System.arraycopy(serializedData, 0, memoryPool, i, size);
                isInserted = true;
            }
        }
        if (!isInserted) {

        }
    }


    /**
     * Expands the memory size
     */
    private void expand() {
        byte[] largerMemoryPool = new byte[memoryPool.length + initialMemSize];
        System.arraycopy(memoryPool, 0, largerMemoryPool, 0, memoryPool.length);
        memoryPool = largerMemoryPool;

        // Create a new block for the newly added memory space
        Block newBlock = new Block(memoryPool.length - initialMemSize,
            initialMemSize);

        // Check if it can be merged with the last block in the free list
        Block lastBlock = freeBlockList.get(freeBlockList.size() - 1);
        if (lastBlock.getPosition() + lastBlock.getLength() == newBlock.getPosition()) {
            // Merge the new block with the last block in freeBlockList
            lastBlock.setLength(lastBlock.getLength() + newBlock.getLength());
        }
        else {
            // Add the new block as a separate free block
            freeBlockList.add(newBlock);
        }
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
    public void get(byte[] space, Handle handle, int size) {
        // TODO Auto-generated method stub

    }


    /**
     * Dumps a printout of the freeblock list
     */
    public void dump() {
        // TODO Auto-generated method stub

    }

}
