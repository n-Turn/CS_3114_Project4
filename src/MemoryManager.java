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
        for (int listIndex = 0; listIndex < freeBlockList.size(); listIndex++) {
            Block currentFreeBlock = freeBlockList.get(listIndex);
            if (currentFreeBlock.getLength() >= size) {
                if (currentFreeBlock.getLength() == size) {
                    freeBlockList.remove(listIndex);
                }
                else {
                    currentFreeBlock.setPosition(listIndex + size);
                    currentFreeBlock.setLength(currentFreeBlock.getLength()
                        - size);
                }
                int position = currentFreeBlock.getPosition();
                System.arraycopy(serializedData, 0, memoryPool, position, size);
                return new Handle(position, size);
            }
        }
        // If no suitable block found, expand memory and retry insertion
        expand();
        return insert(serializedData, size);
    }


    /**
     * Expands the memory size if no spot found is big enough
     */
    private void expand() {
        byte[] largerMemoryPool = new byte[memoryPool.length + initialMemSize];
        System.arraycopy(memoryPool, 0, largerMemoryPool, 0, memoryPool.length);

        // create a new block for the new memory pool space
        Block newBlock = new Block(memoryPool.length, initialMemSize);

        // check if it can be merged with the last block
        Block lastBlock = freeBlockList.get(freeBlockList.size() - 1);
        if (lastBlock.getPosition() + lastBlock.getLength() == newBlock
            .getPosition()) {
            // merge new block and last block if needed
            lastBlock.setLength(lastBlock.getLength() + newBlock.getLength());
        }
        else {
            // otherwise add as a new block to the end
            freeBlockList.add(newBlock);
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
        int startPosition = handle.getPosition();

        // new block of free memory
        Block freedBlock = new Block(startPosition, handle.getLength());

        // put the freed block into the freeBlockList in the correct position
        int insertIndex;
        for (insertIndex = 0; insertIndex < freeBlockList
            .size(); insertIndex++) {
            if (freeBlockList.get(insertIndex).getPosition() > startPosition) {
                break;
            }
        }
        freeBlockList.add(insertIndex, freedBlock);

        // Check for possible merges with neighboring blocks
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
     * @param length
     *            The length of the record
     * @return the number of copied bytes is returned
     */
    public int get(byte[] space, Handle handle, int size) {
        System.arraycopy(memoryPool, handle.getPosition(), space, 0, size);
        // TODO what to return, ask a TA
        return size;
    }


    /**
     * Dumps a printout of the freeblock list
     */
    public void dump() {
        System.out.println("Free Block List:");
        for (int i = 0; i < freeBlockList.size(); i++) {
            Block block = freeBlockList.get(i);
            System.out.print("(" + block.getPosition() + "," + block.getLength()
                + ")");
            if (i != freeBlockList.size() - 1) {
                System.out.print(" -> ");
            }
        }
    }
}
