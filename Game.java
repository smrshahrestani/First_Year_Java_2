import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
/**
 *  This class is the main class of the "King's World" application. 
 *  "King's World" is a very simple, text based adventure game.
 *  Users can walk around some scenery. they can pick and drop items.
 *  
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Seyed Mohammad Reza Shahrestani(K19019925)
 * @version 2019.11.26
 */

public class Game
{
    //calling rooms
    private Room outsideBushHouse, outsideWaterloo, bLecture, wLecture, teleport;
    private Room canteen, lab, office, waterloo ,bushHouse,bushHouseBusS, waterlooBusS, shop;
    private Parser parser;
    private Room currentRoom;  
    private Scanner scan;
    private Player player;
    private Room previousRoom;
    private NPC npc;     
    private Command command; 
    
    private ArrayList<Item> itemsInRoom;
    private ArrayList<Item> inventory ;
    private Stack<Room> roomStack;          //for goBack method
    private ArrayList<Item> items ;        
    private ArrayList<Room> teleportableRooms;  //adding rooms to array list for teleport method
    private ArrayList<Room> characterRoom;      //array list for character rooms
    
    private boolean finished = false;   //to finish the game
    private double oysterBalance;
    private double totalWeight;
    private double currentWeight;
    private int seconds;
    private double wallet;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        itemsInRoom = new ArrayList<>();
        createRooms();
        setItem();
        character();
        npc = new NPC("" , null , "");
        scan = new Scanner(System.in);
        parser = new Parser();
        roomStack = new Stack<Room>();
        player = new Player();
        items = new ArrayList<>();
        inventory = new ArrayList<>();
        previousRoom = currentRoom;
        oysterBalance = 0;
        totalWeight = 0;
        currentWeight = 0; 
        wallet = 15;       //your wallet balance
        seconds = 100;     //total time
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
        shop = new Room("in the shop which you can buy oyster from");
        bushHouseBusS = new Room("at Bush House bus station");
        waterlooBusS = new Room("at Waterloo bus station");
        waterloo = new Room("in The Waterloo campus");
        bushHouse = new Room("in The Bush House Building");
        teleport = new Room("in This room teleports you randomly to other rooms");

        outsideBushHouse.setExit("east", lab);
        outsideBushHouse.setExit("south", bLecture);
        outsideBushHouse.setExit("west", bLecture);
        outsideBushHouse.setExit("north", shop);
        outsideBushHouse.setExit("waterloo", outsideWaterloo);
        outsideWaterloo.setExit("east", wLecture);
        outsideWaterloo.setExit("north", teleport());
        outsideWaterloo.setExit("west", canteen);
        outsideWaterloo.setExit("south", waterlooBusS);
        outsideWaterloo.setExit("bushHouse", outsideBushHouse);
        bLecture.setExit("east", outsideBushHouse);
        lab.setExit("west", outsideBushHouse);
        lab.setExit("south", office);
        office.setExit("north", lab);
        wLecture.setExit("west", outsideWaterloo);
        canteen.setExit("east", outsideWaterloo);
        shop.setExit("east",bushHouseBusS);
        shop.setExit("south", outsideBushHouse);
        bushHouseBusS.setExit("west", shop);
        waterlooBusS.setExit("north",outsideWaterloo);

