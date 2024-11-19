import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Processes commands for the Seminar Manager system
 * Handles insert, delete, search, and print operations
 * 
 * @author Nimay Goradia (ngoradia) and Nicolas Turner (nicturn)
 * @version 11.13/2024
 */
public class CommandProcessor {
    private MemoryManager memManager;
    private HashTable hashTable;
    private Scanner scanner;
// private PrintWriter writer;

    /**
     * Creates a new CommandProcessor
     * 
     * @param memSize
     *            Initial memory pool size
     * @param hashSize
     *            Initial hash table size
     * @param scanner
     *            Input scanner
     */
    public CommandProcessor(int memSize, int hashSize, Scanner scanner) {
        this.memManager = new MemoryManager(memSize);
        this.hashTable = new HashTable(hashSize);
        this.scanner = scanner;
    }


    /**
     * Processes all commands from the input
     */
    public void processCommands() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim().replaceAll("\\s+", " ");
            if (!line.isEmpty()) {
                processCommand(line);
            }
        }
        System.out.print(""); // Flush any lingering output without adding new
                              // lines
    }


    /**
     * Processes a single command
     * 
     * @param line
     *            The command line to process
     */
    private void processCommand(String line) {
        Scanner cmdScanner = new Scanner(line);
        String cmd = cmdScanner.next().trim().replaceAll("\\s+", " ");

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
            String title = scanner.nextLine().trim().replaceAll("\\s+", " ");

            // Get date/time, length, x, y, cost from third line
            String line = scanner.nextLine().trim().replaceAll("\\s+", " ");
            Scanner dataScanner = new Scanner(line);
            String dateTime = dataScanner.next();
            int length = dataScanner.nextInt();
            short x = (short)dataScanner.nextInt();
            short y = (short)dataScanner.nextInt();
            int cost = dataScanner.nextInt();
            dataScanner.close();

            // Get keywords
            String keywordsLine = scanner.nextLine().trim().replaceAll("\\s+",
                " ");
            String[] keywords = keywordsLine.split("\\s+");

            // Get description
            String description = scanner.nextLine().trim().replaceAll("\\s+",
                " ");

            // Create and serialize the seminar
            Seminar sem = new Seminar(id, title, dateTime, length, x, y, cost,
                keywords, description);

            // Check if ID already exists
            if (hashTable.search(id) != -1) {
                System.out.println(
                    "Insert FAILED - There is already a record with ID " + id);
                return;
            }

            byte[] serializedData = sem.serialize();
            Handle handle = memManager.insert(serializedData,
                serializedData.length);
            hashTable.insert(id, handle);

            // Print success message and seminar details
            System.out.println("Successfully inserted record with ID " + id);
            System.out.println(sem.toString());
            System.out.println("Size: " + serializedData.length);

// System.out.println("\n\n");
// memManager.dump();
// System.out.println("\n\n");

        }
        catch (Exception e) {
            System.out.println("Error processing insert command");
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
        // Handle handle = hashTable.search(id);
        int index = hashTable.search(id);

        if (index == -1) {
            System.out.println("Delete FAILED -- There is no record with ID "
                + id);
            return;
        }

        // save the handle before it is deleted
        Handle handle = hashTable.getRecords()[index].getHandle();
        hashTable.delete(id);

        // Only does this if the record was deleted from the hash
        // Remove from both hash table and memory manager
        memManager.remove(handle);
        System.out.println("Record with ID " + id
            + " successfully deleted from the database");
    }


    /**
     * Processes a search command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processSearch(Scanner cmdScanner) {
        int id = cmdScanner.nextInt();
        // Handle handle = hashTable.search(id);

        int index = hashTable.search(id);

        if (index == -1) {
            System.out.println("Search FAILED -- There is no record with ID "
                + id);
            return;
        }

        Handle handle = hashTable.getRecords()[index].getHandle();

        // Get the record from memory manager
        byte[] space = new byte[handle.getLength()];
// int num =
        memManager.get(space, handle, space.length);
// if(handle.getLength() == num)
// {
// System.out.println("yay");
// }

        // Deserialize and print
        try {
            Seminar sem = Seminar.deserialize(space);
            System.out.println("Found record with ID " + id + ":");
            System.out.println(sem.toString());
        }
        catch (Exception e) {
            System.out.println("Error deserializing seminar data");
        }
    }


    /**
     * Processes a print command
     * 
     * @param cmdScanner
     *            Scanner containing command parameters
     */
    private void processPrint(Scanner cmdScanner) {
        String type = cmdScanner.next().trim().replaceAll("\\s+", " ");

        if (type.equals("hashtable")) {
            hashTable.print();
        }
        else if (type.equals("blocks")) {
            System.out.println("Freeblock List:");
            memManager.dump();
        }
    }
}
