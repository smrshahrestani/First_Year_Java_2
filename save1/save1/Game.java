import java.util.*;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Seyed Mohammad Reza Shahrestani.
 * @version 2019.11.26
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    //private HashMap<String, Room> action;   
    private Time time;
    private Scanner scan;
    private boolean finished = false;
    private int money;
    private int totalWeight;
    private int currentWeight;
    private int seconds = 0;
    private Room outsideBushHouse, outsideWaterloo, bLecture, wLecture, canteen, lab, office, waterloo, bushHouse, wParking,bParking;
    private Room previousRoom;
    private ArrayList<Item> inventory = new ArrayList<>();
    private HashMap<String, Item> inventoryHashMap = new HashMap<>();
    
    private HashMap<Item , Integer> hashWeight;

    private Stack<Room> roomStack;     //to be changed
    private Command command;           //to be changed

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        Time time = new Time();
        parser = new Parser();
        scan = new Scanner(System.in);
        previousRoom = currentRoom;
        money = 100;
        totalWeight = 0;
        currentWeight = 0; 
        roomStack = new Stack<Room>();
        hashWeight = new HashMap<>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        outsideBushHouse = new Room("outside the main entrance of the Bush House Building");
        outsideWaterloo = new Room("outside the main entrance of Waterloo campus");
        bLecture = new Room("in the Bush House lecture theater");
        wLecture = new Room("in the Waterloo lecture theater");
        canteen = new Room("in the Waterloo campus canteen");
        lab = new Room("in the computing lab");
        office = new Room("in the computing admin office");
        // my own rooms:
        waterloo = new Room("The Waterloo campus");
        bushHouse = new Room("The Bush House Building");
        wParking = new Room("Waterloo campus parking");
        bParking = new Room("Bush House Parking");

        // initialise room exits
        outsideBushHouse.setExit("east", lab);
        outsideBushHouse.setExit("south", bLecture);
        outsideBushHouse.setExit("west", bLecture);
        outsideBushHouse.setExit("north", bParking);
        bParking.setExit("waterloo", wParking);
        //outsideBushHouse.setExit("Waterloo", outsideWaterloo);

        outsideWaterloo.setExit("east", wLecture);
        outsideWaterloo.setExit("south", canteen);
        outsideWaterloo.setExit("west", canteen);
        outsideWaterloo.setExit("north", wParking);
        wParking.setExit("bushHouse", bParking);
        //outsideWaterloo.setExit("BushHouse", outsideBushHouse);

        bLecture.setExit("east", outsideBushHouse);
        lab.setExit("west", outsideBushHouse);
        lab.setExit("south", office);
        office.setExit("north", lab);
        bParking.setExit("south", outsideBushHouse);

        wLecture.setExit("west", outsideWaterloo);
        canteen.setExit("east", outsideWaterloo);
        wParking.setExit("south", outsideWaterloo);

        currentRoom = outsideBushHouse;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            

        printWelcome();
        if (scan.nextLine().equals("yes"))
        {
            Time time = new Time();
            time.startTime();
            System.out.println(currentRoom.getLongDescription());

            // Enter the main command loop.  Here we repeatedly read commands and
            // execute them until the game is over.
            //boolean finished = false;
            while (! finished ) {
                Command command = parser.getCommand();
                finished = processCommand(command);
            }
            System.out.println("Thank you for playing.  Good bye.");
        }
        else
        {
            System.out.println("Come back when you where ready!!!");
            System.out.println("BYEEEEEEEEEEEEE");
        }

    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        //System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("Type 'yes' if you are ready to start!!!");
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {    
        String commandWord = command.getCommandWord();

        boolean wantToQuit = false;

        if(command.isUnknown())
        {
            System.out.println("I don't know what you mean...");
            return false;
        }

        if (commandWord.equals("help")) 
        {
            printHelp();
        }
        else if (commandWord.equals("go")) 
        {
            goRoom(command);
        }
        else if (commandWord.equals("quit")||commandWord.equals("bye")) 
        {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("goWithCar"))
        {

            //car.withCar() = true;
        }
        else if (commandWord.equals("parkTheCar"))
        {
            //car.withCar() = false;
        }
        else if (commandWord.equals("timeLeft"))
        {
            time.timeLeft();
        }
        else if (commandWord.equals("moneyLeft"))
        {
            System.out.println("you balance is £"+money);
        }
        else if (commandWord.equals("goBack"))
        {
            goBack(command);
        }
        else if (commandWord.equals("pick"))
        {
            pickItem(command);
        }
        else if (commandWord.equals("drop"))
        {

        }
        else if (commandWord.equals("inventory"))
        {
            printInventory();
        }

      
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * pickes the item and place it in inventory 
     * and deletes is from the current room
     */
    private void pickItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to get...
            System.out.println("Get what?");
            return;
        }

        String item = command.getSecondWord();

        Item newItem = currentRoom.getItems(item);

        if (newItem == null) 
        {
            System.out.println("There is no such an item!");
        }
        else 
        {
            inventory.add(newItem);
            currentRoom.removeItems(item);
            System.out.println(item+  " has been picked up!");
        }
    }

    /**
     * drops the item from myList 
     * and places it in the current room
     */
    private void dropItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("drop what?");
            return;
        }

        String item = command.getSecondWord();

        Item newItem = null;
        int index = 0;
        for (int i = 0; i< inventory.size();i++)
        {

            if (inventory.get(i).getDescription().equals(item))
            {
                newItem= inventory.get(i);
                index = i;
            }
        }

        if (newItem == null) 
        {
            System.out.println("You're not carying such an item!");
        }
        else 
        {
            inventory.remove(index);
            currentRoom.setItems(new Item (item));
            System.out.println(item+ " has been dropped!");
        }
    }

    /**
     * 
     */
    private void printInventory()
    {
        String print = "";
        for (int i=0; i<inventory.size(); i++)
        {
            print += inventory.get(i).getDescription() + "";
        }
        System.out.println("You are carrying: ");
        System.out.println(print);
    }
    

    // implementations of user commands:

    /**
     * Print out some help information./
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private boolean goRoom(Command command)
    {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        // you win if:::
        return false;
    }

    /**
     * 
     */
    private void goBaaack()
    {
        currentRoom = previousRoom;
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * 
     * 
     */
    private void goBack(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
            return;

        }

        if(previousRoom == null) {
            System.out.println("Sorry, cannot go back.");
            return;
        }
        roomStack.push(currentRoom);
        Room temp = currentRoom;
        currentRoom = previousRoom;
        previousRoom = temp;
        System.out.println("You have gone back to the previous room.");
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * returns true to quit the game 
     * this method is used in the Time class to finish the game when the time is over
     */
    private boolean timeUp()
    {
        finished = true;
        return finished;
    }

    /** 
     * not compleated
     * 
     * 
     */
    private boolean goWithCar(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("How?");
            return false;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; 
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        // you win if:::
        return false;
    }
}

