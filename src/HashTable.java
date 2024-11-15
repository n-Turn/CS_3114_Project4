// On my honor:
// - I have not used source code obtained from another current or
// former student, or any other unauthorized source, either
// modified or unmodified.
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * Hash table class The key is a string (the artist or song name). The value is
 * probably some kind of Node object.
 *
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.14.24
 */
public class HashTable {

    /**
     * This class represents a Record, which stores a key and a reference to a
     * Node. Each Record object associates a key (String) with a Node in a data
     * structure.
     */
    class Record {
        private int key; // The key associated with this record
        private Handle handle; // The handle associated with this record

        /**
         * This constructor initializes a Record with a key and a handle.
         *
         * @param key
         *            the key associated with the record
         * @param handle
         *            the handle associated with the record
         */
        public Record(int key, Handle handle) {
            this.key = key;
            this.handle = handle;
        }


        /**
         * This method returns the key associated with the record.
         *
         * @return the key of the record
         */
        public int getKey() {
            return key;
        }


        /**
         * This method returns the handle associated with the record.
         *
         * @return the handle of the record
         */
        public Handle getHandle() {
            return handle;
        }
    }

    // Attributes
    private Record[] allRecords; // Array of records
    private int numberOfRecords; // Number of records currently stored
    private int size;
    private Handle tombstoneHandle = null;
    private final Record tombstone = new Record(-1, tombstoneHandle);

    /**
     * Constructor for the Hash class, which initializes the hash table with a
     * specified size.
     *
     * @param size
     *            the size of the hash table to be created
     */
    public HashTable(int size) {
        allRecords = new Record[size]; // Initialize with given size
        this.size = size;
        this.numberOfRecords = 0;
    }


    /**
     * Getter method for the size
     *
     * @return the size of the Hash
     */
    public int getSize() {
        return this.size;
    }


    /**
     * Getter method for the records
     *
     * @return all the records
     */
    public Record[] getRecords() {
        return allRecords;
    }


    /**
     * Getter method for the tombstone
     *
     * @return the tombstone record
     */
    public Record getTombstone() {
        return tombstone;
    }


    /**
     * Getter method for the number of records in the hash
     *
     * @return the number of records in the hash
     */
    public int getNumberOfRecords() {
        return numberOfRecords;
    }


    /**
     * Compute the hash function
     *
     * @param id
     *            The id that we are hashing
     * @param tableSize
     *            Length of the hash table (needed because this method is
     *            static)
     * @return The hash function value (the home slot in the table for this key)
     */
    public static int h(int id, int tableSize) {
        return id % tableSize;
    }


    /**
     * Insert method for the hash table
     *
     * @param id
     *            the id to be inserted into the hash table
     * @param handle
     *            the handle to be inserted into the hash table
     */
    public void insert(int id, Handle handle) {
        if (checkResize()) {
            increaseSize();
            System.out.println("Hash table expanded to " + allRecords.length
                + " records");
        }
        int index = HashTable.h(id, allRecords.length);

        int i = 0;
        while (allRecords[index] != null) {
            if (allRecords[index] == tombstone) {
                break; // Insert at tombstone position
            }
            i++;
            // Quadratic probing to find the next available index
            index = (HashTable.h(id, allRecords.length) + (((i * i) + i) / 2))
                % allRecords.length;

//            if (i == allRecords.length) {
//                return; // Prevent infinite loop if table is full
//            }
        }
        allRecords[index] = new Record(id, handle);
        numberOfRecords++;
    }


    /**
     * Helper method to check if a resize is needed for the hash table
     *
     * @return true if the entries exceed 50% of the hash table size
     */
    private boolean checkResize() {
        int entryCount = 0;
        for (int i = 0; i < allRecords.length; i++) {
            if (allRecords[i] != null && allRecords[i] != tombstone) {
                entryCount++;
            }
        }
        return allRecords.length < (2 * (entryCount + 1));
        //return allRecords.length / 2 <= entryCount;
    }


    /**
     * Increases the size of the hash table if resizing is necessary
     */
    private void increaseSize() {
        int doubledLength = allRecords.length * 2;
        Record[] doubledRecords = new Record[doubledLength];

        int newIndex;
        for (int i = 0; i < allRecords.length; i++) {
            if (allRecords[i] != null && allRecords[i] != tombstone) {
                newIndex = HashTable.h(allRecords[i].getKey(), doubledLength);
                int whileLoopCounter = 0;

                while (doubledRecords[newIndex] != null) {
                    whileLoopCounter++;
                    newIndex = (HashTable.h(allRecords[i].getKey(),
                        doubledLength) + (whileLoopCounter * whileLoopCounter + whileLoopCounter)/2)
                        % doubledLength;

//                    if (whileLoopCounter == doubledLength) {
//                        return; // Prevent infinite loop if table is full
//                    }
                }
                doubledRecords[newIndex] = allRecords[i];
            }
        }
        // Update the size and table values
        allRecords = doubledRecords;
        size = allRecords.length;
    }


    /**
     * Removes a record from the hash table
     *
     * @param id
     *            the id to be removed from the hash table
     * @return true if something was deleted and false if not
     */
    public boolean delete(int id) {
        if (numberOfRecords == 0) {
            return false;
        }
        int index = search(id);

        if (index != -1) {
            allRecords[index] = tombstone; // Mark the slot as deleted
            numberOfRecords--;
            return true;
        }
        return false;
    }


// /**
// * Finds the index of a specified record
// *
// * @param rec
// * the record to be found
// * @return the index of the specified record, or -1 if not found
// */
// public int find(Record rec) {
// int index = HashTable.h(rec.getKey(), allRecords.length);
// int i = 0;
// while (allRecords[index] != null && (i < allRecords.length)) {
// if (allRecords[index] != tombstone && allRecords[index].key == (rec
// .getKey())) {
// return index;
// }
// i++;
// index = (HashTable.h(rec.getKey(), allRecords.length) + (i * i))
// % allRecords.length;
// }F
// return -1; // Record not found
// }


    /**
     * Finds the index of a specified name
     *
     * @param id
     *            the id of the record to be found
     * @return the index of the specified record, or -1 if not found
     */
    public int search(int id) {
        int index = HashTable.h(id, allRecords.length);
        int i = 0;
        while (allRecords[index] != null && (i < allRecords.length)) {
            if (allRecords[index] != tombstone && allRecords[index].key == id) {
                return index;
            }
            i++;
            index = (HashTable.h(id, allRecords.length) + ((i * i) + i) / 2)
                % allRecords.length;
        }
        return -1; // Record not found
    }


    /**
     * Prints the hash table
     */
    public void print() {
        System.out.println("Hashtable:");
        int i = 0;
        while ((i < allRecords.length)) {
            if (allRecords[i] != null) {
                if (allRecords[i].key == -1) {
                    System.out.println(i + ": TOMBSTONE");

                }
                else {
                    System.out.println(i + ": " + allRecords[i].key);
                }
            }
            i++;
        }
        System.out.println("total records: " + numberOfRecords);
    }
}
