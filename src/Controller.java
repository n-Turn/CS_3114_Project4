/**
 * Calls the methods from the Hash class and uses them
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class Controller {

//    private HashTable ht;
//
//    /**
//     * 
//     * The default constructor
//     *
//     * @param tableSize
//     *            The size of the hash
//     */
//    public Controller(int tableSize) {
//        ht = new HashTable(tableSize);
//    }
//
//
//    /**
//     * Getter method for the hash table
//     * 
//     * @return the hash table is returned
//     */
//    public HashTable getHashTable() {
//        return ht;
//    }
//
//
//    /**
//     * Insert the id into the hash table
//     * 
//     * @param id
//     *            The id to be inserted.
//     * @param handle
//     *            The handle to be inserted.
//     */
//    public void insertID(int id, Handle handle) {
//        // insert id if not found
//        ht.insert(id, handle);
//        // System.out.println("|" + id + "| already exists in the database.");
//    }
//
//
//    /**
//     * Remove an id from the database.
//     * 
//     * @param id
//     *            The id to be removed.
//     * @return true if something was deleted and false if not
//     */
//    public boolean deleteID(int id) {
//        if (ht.search(id) != -1) {
//            // If artist exists
//            ht.delete(id);
//            return true;
//            // System.out.println("|" + id + "| has been removed
//            // from the database.");
//        }
//        return false;
//        // System.out.println("|" + id + "| does not exist in the database.");
//    }
//
//
//    /**
//     * Searches for an id in the database.
//     * 
//     * @param id
//     *            The id to be searched for.
//     * @return true if found and false if not
//     */
//    public boolean searchID(int id) {
//        return ht.search(id) != -1;
//    }
//
//
//    /**
//     * Prints the artists and songs into the output file with their index.
//     */
//    public void print() {
//        ht.print();
//    }
}
