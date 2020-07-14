package order.management;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import order.base.Customer;
import order.base.ICustomer;
import order.base.IPerson;
import order.base.Person;
import order.exceptions.ContainerException;
import order.exceptions.OrderException;
import order.exceptions.PositionException;
import order.packing.Container;
import order.packing.IContainer;
import order.packing.IItem;
import order.packing.Item;
import order.util.DinArray;

/**
 * Represent's an Order
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Order implements IOrder, IExporter {
    
    private int orderId;
    private Person destination;
    private DinArray<Item> items;
    private LocalDate orderdate;
    private DinArray<Shipping> shippings;
    private Customer customer;
    
    public Order() {

        this.orderId = -1;
        this.destination = null;
        this.items = new DinArray<>(Item.class);
        this.orderdate = null;
        this.shippings = new DinArray<>(Shipping.class);
    }    

    /**
     * Getter for destination
     * 
     * @return destination - person
     */
    @Override
    public IPerson getDestination() {
        
        return this.destination;
    }

    /**
     * Setter for destination
     * 
     * @param person destination - person
     */
    @Override
    public void setDestination(IPerson person) {
        
        this.destination = (Person) person;
    }

    /**
     * Getter for customer who is responsible by the shipping order
     * 
     * @return customer who is responsible by the shipping order
     */
    @Override
    public ICustomer getCustomer() {
        
        return this.customer;
    }

    /**
     * Setter for order customer
     * 
     * @param customer customer - order customer
     */
    @Override
    public void setCustomer(ICustomer customer) {
        
        this.customer = (Customer) customer;
    }

    /**
     * Getter for a copy of the collection of items in the order
     * 
     * @return items from the order. The returned items should be a copy
     */
    @Override
    public IItem[] getItems() {
        
        return (IItem[]) this.items.toArray();
    }

    /**
     * Setter for order id
     * 
     * @param id order id
     */
    @Override
    public void setId(int id) {
        
        this.orderId = id;
    }

    /**
     * Getter for order id
     * 
     * @return order id
     */
    @Override
    public int getId() {
        
        return this.orderId;
    }

    /**
     * Setter for order date
     * 
     * @param day order day
     * @param month order month
     * @param year order year
     */
    @Override
    public void setDate(int day, int month, int year) {
        
        this.orderdate = LocalDate.of(year, month, day);
    }

    /**
     * Getter for order date
     * 
     * @return order date
     */
    @Override
    public LocalDate getDate() {
        
        return this.orderdate;
    }

    /**
     * Adds a new Item to the order
     * 
     * @param item item to be added
     * 
     * @return
     * <ol>
     * <li>true if the item is inserted.</li>
     * <li>false if the item already exists in the order</li>
     * </ol>
     * 
     * @throws OrderException if item is null
     */
    @Override
    public boolean add(IItem item) throws OrderException {
        
        if (item == null) {
            
            throw new OrderException() {
                      
                @Override
                public String getMessage() {
                    return "item is null";
                }
            
            };
        }
        
        // Verify if the item was already inserted
        for (Item i : this.items) {
            
            if (i.getReference().equals(item.getReference())) {
                
                return false;
            }
        }
        
        // Insert the item into the order list
        this.items.add((Item) item);
        
        return true;
    }

    /**
     * Get a copy of the shippings from the order
     * 
     * @return shipping details from the order
     */
    @Override
    public IShipping[] getShippings() {
        
        return (IShipping[]) this.shippings.toArray();
    }

    /**
     * Add a shipping to the order
     * 
     * @param shipping shipping details
     * 
     * @return
     * <ol>
     * <li>true if the shipping is inserted in the order. The number of shippings should limited to the number defined for each order</li>
     * <li>false if there isn't more space in the order</li>
     * </ol>
     * @throws OrderException if the order is closed or if @param shipping is null
     */
    @Override
    public boolean addShipping(IShipping shipping) throws OrderException {
        
        // Send an OrderException if shipping is null or the order is closed
        if (this.isClosed() || shipping == null) {
            
            throw new OrderException() {
            
                @Override
                public String getMessage() {
                    
                    return "Shipping value is null or the order is closed";
                }
            };
        }
        
        //TODO: Limit the number os shippings?
        
        // Insert the shipping     
        this.shippings.add((Shipping) shipping);
        return true;
    }

    /**
     * Remove a shipping from the order
     * 
     * @param shipping shipping details
     * 
     * @return
     * <ol>
     * <li>true if the shipping is removed in the order.</li>
     * <li>false if the shipping doesn't exist</li>
     * </ol>
     * 
     * @throws OrderException if the order is closed or if @param shipping is null
     */
    @Override
    public boolean removeShipping(IShipping shipping) throws OrderException {
        
        // Send an OrderException if shipping is null or the order is closed
        if (this.isClosed() || shipping == null) {
            
            throw new OrderException() {
            
                @Override
                public String getMessage() {
                    
                    return "Shipping value is null or the order is closed";
                }
            };
        }
        
        for (IShipping s : this.shippings) {
            
            if (s.equals(shipping)) {
                this.shippings.remove((Shipping) shipping);
                
                return true;
            }
            
        }
        
        return false;
    }

    /**
     * Remove all cancelled shippings from the order
     * 
     * @return Returns the number of cancelled shippings
     */
    @Override
    public int clean() {
        
        int count = 0;
        
        for (Shipping shipping : this.shippings) {
            
            if (shipping.getShipmentStatus() == ShipmentStatus.CANCELLED) {
                 
                this.shippings.remove(shipping);
                count++;
            }
        }
        
        return count;
    }

    /**
     * Validate if all containers are valid, if all items are placed inside containers, and if the same item is not placed inside multiple containers.
     * 
     * @throws OrderException if items are not placed into containers, and if the same item is placed inside multiple containers
     * @throws ContainerException if the volume greater than the current volume
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item
     */
    @Override
    public void validate() throws OrderException, ContainerException, PositionException {
        
        
        // TODO: Verify multiple items in various containers?
        
        for (IShipping shipping : this.shippings) {
            
            for(IContainer container : shipping.getContainers()) {
                
                ((Container) container).validate();
            }
        }
    }

    /**
     * Close the order. Before closing a validation procedure is performed. And, verifies if all items were RECEIVED by the destination. The shippings not RECEIVED must be CANCELLED.
     * 
     * @throws OrderException if items are not placed into containers, and if the same item is placed inside multiple containers
     * 
     * @throws ContainerException if the volume greater than the current volume
     * 
     * @throws PositionException if some item is outside (or is overflowing) the container or if some item is overlapping with other item
     */
    @Override
    public void close() throws OrderException, ContainerException, PositionException {
        
        this.validate();
        
        for (Shipping shipping : this.shippings) {
            
            if (shipping.getShipmentStatus() != ShipmentStatus.RECEIVED)
                shipping.setShipmentStatus(ShipmentStatus.CANCELLED);
        }    
    }

    /**
     * Return the closed status of the order
     * 
     * @return true if the order is closed, false if the order is not closed
     */
    @Override
    public boolean isClosed() {
        
        return this.getNumberOfRemaingItemsToSend() == 0;
    }

    /**
     * Returns the order cost as the sum of the RECEIVED shipping orders costs
     * 
     * @return Returns the cost
     */
    @Override
    public double getCost() {
        
        double cost = 0;
        
        for (IShipping shipping : this.shippings) {
            
            if (shipping.getShipmentStatus() == ShipmentStatus.RECEIVED) {
                
                cost += shipping.getCost();
            }
        }
        
        return cost;
    }

    /**
     * Returns a string representation with a summary of all information relative to this order
     * 
     * @return a string representation with a summary
     */
    @Override
    public String summary() {
        
        String s;
        
        s = "OrderID: " + this.orderId + 
            ", CustomerName: " + this.customer.getName() +
            ", DestinationName: " + this.destination.getName();
        
        return s;
    }

    /**
     * Returns the number of items of the order
     * 
     * @return Returns the number of items
     */
    @Override
    public int getNumberOfItems() {
        
        return this.items.size();
    }

    @Override
    public int getNumberOfRemaingItemsToSend() {
        
        int count = 0;

        for (Item item : this.items) {
            
            boolean in = false;
            
            for (Shipping shipping : this.shippings) {
                for (IContainer container : shipping.getContainers()) { 
                    
                    in = container.getItem(item.getReference()) != null;
                    if (in) break;
                }

                if (in) {
                    
                    if (shipping.getShipmentStatus() != ShipmentStatus.RECEIVED &&
                            shipping.getShipmentStatus() != ShipmentStatus.SHIPPED) {
                        
                        count++;  
                    }
                    
                    break;
                }  
            }
                
            if (!in) count++;
        }

        return count;
    }

    @Override
    public IItem[] getRemainingItemsToSend() {
        
        DinArray<IItem> a = new DinArray<>(IItem.class);
        
                for (Item item : this.items) {
            
            boolean in = false;
            
            for (Shipping shipping : this.shippings) {
                for (IContainer container : shipping.getContainers()) { 
                    
                    in = container.getItem(item.getReference()) != null;
                    if (in) break;
                }

                if (in) {
                    
                    if (shipping.getShipmentStatus() != ShipmentStatus.RECEIVED &&
                            shipping.getShipmentStatus() != ShipmentStatus.SHIPPED) {
                        
                        a.add(item);
                    }
                    
                    break;
                }  
            }
                
            if (!in) a.add(item);
        }

        return a.toArray();    
    }
    
    public String toJSONString() {
        
        if (this.orderId == -1) return "{}";
        
        String json = "{ \"orderId\": " + this.orderId + ", " +
               "\"destination\": " + this.destination.toJSONString() + ", " +
               "\"customer\": " + this.customer.toJSONString() +                
               " }";
        
        return json;
    }
    
    @Override
    public void export() throws IOException {
        
        String export = "{";
        
        // Order Date
        
        export += "\"date\": { \"month\": " + this.orderdate.getMonthValue() + 
                  ", \"year\": " + this.orderdate.getYear() + ", \"day\": " +
                  this.orderdate.getDayOfMonth() + "}, ";
        
        // Shippings  
        export += "\"shippings\": [";
            
        String separador = "";
        
        for (Shipping shipping : this.shippings) {
            
            export += separador + "{\"shipping\": " + shipping.toJSONString() + "}";
            separador = ", ";
        }
  
        export += "], ";
        
        // Destination
        
        export += "\"destination\": " + this.destination.toJSONString() + ", ";
        
        // Order ID
        export += "\"id\": " + this.orderId + ", ";
        
        // Customer
        
        export += "\"customer\": " + this.customer.toJSONString();
        
        export +="}";
        
        
        // Save JSON to file
        BufferedWriter writer = new BufferedWriter(new FileWriter("export.json"));
        writer.write(export);
        writer.close();
        
        //System.out.println(export);
        
        int totalItems = this.getNumberOfItems();
        int itemsNaoEnviados = this.getNumberOfRemaingItemsToSend();
        
        String chartA = "{\n" + 
                "\"type\":\"pie\", \n" +
                "\"data\": {\n" + 
                "    \"labels\":[\"Percentagem de items não enviados\",\"Percentagem de items enviados\"],\n"+
                "    \"datasets\":[{\"data\":["+ ((double)itemsNaoEnviados/totalItems) + ","+ (1 - (double)itemsNaoEnviados/totalItems) + "]}]}, \n"+
                "\"title\": \"Items enviados\"" + 
                "}" ;

        // Save JSON to file
        writer = new BufferedWriter(new FileWriter("chartA.json"));
        writer.write(chartA);
        writer.close();
        
        String chartB =  "{\n" + 
                "\"type\":\"bar\", \n" +
                "\"data\": {\n" + 
                "    \"labels\":[\"Em aberto\",\"Fechadas\"],\n"+
                "    \"datasets\":[{\"label\":\"Número de encomendas\", \"data\":["+ (itemsNaoEnviados) + ","+ (totalItems - itemsNaoEnviados) + "]}]}, \n"+
                "\"title\": \"Encomendas por estado\"" + 
                "}" ;

        // Save JSON to file
        writer = new BufferedWriter(new FileWriter("chartB.json"));
        writer.write(chartB);
        writer.close();
        
        
    }
    
}
