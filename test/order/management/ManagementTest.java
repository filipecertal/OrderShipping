package order.management;

import order.packing.Color;
import order.packing.Container;
import order.packing.IItem;
import order.packing.Position;
import packing_gui.PackingGUI;


/**
 * Represent's a container for shipment
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class ManagementTest {
    
    public static void main(String[] args) throws Exception {
        
        Management management = new Management();
        
// TEST #1:

        System.out.println("TEST #1 : Loading order from file... ");
        
        // Import order's data from file...
        Order order = new Order();
        management.importData(order, "order.json");
        management.add(order);
        
// TEST #2:

        System.out.println("TEST #2 : Setting up containers... ");
    
        Container c1 = new Container("c1", Color.black, Color.blue),
                  c2 = new Container("c2", Color.black, Color.blue),
                  c3 = new Container("c3", Color.black, Color.blue);

        for(IItem item : order.getItems()) {

            if (item.getReference().equals("ITEM1")) {

                c2.addItem(item, new Position(4, 0, 0), Color.blue);
            }

            if (item.getReference().equals("ITEM2")) {

               c2.addItem(item, new Position(0, 3, 0), Color.blue);
            }
            
            if (item.getReference().equals("ITEM3")) {

               c3.addItem(item, new Position(0, 0, 0), Color.blue);
            }
                        
            if (item.getReference().equals("ITEM4")) {

                c2.addItem(item, new Position(0, 0, 0), Color.blue);
            }
                                    
            if (item.getReference().equals("ITEM5")) {

                c1.addItem(item, new Position(0, 0, 0), Color.blue);
            }
        }
        
        c1.close();
        c2.close();
        c3.close();

// TESTE #3:

        System.out.println("TEST #3 : Setting up shipping... ");
        
        for (ShipmentStatus s : ShipmentStatus.values()) {
            System.out.println(s + ", " + s.ordinal());
        }
        
         
        Shipping s1 = new Shipping(),
                 s2 = new Shipping();
        
        s1.setShipmentStatus(ShipmentStatus.IN_TREATMENT);
        s1.addContainer(c1);
        s1.addContainer(c2);
        s1.setShipmentStatus(ShipmentStatus.CLOSED);
        s1.setShipmentStatus(ShipmentStatus.SHIPPED);
        s1.setShipmentStatus(ShipmentStatus.RECEIVED);
        
        s2.setShipmentStatus(ShipmentStatus.IN_TREATMENT);
        s2.addContainer(c3);
        s2.setShipmentStatus(ShipmentStatus.CLOSED);

// TESTE #4:

        System.out.println("TEST #4 : Adding shippings to order... ");
        
        order.addShipping(s1);
        order.addShipping(s2);
        
// TESTE #5:        
        
        System.out.println("TEST #5 : Export and view order shipping graphics (GUI)... ");
        
        order.export();
        PackingGUI.render("export.json");       
         
    }
    
}
