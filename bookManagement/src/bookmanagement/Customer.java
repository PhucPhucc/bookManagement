package bookmanagement;

public class Customer {

    private Integer customerId;
    private String fullName;
    private String phone;
    private String address;
    private String email;

    public Customer(Integer customerId, String fullName, String phone, String address, String email) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
