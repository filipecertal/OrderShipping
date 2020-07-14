package order.packing;

/**
 * Represents an item on the system
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Item implements IItem {
    
    private String reference, description;
    private int depth, height, length, volume;
    
    /**
     * Item's instance constructor
     * 
     * @param reference reference that acts as unique identifier for the item
     * @param description item's description
     * @param depth item's depth
     * @param height item's height
     * @param length item's length
     */
    public Item(String reference, String description, int depth, int height, int length) {
        
        this.reference = reference;
        this.description = description;
        this.depth = depth;
        this.height = height;
        this.length = length;
        this.volume = depth * height * length;
    }
    
    
    /**
     * Getter for item reference that acts as unique identifier for the item
     * 
     * @return item reference
     */
    @Override
    public String getReference() {
        
        return this.reference;
    }

    /**
     * Getter for item description
     * 
     * @return item description
     */
    @Override
    public String getDescription() {
        
        return this.description;
    }

    /**
     * Setter for the item description
     * 
     * @param description a textual description for the item 
     */
    @Override
    public void setDescription(String description) {
        
        this.description = description;
    }

    /**
     * Getter of depth
     * 
     * @return the depth value
     */
    @Override
    public int getDepth() {
        
        return this.depth;
    }

    /**
     * Getter of height
     * 
     * @return the height value
     */
    @Override
    public int getHeight() {
        
        return this.height;
    }

    /**
     * Getter of length
     * 
     * @return the length value
     */
    @Override
    public int getLenght() {
        
        return this.length;
    }

    /**
     * Getter of volume
     * 
     * @return the volume value
     */
    @Override
    public int getVolume() {
        
        return this.volume;
    }
    
    /**
     * Create the item's JSON notation representation
     * 
     * @return item's JSON notation representation
     */
    public String toJSONString() {
        
        return "{\n" +
               "\"reference\": \"" + this.reference + "\",\n" +
               "\"depth\": " + this.depth + ",\n" +
               "\"length\": " + this.length + ",\n" +
               "\"description\": \"" + this.description + "\",\n" +
               "\"height\": " + this.height + ",\n" +
                "}";
    }
}
