/**
 * Calls the methods from the Hash class and uses them
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class Controller {

    private HashTable ht;

    /**
     * 
     * The default constructor
     *
     * @param tableSize
     *            The size of the hash
     */
    public Controller(int tableSize) {
        ht = new HashTable(tableSize);
    }


    /**
     * Getter method for the hash table
     * 
     * @return the number of songs
     */
    public HashTable getHashTable() {
        return ht;
    }


    /**
     * Insert the id into the hash table
     * 
     * @param id
     *            The id to be inserted.
     * @param songName
     *            The name of the song to be inserted.
     */
    public boolean insertID(int id) {
        if (ht.search(id) == -1) {
            ht.insert(id);
            // Insert the id if not found
            return true;
        }
        return false;
        // System.out.println("|" + ID + "| already exists in the
        // Artist database.");
    }


    /**
     * Remove an artist and song into the respective databases.
     * 
     * @param id
     *            The id to be removed.
     */

    public boolean deleteID(int id) {
        if (ht.search(id) != -1) { // If artist exists
            ht.delete(id);
            return true;
            // System.out.println("|" + artistName + "| has been removed
            // from the database.");
        }
        return false;
        // System.out.println("|" + id + "| does not exist in the database.");
    }

    /**
     * 
     * Prints the artists and songs into the output file with their index.
     *
     * 
     * 
     * @param hashTable
     *            The hash table to be printed either songs or artists
     * 
     */
    // public void print(Hash hashTable)
    // {
    // hashTable.print();
    // }
}
