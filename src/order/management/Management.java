package order.management;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import order.base.Address;
import order.base.Customer;
import order.base.ICustomer;
import order.base.Person;
import order.exceptions.ContainerException;
import order.exceptions.OrderException;
import order.exceptions.PositionException;
import order.packing.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Management module (API)
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Management implements IManagement, IOrderImporter {
    
    private Order[] orders;
    
    public Management() {
        
        this.orders = new Order[0];
    }
    

    /**
     * Adds a new IOrder to the order manager
     * 
     * @param order order to be added
     * 
     * @return
     * <ol>
     * <li>true if the order is inserted.</li>
     * <li>false if the order already exists in the container</li>
     * </ol>
     * 
     * @throws OrderException if order is null
     */
    @Override
    public boolean add(IOrder order) throws OrderException {
           
        if (order == null) {
            
            throw new OrderException() {
            
                @Override
                public String getMessage() {
                    return "Order object is null";
                }
            };
        }
        
        for (Order o : this.orders) {
            
            if (o.equals(order)) {
                
                return false;
            }
        }
        
        this.orders = Arrays.copyOf(this.orders, this.orders.length + 1);
        this.orders[this.orders.length - 1] = (Order) order;

        return true;   
    }

    @Override
    public boolean remove(IOrder order) throws OrderException {
        
        if (order == null) {
            
            throw new OrderException() {
            
                @Override
                public String getMessage() {
                    return "Order object is null";
                }
            };
        }
        
        int i;
        
        for (i = 0; i < this.orders.length && !order.equals(this.orders[i]); i++);
        
        if (i >= this.orders.length) return false;
        
        for (int j = i; i < this.orders.length - 1; j++)
            this.orders[j] = this.orders[j+1];
        
        this.orders = Arrays.copyOf(this.orders, this.orders.length - 1);
        return true;
    }

    @Override
    public IOrder[] getOrders(ICustomer customer) {
        
        Order[] r = new Order[this.orders.length];
        int pos = 0;
        
        for (Order o : this.orders) {
            
            if (o.getCustomer().getCustomerId() == customer.getCustomerId()) {
                r[pos++] = o;
            }
            
        }
        
        return Arrays.copyOf(r, pos);
    }

    @Override
    public IOrder[] getOrders() {
        
        return Arrays.copyOf(this.orders, this.orders.length);
    }

    @Override
    public void importData(IOrder order, String path) throws IOException, ParseException, ContainerException, OrderException, PositionException {
        
        JSONParser parser = new JSONParser();
        
        Reader reader = new FileReader(path);
        JSONObject jsonOrder = (JSONObject) parser.parse(reader);
        
        // Get the orderID
        int orderId =  (int)(long)jsonOrder.get("id");
        
        order.setId(orderId);
        
        // Get the order date
        JSONObject jsonDate = (JSONObject) jsonOrder.get("date");
        int day = (int)(long)jsonDate.get("day"),
            month = (int)(long)jsonDate.get("month"),
            year = (int)(long)jsonDate.get("year");
        
        order.setDate(day, month, year);
        
        // Get destination
        JSONObject jsonDestination = (JSONObject) jsonOrder.get("destination");
        JSONObject jsonAddress = (JSONObject) jsonDestination.get("address");
        Address address = new Address(
                (String)jsonAddress.get("street"),
                (String)jsonAddress.get("city"),
                (String)jsonAddress.get("country"),
                (String)jsonAddress.get("state"),
                (int)(long)jsonAddress.get("number")
            );
        
        Person destination = new Person((String)jsonDestination.get("name"), address);
        
        order.setDestination(destination);

     
        // Get Customer
        JSONObject jsonCustomer = (JSONObject) jsonOrder.get("customer");
        
        // Parse Address from customer
        jsonAddress = (JSONObject) jsonCustomer.get("address");
        address = new Address(
                (String)jsonAddress.get("street"),
                (String)jsonAddress.get("city"),
                (String)jsonAddress.get("country"),
                (String)jsonAddress.get("state"),
                (int)(long)jsonAddress.get("number")
            );
        
        // Parse the Billing Address
        JSONObject jsonBillingAddress = (JSONObject) jsonCustomer.get("billingAddress");
        Address billingAddress = new Address(
                (String)jsonBillingAddress.get("street"),
                (String)jsonBillingAddress.get("city"),
                (String)jsonBillingAddress.get("country"),
                (String)jsonBillingAddress.get("state"),
                (int)(long)jsonBillingAddress.get("number")
            );
        
        Customer customer = new Customer(
                (String)jsonCustomer.get("name"),
                address,
                billingAddress
            );
        
        order.setCustomer(customer);
        
        // Get items        
        JSONArray jsonItems = (JSONArray) jsonOrder.get("items");
        
        for (int i = 0; i < jsonItems.size(); i++) {
            
            JSONObject jsonItem = (JSONObject) jsonItems.get(i);
            
            String reference =  (String) jsonItem.get("reference");
            String description =  (String) jsonItem.get("decription");
            int depth = (int)(long) jsonItem.get("depth"),
                length = (int)(long) jsonItem.get("length"),
                height = (int)(long) jsonItem.get("height");
           
            order.add(new Item(reference, description, depth, height, length));

        }
        
    }
    
    public void ExportCustomersChart() throws IOException {
        
        BufferedWriter writer;
        
        String[] customerNames = new String[0];
        int[] customerOrders = new int[0];
         
        for (Order order: this.orders) {
            
            int id = order.getCustomer().getCustomerId();
            String name = order.getCustomer().getName();
            
            if (id >= customerOrders.length) {
                
                customerOrders = Arrays.copyOf(customerOrders, id + 1);
                customerNames = Arrays.copyOf(customerNames, id + 1);
                customerOrders[id] = 1;
                customerNames[id] = name;
                
            } else {
                
                customerOrders[id]++;
            }       
        }
        
        
        String chartC =   "{\n" + 
                "\"type\":\"bar\", \n" +
                "\"data\": {\n" + 
                "    \"labels\":[";
                
                
        String separador = "";

        for (int i = 0; i < customerOrders.length; i++) {

            if (customerOrders[i]>0)
               chartC += separador + "\"" + customerNames[i] + "\"";

            separador = ", ";

        }           
                 
        chartC += "],\n"+
                "    \"datasets\":[{\"label\":\"Número de encomendas\", \"data\":["; 
        
                
        separador = "";

        for (int i = 0; i < customerOrders.length; i++) {

            if (customerOrders[i]>0)
               chartC += separador + customerOrders[i];

            separador = ", ";

        }  
                       
        chartC += "]}]}, \n"+
            "\"title\": \"Encomendas por cliente\"" + 
            "}" ;

        // Save JSON to file
        writer = new BufferedWriter(new FileWriter("chartC.json"));
        writer.write(chartC);
        writer.close();
        
    }
    
}
