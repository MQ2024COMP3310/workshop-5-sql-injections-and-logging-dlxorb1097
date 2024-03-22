package workshop05code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
//Included for the logging exercise
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class App {
    // Start code for logging exercise
    
    private static final Logger logger = Logger.getLogger(App.class.getName());
    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        try {// resources\logging.properties
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
            logger.log(Level.WARNING, e1.toString());
        }
    }

    
    // End code for logging exercise
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteConnectionManager wordleDatabaseConnection = new SQLiteConnectionManager("words.db");

        wordleDatabaseConnection.createNewDatabase("words.db");
        if (wordleDatabaseConnection.checkIfConnectionDefined()) {
            //System.out.println("Wordle created and connected.");
            logger.log(Level.INFO, "Wordle created and connected.");
        } else {
            //System.out.println("Not able to connect. Sorry!");
            logger.log(Level.INFO, "Not able to connect. Sorry!");
            return;
        }
        if (wordleDatabaseConnection.createWordleTables()) {
            //System.out.println("Wordle structures in place.");
            logger.log(Level.INFO, "Wordle structures in place.");
        } else {
            //System.out.println("Not able to launch. Sorry!");
            logger.log(Level.INFO, "Not able to launch. Sorry!");
            return;
        }

        // let's add some words to valid 4 letter words from the data.txt file

        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                logger.log(Level.FINE, line);
                wordleDatabaseConnection.addValidWord(i, line);
                i++;
            }

        } catch (IOException e) {
            //System.out.println("Not able to load . Sorry!");
            logger.log(Level.WARNING, "Not able ot load. Sorry!", e);
            //System.out.println(e.getMessage());
            return;
        }

        // let's get them to enter a word

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a 4 letter word for a guess or q to quit: ");
            //logger.log(Level.INFO, "Enter a 4 letter word for a guess or q to quit: ");
            String guess = scanner.nextLine();

            while (!guess.equals("q")) {
                System.out.println("You've guessed '" + guess+"'.");

                if (wordleDatabaseConnection.isValidWord(guess)) { 
                    System.out.println("Success! It is in the the list.\n");
                    //logger.log(Level.INFO, "Success! It is in the list.\n");
                } else if (guess.length() != 4){
                    System.out.println("Word is not four letters.\n");
                    //logger.log(Level.INFO, "Word is not four letters.\n");
                } else if (!guess.equals(guess.toLowerCase())){
                    System.out.println("Word must all be in lowercase.\n");
                    //logger.log(Level.INFO, "Word must all be in lowercase.\n");
                } else {
                    System.out.println("Sorry. This word is NOT in the the list.\n");
                    //logger.log(Level.INFO, "Sorry. This word is NOT in the list.\n");
                }

                System.out.print("Enter a 4 letter word for a guess or q to quit: " );
                //logger.log(Level.INFO, "Enter a 4 letter word for a guess or q to quit: " );
                guess = scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.toString());
        }

    }
}