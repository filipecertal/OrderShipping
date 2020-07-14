package order.base;

/**
 * Represent's a Person
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Person implements IPerson {
    
    private String name;
    private Address address;

    public Person(String name, Address address) {
        
        this.name = name;
        this.address = address;
    }

    @Override
    public IAddress getAddress() {
        return this.address;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setAddress(IAddress ia) {
        this.address = (Address) ia;
    }

    @Override
    public void setName(String string) {
        this.name = string;
    }
    
    public String toJSONString() {
        return "{ \"address\": " + this.address.toJSONString() + ", " + "\"name\": \"" + this.name +"\" }";
    }
    
}
