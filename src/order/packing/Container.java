package order.packing;

import order.exceptions.ContainerException;
import order.exceptions.PositionException;
import order.util.DinArray;

/**
 * Represent's a container for shipment
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Container implements IContainer {
    
            
    private static final int depth = 5, height = 5, length = 5, volume = 5;
        
    private DinArray<ItemPacked> items;
    
    private final String reference;
    private boolean closed;

    private final Color color, colorEdge;
        
    public Container(String reference, Color color, Color colorEdge) {
        
        this.color = color;
        this.colorEdge = colorEdge;
        
        this.items = new DinArray<>(ItemPacked.class);
        
        this.reference = reference;
        this.closed = false;
    }
    
    /**
     * Adds a new ItemPacked to the container considering the given item, position and color
     * 
     * @param item item to be added
     * @param position position in which the item will be placed
     * @param color color used for the item
     * @return <ol>
     * <li>true if the item is inserted in the container. When the item is inserted, 
     * the volume should be updated. If there isn't enough space in the collection, 
     * the collection should be adjusted to store more items</li>
     * <li>false if the item already exists in the container</li>
     * </ol>
     * @throws ContainerException if:
     * <ol>
     * <li>any parameter is null</li>
     * <li>the container is closed</li>
     * </ol>
     */
    @Override
    public boolean addItem(IItem item, IPosition position, Color color) throws ContainerException {
        
        // Throw a ContainerException if there is a null parameter
        if (item == null || position == null || color == null ) {
            
            new ContainerException() {
        
                @Override
                public String getMessage() {
                    return "trying to add an item with null parameter";
                }
            };
        }
        
        // Throw ContainerException if the container is closed
        if (this.isClosed()) {
            
            new ContainerException() {
        
                @Override
                public String getMessage() {
                    return "the container is closerd";
                }
            };
        }
        
        // Verify if the item doesn't exists in the container,
        // comparing it's reference with the other item's references
        for (ItemPacked ip : this.items) {
               
            if (ip.getItem().getReference().equals(item.getReference())) {
                
                return false;
            }
        }
        
        // Insert the item packed in the container
        this.items.add( new ItemPacked((Item)item, (Position)position, color, color) );
        
        return true; 
    }

    /**
     * Removes an item from the container
     * 
     * @param item to be removed
     * @return
     * <ol>
     * <li>true if the item is removed in the container. When the item is removed, the volume should be updated.</li>
     * <li>false if the item doesn't exists in the container</li>
     * </ol>
     * @throws ContainerException if:
     * <ol>
     * <li>the parameter is null</li>
     * <li>the container is closed</li>
     * </ol>
     */
    @Override
    public boolean removeItem(IItem item) throws ContainerException {
        
        // Throw a ContainerException if there is a null parameter
        if (item == null) {
            
            new ContainerException() {
        
                @Override
                public String getMessage() {
                    return "trying to add an item with null parameter";
                }
            };
        }
        
        // Throw ContainerException if the container is closed
        if (this.isClosed()) {
            
            new ContainerException() {
        
                @Override
                public String getMessage() {
                    return "the container is closerd";
                }
            };
        }
        
        // Verify if the item does exists in the container,
        // comparing it's reference with the other item's reference
        for (ItemPacked ip : this.items) {
               
            if (ip.getItem().getReference().equals(item.getReference())) {
                
                // Remove the item from list.
                return this.items.remove(ip);
            }
        }
        
        return false;
    }

    /**
     * Validates the container structure considering:
     * <ol>
     * <li>if the volume if lesser or equal to the current volume</li>
     * <li>if all items are inside the container</li>
     * <li>if none of the items inside the container are overlapping</li>
     * </ol>
     * 
     * @throws ContainerException if the volume greater than the current volume
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item
     */
    @Override
    public void validate() throws ContainerException, PositionException {
        
        // Validate occupied volume if is lesser than the container volume
        if (this.getOccupiedVolume() > Container.volume) {
            
            new ContainerException() {
        
                @Override
                public String getMessage() {
                    return "the occupied volume is greater than the container's volume";
                }
            };    
        }
        
        // Validate if all items are inside the container
        
        int maxX = 0, maxY = 0, maxZ = 0;
        
        for (ItemPacked item : this.items) {
            
            if (item.getPosition().getX() > maxX) {
                
                maxX = item.getPosition().getX();
            }
            
            if (item.getPosition().getY() > maxY) {
                
                maxY = item.getPosition().getY();
            }
            
            if (item.getPosition().getZ() > maxZ) {
                
                maxZ = item.getPosition().getZ();
            }
        }
        
        if (maxX > Container.length || maxY > Container.height || maxZ > Container.depth) {
            
            new PositionException() {
        
                @Override
                public String getMessage() {
                    return "some item is outside the container's limits";
                }
            }; 
        }
             
        // Validate if theren't overlaped items in the container
        // TODO: overlaping validation
        
        
    }

    /**
     * Close the container. Before closing the container, a validation procedure is performed.
     * 
     * @throws ContainerException if the volume greater than the current volume
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item
     */
    @Override
    public void close() throws ContainerException, PositionException {
        
        this.validate();
        this.closed = true;
    }

    /**
     * Returns a item with a given reference
     * 
     * @param reference (unique identifier) of the item
     * @return item with a given reference or null if doesn't exist
     */
    @Override
    public IItem getItem(String reference) {

        for (ItemPacked item : this.items) {
            
            if (item.getItem().getReference().equals(reference)) {
                
                return item.getItem();
            }       
        }
        
        return null;
    }

    /**
     * Return the occupied volume in the container
     * 
     * @return Return the occupied volume in the container
     */
    @Override
    public int getOccupiedVolume() {
        int occupied = 0;
        
        for (ItemPacked item : this.items) {
            
            occupied += item.getItem().getVolume();
        }
        
        return occupied;
    }

    /**
     * Returns a copy of the collection (without null positions) for the items packed in the container
     * 
     * @return the items packed in the container
     */
    @Override
    public IItemPacked[] getPackedItems() {
        
        return (IItemPacked[]) this.items.toArray();
    }

    /**
     * Getter container reference that acts as unique identifier for the container
     * 
     * @return container reference
     */
    @Override
    public String getReference() {
        
        return this.reference;
    }

    /**
     * Returns the number of items in the container
     * 
     * @return number of items in the container
     */
    @Override
    public int getNumberOfItems() {
        
        return this.items.size();
    }

    /**
     * Return the remaining volume in the container
     * 
     * @return remaining volume in the container
     */
    @Override
    public int getRemainingVolume() {
        
        return Container.volume - this.getOccupiedVolume();
    }

    /**
     * Return if the container is closed
     * 
     * @return true if the container is closed, false if the container is not closed
     */
    @Override
    public boolean isClosed() {
        
        return this.closed;
    }

    /**
     * Getter of depth
     * 
     * @return the depth value
     */
    @Override
    public int getDepth() {
        
        return Container.depth;
    }

    /**
     * Getter of height
     * 
     * @return the depth height
     */
    @Override
    public int getHeight() {
        
        return Container.height;
    }

    /**
     * Getter of length
     * 
     * @return the depth length
     */
    @Override
    public int getLenght() {
        
        return Container.length;
    }

    /**
     * Getter of volume
     * 
     * @return the depth volume
     */
    @Override
    public int getVolume() {
        
        return Container.volume;
    }

    /**
     * Getter for the container color
     * 
     * @return container Color
     */
    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Getter for the container's edge color
     * 
     * @return container's edge Color
     */
    @Override
    public Color getColorEdge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Setter for the container Color
     * 
     * @param color container Color
     */
    @Override
    public void setColor(Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Setter for the conatainer's edge Color
     * 
     * @param color container's edge Color
     */
    @Override
    public void setColorEdge(Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Create the container's packed JSON notation representation
     * 
     * @return container's packed JSON notation representation
     */
    public String toJSONString() {
       
        String json =  "{ " +
                "\"volume\": " + Container.volume + ", " +   
                "\"reference\": \"" + this.reference + "\", " +
                "\"depth\": " + Container.depth + ", " +
                "\"color\": \"" + this.color + "\", " +
                "\"length\": " + Container.length + ", " +
                "\"closed\": " + this.closed + ", " +
                "\"colorEdge\": \"" + this.colorEdge + "\", " +
                "\"items\": [ ";
        
        String separator = "";
        
        for (ItemPacked item : this.items) {
            
            json += separator + item.toJSONString();
            
            separator = ", ";
        }
                
        return json + " ], " +
                "\"height\": " + Container.height + ", " +
                "\"occupiedVolume\": " + this.getOccupiedVolume() +
                " }";
    }
}
