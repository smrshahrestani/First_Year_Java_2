/**
 * Write a description of class Items here.
 *
 * @author Seyed Mohammad Reza Shahrestani(K19019925)
 * @version 2019.11.26
 */
public class Item
{
    // instance variables - replace the example below with your own
    String description;
    String name ;
    double weight ;
    Item item;
    /**
     * Constructor for objects of class Items
     */
    public Item(String description,double weight)
    {
        this.description = description;
        this.name = name;
        this.weight = weight;
        //item = new Item(description , weight);
    }

    /**
     * @returns the item decdescription
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * @returns the item name
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * @returns the item weight
     */
    public double getWeight()
    {
        return weight;
    }


    
}
