package order.base;

/**
 * Represent's a Customer
 * 
 * @author Filipe Certal (filipe.certal@gmail.com)
 */
public class Customer extends Person implements ICustomer {
    
    private static int customerCount = 1;
    
    private int customerId;
    private String vat;
    private Address billingAddress;
    
    public Customer(String name, Address address, Address billingAddress) {
        
        super(name, address);
        this.customerId = Customer.customerCount++;
        this.customerId = customerId;
        this.vat = vat;
        this.billingAddress = billingAddress;
    }
        
    @Override
    public int getCustomerId() {
        return this.customerId;
    }

    @Override
    public String getVat() {
        return this.vat;
    }

    @Override
    public void setVat(String string) {
        this.vat = string;
    }

    @Override
    public IAddress getBillingAddress() {
        return this.billingAddress;
    }

    @Override
    public void setBillingAddress(IAddress ia) {
        this.billingAddress = (Address) ia;
    }

    @Override
    public IAddress getAddress() {
        return super.getAddress();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setAddress(IAddress ia) {
        super.setAddress(ia);
    }

    @Override
    public void setName(String string) {
        super.setName(string);
    }
    
    public String toJSONString() {
        
        return "{ \"address\": " + ((Address) this.getAddress()).toJSONString() +
               ", \"name\": \"" + this.getName() +  "\"" + 
               ", \"id\": " + this.customerId +
               ", \"billingAddress\": " + this.billingAddress.toJSONString() +  " }";
    }
    
}
