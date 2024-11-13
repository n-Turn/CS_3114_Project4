import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Processes commands for the Seminar Manager system
 * Handles insert, delete, search, and print operations
 * 
 * @author Nicolas Turner and Nimay Goradia
 * @version {Put Something Here}
 */
public class CommandProcessor {
    private MemoryManager memManager;
    private HashTable hashTable;
    private Scanner scanner;
    private PrintWriter writer;

    /**
     * Creates a new CommandProcessor
     * 
     * @param memSize
     *            Initial memory pool size
     * @param hashSize
     *            Initial hash table size
     * @param scanner
     *            Input scanner
     * @param writer
     *            Output writer
     */
    public CommandProcessor(
        int memSize,
        int hashSize,
        Scanner scanner,
        PrintWriter writer) {
        this.memManager = new MemoryManager(memSize);
        this.hashTable = new HashTable(hashSize);
        this.scanner = scanner;
        this.writer = writer;
    }


    /**
     * Processes all commands from the input
     */
    public void processCommands() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                processCommand(line);
            }
        }
    }


    /**
     * Processes a single command
     * 
     * @param line
     *            The command line to process
     */
    private void processCommand(String line) {
        Scanner cmdScanner = new Scanner(line);
        String cmd = cmdScanner.next().trim();

        switch (cmd) {
            case "insert":
                processInsert(cmdScanner);
                break;
            case "delete":
                processDelete(cmdScanner);
                break;
            case "search":
                processSearch(cmdScanner);
                break;
            case "print":
                processPrint(cmdScanner);
                break;
            default:
                // Invalid command - ignore
                break;
        }
        cmdScanner.close();
    }


    /**
     * Processes an insert command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processInsert(Scanner cmdScanner) {
        try {
            // Get ID from first line
            int id = cmdScanner.nextInt();

            // Read the next 4 lines for seminar data
            String title = scanner.nextLine().trim();

            // Get date/time, length, x, y, cost from third line
            String line = scanner.nextLine().trim();
            Scanner dataScanner = new Scanner(line);
            String dateTime = dataScanner.next();
            int length = dataScanner.nextInt();
            short x = (short)dataScanner.nextInt();
            short y = (short)dataScanner.nextInt();
            int cost = dataScanner.nextInt();
            dataScanner.close();

            // Get keywords
            String keywordsLine = scanner.nextLine().trim();
            String[] keywords = keywordsLine.split("\\s+");

            // Get description
            String description = scanner.nextLine().trim();

            // Create and serialize the seminar
            Seminar sem = new Seminar(id, title, dateTime, length, x, y, cost,
                keywords, description);
            
            // Check if ID already exists
            if (hashTable.search(id) != -1) {
                writer.println(
                    "Insert FAILED - There is already a record with ID " + id);
                return;
            }

            // Insert into memory manager and hash table
            try {
                byte[] serializedData = sem.serialize();
                Handle handle = memManager.insert(serializedData,
                    serializedData.length);
                hashTable.insert(id, handle);

                // Print success message and seminar details
                writer.println("Successfully inserted record with ID " + id);
                writer.println(sem.toString());
                writer.println("Size: " + serializedData.length);

            }
            catch (Exception e) {
                writer.println("Error: Could not serialize/store seminar");
            }
        }
        catch (Exception e) {
            writer.println("Error processing insert command");
        }
    }


    /**
     * Processes a delete command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processDelete(Scanner cmdScanner) {
        int id = cmdScanner.nextInt();
        //Handle handle = hashTable.search(id);
        int index = hashTable.search(id);
        Boolean recordExists = hashTable.delete(id);

        if (!recordExists) {
            writer.println("Delete FAILED -- There is no record with ID " + id);
            return;
        }
        // Only does this if the record was deleted from the hash
        // Remove from both hash table and memory manager
        Handle handle = hashTable.getRecords()[index].getHandle();
        memManager.remove(handle);
        writer.println("Record with ID " + id
            + " successfully deleted from the database");
    }


    /**
     * Processes a search command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processSearch(Scanner cmdScanner) {
        try {
            int id = cmdScanner.nextInt();   
            //Handle handle = hashTable.search(id);
            
            int index = hashTable.search(id);

            if (index == -1) {
                writer.println("Search FAILED -- There is no record with ID "
                    + id);
                return;
            }
            Handle handle = hashTable.getRecords()[index].getHandle();


            // Get the record from memory manager
            byte[] space = new byte[handle.getLength()];
            memManager.get(space, handle, handle.getLength());

            // Deserialize and print
            try {
                Seminar sem = Seminar.deserialize(space);
                writer.println("Found record with ID " + id + ":");
                writer.println(sem.toString());
            }
            catch (Exception e) {
                writer.println("Error deserializing seminar data");
            }
        }
        catch (Exception e) {
            writer.println("Error processing search command");
        }
    }


    /**
     * Processes a print command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processPrint(Scanner cmdScanner) {
        String type = cmdScanner.next().trim();

        if (type.equals("hashtable")) {
            writer.println("Hashtable:");
            hashTable.print(writer);
        }
        else if (type.equals("blocks")) {
            writer.println("Freeblock List:");
            memManager.dump();
        }
    }
}
