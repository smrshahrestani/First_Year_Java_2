/**
 * Write a description of class NPC here.
 *
 * @author Seyed Mohammad Reza Shahrestani(K19019925)
 * @version 2019.11.28
 */
public class NPC
{
    public String name;
    public Room position;
    public String dialogue;

    /**
     * Constructor for objects of class NPC
     */
    public NPC(String name, Room position, String dialogue) 
    {
        this.name = name;
        this.position = position;
        this.dialogue = dialogue;
    }
    
    /**
     * @returns the name of the character
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @returns the position of the character 
     */
    public Room getPosition()
    {
        return position;
    }
    
    /**
     * @returns the diadialogue of the character
     */
    public String getDialogue()
    {
        return dialogue;
    }
}
