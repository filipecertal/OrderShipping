package order.packing;

/**
 * Represent's an Item Packed on a Container, with a Position, Color and ColorEdge
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class ItemPacked implements IItemPacked {
    
    private final Item item;
    private Position position;
    private Color color, colorEdge;
    
    /**
     * 
     * Item's Packed instance constructor
     * 
     * @param item The item that's being packed
     * @param position The position (cartesian coordinates x, y and z)
     * @param color The item's pack color
     * @param colorEdge The item' pack edge color
     */
    public ItemPacked(Item item, Position position, Color color, Color colorEdge) {
        
        this.item = new Item(item.getReference(), item.getDescription(), item.getDepth(), item.getHeight(), item.getLenght());
        this.position = new Position(position.getX(), position.getY(), position.getZ());
        this.color = color;
        this.colorEdge = colorEdge;
    }

    /**
     * Getter for the Item packing configuration
     * 
     * @return Item packing configuration
     */
    @Override
    public IItem getItem() {
       
        return this.item;
    }

    /**
     * Getter for the packing Position
     * 
     * @return packing Position
     */
    @Override
    public IPosition getPosition() {
        
        return this.position;
    }

    /**
     * Setter for the packing Position
     * 
     * @param position packing Position
     */
    @Override
    public void setPosition(IPosition position) {
        
        this.position = (Position) position;
    }

    /**
     * Getter for the packing color
     * 
     * @return packing Color
     */
    @Override
    public Color getColor() {
        
        return this.color;
    }

    /**
     * Getter for the packing edge Color
     * 
     * @return packing edge Color
     */
    @Override
    public Color getColorEdge() {
        return this.colorEdge;
    }

    /**
     * Setter for the packing color
     * 
     * @param color packing Color
     */
    @Override
    public void setColor(Color color) {
        
        this.color = color;
    }

    /**
     * Setter for the packing edge Color
     * 
     * @param color packing Color
     */
    @Override
    public void setColorEdge(Color color) {
        
        this.colorEdge = color;
    }
    
    /**
     * Create the item's packed JSON notation representation
     * 
     * @return item's packed JSON notation representation
     */
    public String toJSONString() {
        
        return "{ " +
               "\"reference\": \"" + this.item.getReference() + "\", " +
               "\"depth\": " + this.item.getDepth() + ", " +
               "\"color\": \"" + this.color.toString() + "\", " +
               "\"x\": " + this.position.getX() + ", " +
               "\"length\": " + this.item.getLenght() + ", " +
               "\"y\": " + this.position.getY() + ", " +  
               "\"description\": \"" + this.item.getDescription() + "\", " +
               "\"z\": " + this.position.getZ() + ", " +
               "\"colorEdge\": \"" + this.colorEdge.toString() + "\", " +
               "\"height\": " + this.item.getHeight() + 
                " }";
    }  
}
