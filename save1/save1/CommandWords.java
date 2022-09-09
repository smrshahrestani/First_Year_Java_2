import java.util.*;
/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes and Seyed Mohammad Reza Shahrestani
 * @version 2019.11.26
 */

public class CommandWords
{
    private HashMap<String, Room> action;
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
            "go", "quit","bye", "help","pick", "drop", "goWithCar" , "parkTheCar" , "timeLeft" , "moneyLeft" ,"goBack" ,"inventory"
        };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
        //action = new HashMap<>();
        //action.put( ,  action);
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
