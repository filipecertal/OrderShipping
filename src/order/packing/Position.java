package order.packing;

import order.exceptions.PositionException;

/**
 * Represents the position (cartesian coordinates) behaviour for items
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Position implements IPosition {
    
    private int x, y, z;
    
    /**
     * Position's instance contructor
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Position(int x, int y, int z) {
        
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for x coordinate
     * 
     * @return x coordinate
     */
    @Override
    public int getX() {
        
        return this.x;
    }

    /**
     * Getter for y coordinate
     * 
     * @return y coordinate
     */
    @Override
    public int getY() {
        
        return this.y;
    }

    /**
     * Getter for z coordinate
     * 
     * @return z coordinate
     */
    @Override
    public int getZ() {
        
        return this.z;
    }

    /**
     * Setter for x coordinate
     * 
     * @param x coordinate value
     * @throws PositionException if x coordinate is lower than 0 (zero)
     */
    @Override
    public void setX(int x) throws PositionException {
        
        if (x < 0) {

            throw new PositionException() {
        
                @Override
                public String getMessage() {
                    return "x coordinate is lower than 0";
                }
            };
            
        } else {
            
            this.x = x;      
        }
    }

    /**
     * Setter for y coordinate
     * 
     * @param y coordinate value
     * @throws PositionException if y coordinate is lower than 0 (zero)
     */
    @Override
    public void setY(int y) throws PositionException {
        
        if (y < 0) {

            throw new PositionException() {
        
                @Override
                public String getMessage() {
                    return "x coordinate is lower than 0";
                }
            };
            
        } else {
            
            this.y = y;          
        }
    }

    /**
     * Setter for z coordinate
     * 
     * @param z coordinate value
     * @throws PositionException if z coordinate is lower than 0 (zero)
     */
    @Override
    public void setZ(int z) throws PositionException {
        
        if (z < 0) {

            throw new PositionException() {
        
                @Override
                public String getMessage() {
                    return "x coordinate is lower than 0";
                }
            };
            
        } else {
            
            this.z = z;     
        }
    }
}
