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
        freeBlockList.add(new Block(0, memSize));
    }


    /**
     * Inserts the serialized data into the memory
     * 
     * @param serializedData
     *            is the serialized data we are inserting into the hash
     * @param size
     *            is the length of the serialized data
     * @return the handle that is inserted
     */
    public Handle insert(byte[] serializedData, int size) {
        // goes through the freeblock list to get the index to insert at
        for (int listIndex = 0; listIndex < freeBlockList.size(); listIndex++) {
            Block currentFreeBlock = freeBlockList.get(listIndex);
            // if there is space for the data add it
            if (currentFreeBlock.getLength() >= size) {
                int position = currentFreeBlock.getPosition();
                // if the space is exact, then remove the freeblock
                if (currentFreeBlock.getLength() == size) {
                    freeBlockList.remove(listIndex);
                }
                // otherwise edit the position and the length
                else {
                    currentFreeBlock.setPosition(currentFreeBlock.getPosition()
                        + size);
                    currentFreeBlock.setLength(currentFreeBlock.getLength()
                        - size);
                }
                // put the data in the memory pool
                System.arraycopy(serializedData, 0, memoryPool, position, size);
                return new Handle(position, size);
            }
        }
        // if there is no space for it, expand the memory pool
        expand();
        System.out.println("Memory pool expanded to " + memoryPool.length
            + " bytes");
        return insert(serializedData, size);
    }


    /**
     * Expands the memory size if no spot found is big enough
     */
    private void expand() {
        // create a larger memory pool and copy the old data to the new one
        byte[] largerMemoryPool = new byte[memoryPool.length + initialMemSize];
        System.arraycopy(memoryPool, 0, largerMemoryPool, 0, memoryPool.length);

        // create a new block for the new memory space
        Block newBlock = new Block(memoryPool.length, initialMemSize);

        // if the list is empty then just add it in
        if (freeBlockList.isEmpty()) {
            freeBlockList.add(newBlock);
        }
        // otherwise check if it can be merged with the last block
        else {
            Block lastBlock = freeBlockList.get(freeBlockList.size() - 1);
            lastBlock.setLength(lastBlock.getLength() + newBlock.getLength());
        }
        memoryPool = largerMemoryPool;
    }


    /**
     * Removes a serialized record from the memory
     * 
     * @param handle
     *            is the handle that is removed
     */
    public void remove(Handle handle) {
        //get the position
        int startPosition = handle.getPosition();

        // new block of free memory
        Block freedBlock = new Block(startPosition, handle.getLength());

        // find the place to put the block
        int insertIndex;
        for (insertIndex = 0; insertIndex < freeBlockList
            .size(); insertIndex++) {
            if (freeBlockList.get(insertIndex).getPosition() > startPosition) {
                break;
            }
        }
        // put the freed block into the freeBlockList in the spot
        freeBlockList.add(insertIndex, freedBlock);

        // Merge with neighboring blocks if necessary
        mergePrevAndNextBlocks(insertIndex);
    }


    /**
     * Merges the block at the specified index with adjacent blocks if possible.
     *
     * @param index
     *            the index of the newly added block in freeBlockList
     */
    private void mergePrevAndNextBlocks(int index) {
        Block currentBlock = freeBlockList.get(index);

        // if not the first block
        if (index > 0) {
            // merge with the previous block if they are adjacent
            Block prevBlock = freeBlockList.get(index - 1);
            if (prevBlock.getPosition() + prevBlock.getLength() == currentBlock
                .getPosition()) {
                prevBlock.setLength(prevBlock.getLength() + currentBlock
                    .getLength());
                // remove the merged block
                freeBlockList.remove(index);
                // point to the merged block
                index--;
                // update currentBlock reference for next if statement
                currentBlock = prevBlock;
            }
        }

        // if not the last block
        if (index < freeBlockList.size() - 1) {
            // merge with the next block if they are adjacent
            Block nextBlock = freeBlockList.get(index + 1);
            if (currentBlock.getPosition() + currentBlock
                .getLength() == nextBlock.getPosition()) {
                currentBlock.setLength(currentBlock.getLength() + nextBlock
                    .getLength());
                // remove the merged block
                freeBlockList.remove(index + 1);
            }
        }
    }


    /**
     * Get a specific record when searching for it
     * 
     * @param space
     *            The space a record takes up
     * @param handle
     *            The handle of that record
     * @param size
     *            The length of the record
     * @return the number of copied bytes is returned
     */
    public int get(byte[] space, Handle handle, int size) {
        System.arraycopy(memoryPool, handle.getPosition(), space, 0, size);
        return size;
    }


    /**
     * Dumps a printout of the freeblock list
     */
    public void dump() {
        System.out.println("Freeblock List:");
        if (freeBlockList.isEmpty()) {
            System.out.println("There are no freeblocks in the memory pool");
        }
        else {
            for (int i = 0; i < freeBlockList.size(); i++) {
                Block block = freeBlockList.get(i);
                System.out.print("(" + block.getPosition() + "," + block
                    .getLength() + ")");
                if (i != freeBlockList.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }
}
