package order.management;

import order.exceptions.ContainerException;
import order.exceptions.OrderException;
import order.exceptions.PositionException;
import order.packing.Container;
import order.packing.IContainer;
import order.util.DinArray;

/**
 * Instance representing the Shipping order behaviour, this class stores a collection of containers.
 *
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
class Shipping implements IShipping {
    
    private static final double cost = 31.25;
    private ShipmentStatus status;
    private DinArray<Container> containers;
    
    public Shipping() {
        
        this.status = ShipmentStatus.AWAITS_TREATMENT;
        this.containers = new DinArray<>(Container.class);
    }

    /**
     * Adds a new container to the shipping order
     * 
     * @param container container to be added
     * 
     * @return true if container is inserted in the shipping order. false if container already exists in the shipping order
     * 
     * @throws OrderException if the shipping order status is not equal to IN_TREATMENT
     * 
     * @throws ContainerException
     * <ol>
     * <li>the parameter is null</li>
     * <li>the container is not closed</li>
     * </ol>
     */
    @Override
    public boolean addContainer(IContainer container) throws OrderException, ContainerException {
        
        // Send a ContainerException if container is null
        if (container == null) {
            
            throw new ContainerException() {
                
                @Override
                public String getMessage() {
                    
                    return "The container is null";
                }   
            };
        }
        
        // Send a ContainerException if the container is not closed
        if (!container.isClosed()) {
            
            throw new ContainerException() {
                
                @Override
                public String getMessage() {
                    
                    return "The container isn't closed";
                }
            };
        }
        
        // Send a OrderException if the shipping status is not equal to IN_TREATMENT
        if (this.status != ShipmentStatus.IN_TREATMENT) {
                
            throw new OrderException() {
                
                @Override
                public String getMessage() {
                    
                    return "The shipment isn't in treatment";
                }
            };
        }
        
        // Verify if the container isn't already inserted.
        for (Container c : this.containers) {
            
            if (c.getReference().equals(container.getReference())) {
                
                return false;
            }
        }
        
        // Insert the container in shipping
        this.containers.add((Container)container);
        
        return true;
    }

    /**
     * Removes a container from the shipping order
     * 
     * @param container container to be removed
     * 
     * @return
     * <ol>
     * <li>true container is removed in the shipping order.</li>
     * <li>false container doesn't exists in the shipping order.</li>
     * </ol>
     * 
     * @throws OrderException if the shipping order status is not equal to IN_TREATMENT
     * @throws ContainerException if the parameter is null
     */
    @Override
    public boolean removeContainer(IContainer container) throws OrderException, ContainerException {
        
        // Send a ContainerException if container is null
        if (container == null) {
            
            throw new ContainerException() {
                
                @Override
                public String getMessage() {
                    
                    return "The container is null";
                }   
            };
        }
        
        // Send a OrderException if the shipping status is not equal to IN_TREATMENT
        if (this.status != ShipmentStatus.IN_TREATMENT) {
                
            throw new OrderException() {
                
                @Override
                public String getMessage() {
                    
                    return "The shipment isn't in treatment";
                }
            };
        }
        
        // Verify if the container isn't already inserted.
        for (Container c : this.containers) {
            
            if (c.getReference().equals(container.getReference())) {
                                 
                // Insert the container in shipping
                return this.containers.remove((Container) container);
            }
        }

        return false; 
    }

    /**
     * Checks if the container exists in the shipping order
     * 
     * @param container to check existence
     * 
     * @return true if container exists in the shipping order. false if the container does not exists in the shipping order
     */
    @Override
    public boolean existsContainer(IContainer container) {
        
        // Verify if the container isn't already inserted.
        for (Container c : this.containers) {
            
            if (c.getReference().equals(container.getReference())) {
                                 
                return true;
            }
        }
        
        return false;
    }

    /**
     * Searches for a given container based on its reference
     * 
     * @param reference to find container
     * 
     * @return the container with a given reference. Returns null if the container does not exists.
     */
    @Override
    public IContainer findContainer(String reference) {
        
        for (Container c : this.containers) {
            
            if (c.getReference().equals(reference)) {
                                 
                return c;
            }
        }

        return null;
    }

    /**
     * Getter for order status
     * 
     * @return order status
     */
    @Override
    public ShipmentStatus getShipmentStatus() {
        
        return this.status;
    }

    /**
     * Setter for status. A specific order for status should be preserved:
     * <ol>
     * <li>if the @param status is IN_TREATMENT then the status should be: AWAITS_TREATMENT</li>
     * <li>if the @param status is CLOSED then the status should be: IN_TREATMENT and the number of containers in the shipping order should be greater than 0. Additionally, to close the shipping order, the shipping order should be validated</li>
     * <li>if the @param status is SHIPPED then the order status should be: CLOSED</li>
     * <li>if the @param status is RECEIVED then the order status should be: SHIPPED</li>
     * <li>if the @param status is CANCELLED then the order status any status other than RECEIVED</li>
     * </ol>
     * 
     * @param status represents the order status to change the status
     * 
     * @throws OrderException if the current status is not compatible with the status @param status to change
     * 
     * @throws ContainerException if the volume greater than the current volume (when shipping order is validated)
     * 
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item (when shipping order is validated)
     */
    @Override
    public void setShipmentStatus(ShipmentStatus status) throws OrderException, ContainerException, PositionException {
             
        if ((this.status == ShipmentStatus.AWAITS_TREATMENT && status == ShipmentStatus.IN_TREATMENT) ||
            (this.status == ShipmentStatus.IN_TREATMENT && status == ShipmentStatus.CLOSED && this.containers.size() > 0) ||
            (this.status == ShipmentStatus.CLOSED && status == ShipmentStatus.SHIPPED) ||
            (this.status == ShipmentStatus.SHIPPED && status == ShipmentStatus.RECEIVED) ||
            (this.status != ShipmentStatus.RECEIVED && status == ShipmentStatus.CANCELLED)) {
            
            if (status == ShipmentStatus.CLOSED) {
                this.validate();
            }            
            
            this.status = status;
            
        } else {
            throw new OrderException () {
              
                @Override
                public String getMessage() {
                 
                    return "New status is not compatible with current order status";
                }
            };
        }     
    }

    /**
     * Getter for a copy of the collection of containers
     * 
     * @return a copy of the collection of containers Returns an array (without null positions) for the containers in the shipping order
     */
    @Override
    public IContainer[] getContainers() {
        
        return (IContainer[]) this.containers.toArray();
    }

    /**
     * Checks if any container is invalid
     * 
     * @throws ContainerException if the volume greater than the current volume
     * 
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item
     */ 
    @Override
    public void validate() throws ContainerException, PositionException {
        
        for (Container container : this.containers) {
            
            container.validate();
        }
    }

    /**
     * Returns a string representation with a summary of the existing containers and their items
     * 
     * @return a string representation with a summary of the existing containers and their items
     */
    @Override
    public String summary() {
        
       return "TODO: Summary";
    }

    /**
     * Returns the shipping cost of the containers as the volume containers multiplied by the price per cubic volume unit defined by Management
     * 
     * @return Returns the cost of the containers
     */
    @Override
    public double getCost() {
        
        return Shipping.cost * this.containers.size();
    }
    
    /**
     * Create the item's JSON notation representation
     * 
     * @return item's JSON notation representation
     */
    public String toJSONString() {
        
        String json =   "{ " + 
                        "\"cost\": " + this.getCost() + ", " +
                        "\"containers\": [";
        
        String separator = "";
        
        for (Container container : this.containers) {
            
            json += separator + container.toJSONString();
            separator = ", ";
        }
        
        json += "], \"status\": \"" + this.status.toString() +"\"";
        
        return json + " }";
    }
    
}
