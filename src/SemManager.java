
/**
 * Test changes for the SemManger class
 * Please work
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The class containing the main method.
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */

// On my honor:
// - I have not used source code obtained from another current or
//   former student, or any other unauthorized source, either
//   modified or unmodified.
//
// - All source code and documentation used in my program is
//   either my original work, or was derived by me from the
//   source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
//   anyone other than my partner (in the case of a joint
//   submission), instructor, ACM/UPE tutors or the TAs assigned
//   to this course. I understand that I may discuss the concepts
//   of this program with other students, and that another student
//   may help me debug my program so long as neither of us writes
//   anything during the discussion or modifies any computer file
//   during the discussion. I have violated neither the spirit nor
//   letter of this restriction.


public class SemManager {
    /**
     * Runs the project according to the arguments
     * 
     * @param args
     *     Command line parameters
     * @throws FileNotFoundException if the file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        // This is the main file for the program.
        
        int memSize = Integer.parseInt(args[0]);
        int hashSize = Integer.parseInt(args[1]);
        File inputFile = new File(args[2]);
        
        Scanner input = new Scanner(inputFile);
        
        CommandProcessor cp = new CommandProcessor(memSize, hashSize, input);
        cp.processCommands();
        
    }
}
