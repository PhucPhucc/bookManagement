package bookmanagement;

import java.sql.Date;

public class Rental {

    private Integer id;
    private Integer customerId;
    private String fullName;
    private String phone;

    private Integer idBook;
    private String title;
    private Double rentalPrice;
    private Date rentalDate;
    private Date dueDate;
    private Date returnDate;

    public Rental(Integer id, Integer customerId, String fullName, String phone, Integer idBook, String title, Double rentalPrice, Date rentalDate, Date dueDate, Date returnDate) {
        this.id = id;
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.idBook = idBook;
        this.title = title;
        this.rentalPrice = rentalPrice;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(Double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }
}
