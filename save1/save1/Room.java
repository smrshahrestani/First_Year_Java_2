import java.util.Set;
import java.util.HashMap;
import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes and Seyed Mohammad Reza Shahrestani
 * @version 2019.11.26
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> itemsInRoom = new ArrayList<>();
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     * shows the details of the room's items.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        returnString+= "\nItems in the room:\n";
        returnString+= getRoomItems();
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * get items from the room (int)
     */
    public Item getItems(int index)
    {
        return itemsInRoom.get(index); 
    }
    
    /**
     * get items from the room (string)
     */
    public Item getItems(String itemName)
    {
        for(int i = 0; i < itemsInRoom.size();i++)
        {
            if (itemsInRoom.get(i).getDescription().equals(itemName))
            {
                return itemsInRoom.get(i);
            }
        }
        return null;
    }
    
    /**
     * set different items in room
     */
    public void setItems(Item newItem)
    {
        itemsInRoom.add(newItem); 
    }
    
    /**
     * get a discription of the items in the room
     * 
     */
    public String getRoomItems()
    {
        String print = "";
       for (int i=0; i<itemsInRoom.size(); i++)
       {
           print += itemsInRoom.get(i).getDescription()+"";
       }
       System.out.println("You are carrying: ");
       return print;
    }
    
    /**
     * remove items from the room (string)
     */
    public void removeItems(String itemName)
    {
        for(int i = 0; i < itemsInRoom.size();i++)
        {
            if (itemsInRoom.get(i).getDescription().equals(itemName))
            {
                itemsInRoom.remove(i);
            }
        }
        
    }
}

