/**
 * Controller class that initializes the MemoryManager and HashTable
 * to manage the seminars.
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class Controller {

    private MemoryManager memManager;
    private HashTable hashTable;

    /**
     * Initializes the Controller with specified memory and hash table sizes.
     * 
     * @param memSize
     *            Initial memory pool size.
     * @param hashSize
     *            Initial hash table size.
     */
    public Controller(int memSize, int hashSize) {
        this.memManager = new MemoryManager(memSize);
        this.hashTable = new HashTable(hashSize);
    }


    /**
     * Inserts a new seminar into the system.
     * 
     * @param id
     *            The id to insert.
     * @param seminar
     *            The Seminar object to insert.
     */
    public void insertSeminar(int id, Seminar seminar) {

        // if duplicate
        if (hashTable.search(id) != -1) {
            System.out.println(
                "Insert FAILED - There is already a record with ID " + id);
            return;
        }

        try {
            // serialize the seminar
            byte[] serializedData = seminar.serialize();

            // insert into the mem manager
            Handle handle = memManager.insert(serializedData,
                serializedData.length);

            // insert the handle into the hash table with the id
            hashTable.insert(id, handle);

            System.out.println("Successfully inserted record with ID " + id
                + "\n" + seminar.toString() + "\n" + "Size: "
                + serializedData.length);
        }
        catch (Exception e) {
            System.out.println("Error inserting seminar with ID " + id + ": "
                + e.getMessage());
        }
    }


    /**
     * Deletes a seminar from the system by ID.
     * 
     * @param id
     *            The ID of the seminar to delete.
     */
    public void deleteSeminar(int id) {
        int index = hashTable.search(id);

        // if the id doesn't exist
        if (index == -1) {
            System.out.println("Delete FAILED -- There is no record with ID "
                + id);
            return;
        }

        // gets the handle from the hashtable based on the index
        Handle handle = hashTable.getRecords()[index].getHandle();

        // delete from the hash table
        hashTable.delete(id);

        // remove from the memory
        memManager.remove(handle);

        System.out.println("Record with ID " + id
            + " successfully deleted from the database");

    }


    /**
     * Searches for a seminar by ID.
     * 
     * @param id
     *            The ID of the seminar to search for.
     */
    public void searchSeminar(int id) {
        int index = hashTable.search(id);

        // if the seminar doesn't exist
        if (index == -1) {
            System.out.println("Search FAILED -- There is no record with ID "
                + id);
            return;
        }

        try {
            // get handle from hashtable using index
            Handle handle = hashTable.getRecords()[index].getHandle();

            // allocate space to retrieve the data
            byte[] space = new byte[handle.getLength()];
            memManager.get(space, handle, space.length);

            Seminar sem = Seminar.deserialize(space);

            System.out.println("Found record with ID " + id + ":\n" + sem
                .toString());
        }
        catch (Exception e) {
            System.out.println("Error deserializing seminar data for ID " + id
                + ": " + e.getMessage());
        }
    }


    /**
     * Prints the current state of the hash table or the free blocks.
     * 
     * @param type
     *            The type of information to print either hashtable or
     *            freeblocks
     */
    public void printInfo(String type) {

        switch (type.toLowerCase()) {
            case "hashtable":
                hashTable.print();
                break;

            case "blocks":
                memManager.dump();
                break;

            default:
                System.out.println(
                    "Invalid print type. Use 'hashtable' or 'blocks'.");
                break;
        }
    }
}