        currentRoom = outsideBushHouse;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     *  exits the loop if either you manage to finish the game or your time is up.
     */
    public void play() 
    {
        printWelcome();
        if (scan.nextLine().equals("yes")) //asks if the user wants to start the game.
        {
            System.out.println(currentRoom.getLongDescription());

            // Enter the main command loop.  Here we repeatedly read commands and
            // execute them until the game is over.
            while (! finished && !timeOver() && !win() ) 
            {
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
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println(" ║\t Welcome to the 'King's World' game                                                      ║");
        System.out.println(" ║\t This is in the King's College London                                                    ║");
        System.out.println(" ║\t You can walk between rooms                                                              ║");
        System.out.println(" ║\t You can walk between Waterloo and Bush House campus                                     ║");
        System.out.println(" ║\t You can use bus between the campuses                                                    ║");
        System.out.println(" ║\t You can pick and drop items in different rooms                                          ║");
        System.out.println(" ║\t Some Items can be picked and some can't                                                 ║");
        System.out.println(" ║\t You can carry up to a certain total weight                                              ║");
        System.out.println(" ║\t There is a teleport room in the north of waterloo campus                                ║");
        System.out.println(" ║\t which teleports you between all rooms                                                   ║");
        System.out.println(" ║\t You have 100 seconds to finish the game                                                 ║");
        System.out.println(" ║\t each time you go to a room, it takes 5 seconds.                                         ║");
        System.out.println(" ║\t each time you go to back, it takes 7 seconds.                                           ║");
        System.out.println(" ║\t each time you go to the teleport room, it takes 3 seconds.                              ║");
        System.out.println(" ║\t each time you walk between BushHouse and Waterloo, it takes 20 seconds.                 ║");
        System.out.println(" ║\t each time you ride a bus between BushHouse and Waterloo, it takes 5 seconds.            ║");
        
        System.out.println(" ║\t Your goal is to find the computer and put it in the office                              ║");
        System.out.println(" ║\t and you need to find the laptop and put it in the waterloo canteen                      ║");
        System.out.println(" ║\t and also you need to have oyster in your inventory to finish the game.                  ║");
        System.out.println(" ║\t don't forget, you only have 100 seconds.                                                ║");
        
        System.out.println(" ║\tType 'help' if you need help.                                                            ║");
        System.out.println(" ║\t                                                                                         ║");
        System.out.println(" ║\tType 'yes' if you are ready to start!!!                                                  ║");
        System.out.println(" ╚════════════════════════════════════════════════════════════════════════════════╝");
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
        int weight = 0;
        String commandS = command.toString();
        Item oyster = new Item("oyster" , 0.1 );

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
        else if (commandWord.equals("timeLeft"))  //it prints the remaining time.
        {
            System.out.println("the remainig time is: "+seconds+" seconds");
        }
        else if (commandWord.equals("oysterBalance"))   //it prints your oyster balance if you have it in your inventory.
        {
            for (int i = 0; i < inventory.size(); ++i) {
                if (inventory.get(i).getDescription().equals("oyster")) {
                    System.out.println("you balance is £" + oysterBalance);
                } else {
                    System.out.println("You don't have Oyster card!");
                    System.out.println("You can find one in the Bush House shop.");
                }
            }
        }
        else if (commandWord.equals("goBack"))     //go to the previous room.
        {
            goBack(command);
        }
        else if (commandWord.equals("pick"))       //picks pickable items
        {
            pickItem(command);
        }
        else if (commandWord.equals("drop"))       //drops the items from inventory
        {
            dropItem(command);
        }
        else if (commandWord.equals("inventory"))  //prints your inventory
        {
            printInventory();
        }
        else if (commandWord.equals("wallet-oyster"))  //tops up your oyster by £5.
        {
            addMoneyTOOyster(5);   //adds £5 each time to the oyster
        }
        else if (commandWord.equals("wallet"))     //prints your wallet balance
        {
            System.out.println("You have £" + wallet + " in your wallet." );
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * sets the items in each room
     * set name and weight of the item
     */
    private void setItem()
    {
        lab.setItems(new Item("computer", 4.5));
        shop.setItems(new Item("oyster" , 0.1));
        canteen.setItems(new Item("table", 20.0));
        canteen.setItems(new Item("chair",10.0));
        office.setItems(new Item("laptop", 2.5));
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
        //Item newItem = currentRoom.getItems(item);
        Item newItem = currentRoom.getItems(item);
        if (newItem == null) 
        {
            System.out.println("There is no such an item!");
        }
        else 
        {
            if(newItem.getDescription().equals("chair")|| newItem.getDescription().equals("table"))    //these items cant be picked
            {
                System.out.println("Sorry, you cant pick this item");
                System.out.println("This item is heavy for you to carry!");
            }
            else if(newItem.getDescription().equals("oyster")&& wallet >= 5)  //it pickes oyster if you have more than £5 in your wallet
            {
                seconds-= 2;
                wallet -= 5;
                currentWeight += 0.1 ;
                inventory.add(newItem);
                currentRoom.removeItems(item);
                System.out.println(item+  " has been picked up!");
            }
            else if(newItem.getDescription().equals("oyster")&& wallet < 5)  //prints this if you have less than £5 in your wallet
            {
                System.out.println("You don't have enough money to buy an oyster!");
            }
            else if(newItem.getDescription().equals("laptop"))   //pick laptop
            {
                seconds-= 2;
                currentWeight += 2.5;
                inventory.add(newItem);
                currentRoom.removeItems(item);
                System.out.println(item+  " has been picked up!");
            }
            else if(newItem.getDescription().equals("computer"))  //pick computer
            {
                seconds-= 2;
                currentWeight += 4.5;
                inventory.add(newItem);
                currentRoom.removeItems(item);
                System.out.println(item+  " has been picked up!");
            }
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
        else if(newItem.getDescription().equals("oyster"))   //drops oyster
        {
            if(currentRoom == shop)      //if you drop the oyster in the shop, your oyster balance will be added to your wallet.
            {
                seconds-= 2;
                currentWeight -= 0.1 ;
                wallet += oysterBalance + 5;
                inventory.remove(index);
                currentRoom.setItems(newItem);  
                System.out.println(item+ " has been dropped!");
            }
            else
            {
                seconds-= 2;
                currentWeight -= 0.1 ;
                inventory.remove(index);
                currentRoom.setItems(newItem);  
                System.out.println(item+ " has been dropped!");
            }
        }
        else if(newItem.getDescription().equals("laptop"))   //drops the laptop
        {
            seconds-= 2;
            currentWeight -= 2.5;
            inventory.remove(index);
            currentRoom.setItems(newItem);  
            System.out.println(item+ " has been dropped!");
        }
        else if(newItem.getDescription().equals("computer"))  //drops the computer
        {
            seconds-= 2;
            currentWeight -= 4.5;
            inventory.remove(index);
            currentRoom.setItems(newItem);  
            System.out.println(item+ " has been dropped!");
        }
        else 
        {
            seconds-= 2;
            inventory.remove(index);
            currentRoom.setItems(newItem);  
            System.out.println(item+ " has been dropped!");
        }
    }

    /**
     * prints the inventory
     */
    private void printInventory()
    {
        String print = "";
        for (int i=0; i<inventory.size(); i++)
        {
            print += inventory.get(i).getDescription() + " ";
        }
        System.out.println("You are carrying: ");
        System.out.println(print);
        System.out.println("The total weight your carrying is: "  + currentWeight +" KG");
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
        if (!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }
        String direction = command.getSecondWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        if (currentRoom == character())    //if you meet the character, he will say hi to you and you'll get £2.
        {
            System.out.println("Hello my friend, long time no see!");
            System.out.println("you get £2 !");
            wallet += 2;
        }
        //if you are in either BushHouse and Waterloo and want to walk to the another campus
        //you'll loose 20 seconds
        if (currentRoom == outsideWaterloo && direction.equals("bushHouse") || direction.equals("waterloo") && currentRoom == outsideBushHouse)
        {
            seconds -= 20;
            System.out.println("You spend 20 seconds walking!");
            System.out.println("You could go by bus to save more time.");
        }
        if (!command.hasThirdWord())
        {
            if (nextRoom == null) {
                System.out.println("There is no door!");
            }
            else {
                seconds-= 5;
                previousRoom = currentRoom;
                currentRoom = nextRoom;
                System.out.println(currentRoom.getLongDescription());
            }
        }
        else if (command.hasThirdWord())
        {
            if (currentRoom == bushHouseBusS || currentRoom == waterlooBusS)   //you can use the bus if you are in the bus station
            {
                for (int i = 0; i < inventory.size(); ++i) {
                    if(oysterBalance > 1.5)  //if you have balance in your oyster
                    {
                        if(inventory.get(i).getDescription().equals("oyster")) 
                        {
                            if (command.getThirdWord().equals("bus"))   //if the second word is bus.
                            {
                                if (command.getSecondWord().equals("waterloo")||(command.getSecondWord().equals("bushHouse")))
                                {   bushHouseBusS.setExit("waterloo", waterlooBusS);
                                    waterlooBusS.setExit("bushHouse",bushHouseBusS);
                                    previousRoom = currentRoom;
                                    currentRoom = currentRoom.getExit(direction);
                                    System.out.println(currentRoom.getLongDescription());
                                    oysterBalance -= 1.5;
                                    seconds -= 5;
                                    System.out.println("your new oyster balance is: £"+ oysterBalance);
                                }
                                else
                                {
                                    System.out.println("You can't go with bus.");
                                }
                            }
                            else
                            {
                                System.out.println("You only can go by bus.");
                            }
                        }
                        else 
                        {
                            System.out.println("You need to have an oyster card to use a bus.");
                            System.out.println(inventory.contains("oyster"));
                        }
                    } 
                    else 
                    {
                        System.out.println("Your balance is low");
                    }
                }
            }
            else
            {
                System.out.println("You need to go to the bus station");
            }
            printInventory();
            return false;
        }
        else
        {
            System.out.println("You have to go to the bus station");
        }
        return false;
    }

    /** 
     * if it can go back, goes to the previous room
     * otherwise prints an error
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
        seconds-= 7;
        roomStack.push(currentRoom);
        Room temp = currentRoom;
        currentRoom = previousRoom;
        previousRoom = temp;
        System.out.println("This is the previous room.");
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
     * teleport method which teleports you to other rooms 
     */
    private Room teleport() {

        teleportableRooms = new ArrayList<>();
        teleportableRooms.add(outsideBushHouse);
        teleportableRooms.add(bLecture);
        teleportableRooms.add(wLecture);
        teleportableRooms.add(canteen);
        teleportableRooms.add(lab);
        teleportableRooms.add(office);
        teleportableRooms.add(bushHouseBusS);
        teleportableRooms.add(waterlooBusS);
        teleportableRooms.add(shop);
        seconds-= 3;

        Random rand = new Random();
        int num = rand.nextInt(teleportableRooms.size());  //generates a random number
        Room room = teleportableRooms.get(num);            //assighnes the generated number to the room
        return room;
    }

    /**
     * adds money to your oyster balance
     */
    private void addMoneyTOOyster(double index)
    {
        for (int i = 0; i < inventory.size(); ++i) {
            if (inventory.get(i).getDescription().equals("oyster")) 
            {
                if (wallet >= index)
                {
                    wallet -= index;
                    oysterBalance += index;
                    System.out.println("Your oyster balance is: £"+ oysterBalance);
                    System.out.println("and you have £"+ wallet + " in your wallet");
                }
                else
                {
                    System.out.println("You dont have enough money in your wallet.");
                }
            } 
            else {
                System.out.println("You don't have Oyster card!");
                System.out.println("You can find one in the Bush House shop.");
            }
        }
    }

    /**
     * add all rooms to a arraylist 
     * which the character can move between them
     */
    private Room character() 
    {

        characterRoom = new ArrayList<>();
        characterRoom.add(outsideBushHouse);
        characterRoom.add(bLecture);
        characterRoom.add(wLecture);
        characterRoom.add(canteen);
        characterRoom.add(lab);
        characterRoom.add(office);
        characterRoom.add(bushHouseBusS);
        characterRoom.add(waterlooBusS);
        characterRoom.add(shop);

        Random rand = new Random();
        int num = rand.nextInt(characterRoom.size());
        Room room = characterRoom.get(num);
        return room;

    }

    /**
     * quits the game if player manages to meet all conditions
     */
    private boolean win()
    {
        boolean win = false;
        for (int i = 0; i < inventory.size(); ++i) {
            if (inventory.get(i).getDescription().equals("oyster") && office.getItems("computer") != null && canteen.getItems("laptop") != null) {
                System.out.println("YOU HAVE WON THE GAME.... ;) ");
                win = true;
            }
        } 
        return win;
    }

    /**
     * quits the game if the time is over
     */
    private boolean timeOver()
    {
        if(seconds<=0)
        {
            System.out.println("time is over :( ");
            finished = true;
        }
        return finished;
    }
}