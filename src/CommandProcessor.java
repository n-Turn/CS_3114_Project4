import java.util.Scanner;

/**
 * Processes commands for the Seminar Manager system and handles the insert,
 * delete, search, and print operations
 * 
 * @author Nimay Goradia (ngoradia) and Nico Turner (nicturn)
 * @version 11.13.2024
 */
public class CommandProcessor {
    private Controller controller;
    private Scanner scanner;

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
        this.controller = new Controller(memSize, hashSize);
        this.scanner = scanner;
    }


    /**
     * Processes all commands from the input.
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
     * Processes a single command.
     * 
     * @param line
     *            The command line to process.
     */
    private void processCommand(String line) {
        Scanner cmdScanner = new Scanner(line);
        String command = cmdScanner.next();

        switch (command.toLowerCase()) {
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
                System.out.println("Unknown command: " + command);
                break;
        }
        cmdScanner.close();
    }


    /**
     * Processes the "insert" command.
     * 
     * @param cmdScanner
     *            Scanner for command arguments.
     */
    private void processInsert(Scanner cmdScanner) {
        int id = cmdScanner.nextInt();
        String title = scanner.nextLine().trim().replaceAll("\\s+", " ");
        String dateTimeLength = scanner.nextLine().trim().replaceAll("\\s+",
            " ");
        String[] dateTimeLengthParts = dateTimeLength.split("\\s+");
        String dateTime = dateTimeLengthParts[0];
        int length = Integer.parseInt(dateTimeLengthParts[1]);
        short x = Short.parseShort(dateTimeLengthParts[2]);
        short y = Short.parseShort(dateTimeLengthParts[3]);
        int cost = Integer.parseInt(dateTimeLengthParts[4]);

        String[] keywords = scanner.nextLine().trim().split("\\s+");
        String description = scanner.nextLine().trim().replaceAll("\\s+", " ");

        Seminar seminar = new Seminar(id, title, dateTime, length, x, y, cost,
            keywords, description);
        controller.insertSeminar(id, seminar);

    }


    /**
     * Processes the "delete" command.
     * 
     * @param cmdScanner
     *            Scanner for command arguments.
     */
    private void processDelete(Scanner cmdScanner) {
        int id = cmdScanner.nextInt();
        controller.deleteSeminar(id);

    }


    /**
     * Processes the "search" command.
     * 
     * @param cmdScanner
     *            Scanner for command arguments.
     */
    private void processSearch(Scanner cmdScanner) {
        int id = cmdScanner.nextInt();
        controller.searchSeminar(id);

    }


    /**
     * Processes the "print" command.
     * 
     * @param cmdScanner
     *            Scanner for command arguments.
     */
    private void processPrint(Scanner cmdScanner) {
        String type = cmdScanner.next();
        controller.printInfo(type);
    }
}
