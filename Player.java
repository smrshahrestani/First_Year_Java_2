import java.util.*;
/**
 * Write a description of class Player here.
 *
 * @author Seyed Mohammad Reza Shahrestani(K19019925)
 * @version 2019.11.26
 */
public class Player
{
    public ArrayList<Item> inventory;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        inventory = new ArrayList<>();
    }

    /**
     * @returns the inventory
     */
    public ArrayList getInventory()
    {
        return inventory;
    }
    
}
