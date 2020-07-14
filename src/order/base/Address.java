package order.base;


/**
 * Represent's an Address
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Address implements IAddress {
    
    private String street;
    private String city;
    private String country;
    private String state;
    private int number;
    
    public Address(String street, String city, String country, String state, int number) {
        
        this.street = street;
        this.city = city;
        this.country = country;
        this.state = state;
        this.number = number;
    }
    
    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String getStreet() {
        return this.street;
    }

    @Override
    public void setCity(String string) {
        this.city = string;
    }

    @Override
    public void setCountry(String string) {
        this.country = string;
    }

    @Override
    public void setNumber(int i) {
        this.number = i;
    }

    @Override
    public void setState(String string) {
        this.state =string;
    }

    @Override
    public void setStreet(String string) {
        this.street = string;
    }
    
    public String toJSONString() {
        return "{ " +
        "\"country\": \"" + this.country + "\", " +
        "\"number\": " + this.number + ", " +
        "\"street\": \"" + this.street + "\", " +
        "\"city\": \"" + this.city + "\", " +
        "\"state\": \"" + this.state + "\" }";
    }
    
    
}
