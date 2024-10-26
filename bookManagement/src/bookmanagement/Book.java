package bookmanagement;

import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.sql.Date;

public class Book {

    private Integer id;
    private String title;
    private String author;
    private String publiser;
    private Date publicYear;
    private String genre;
    private Integer quantity;
    private Double price;
    private String image;
    private Date rentalDay;

    public double getRental() {
        double rentalValue = (quantity != null && quantity != 0) ? (price / (quantity * 2) ) : 0.0;
        rentalValue = Math.round(rentalValue * 100.0) / 100.0;
        if(rentalValue > 20) rentalValue = 20;
        if(rentalValue < 2 ) rentalValue = 2;
        return rentalValue;
    }

    public Book(Integer id, String title, String author, String publiser, Date publicYear, String genre, Integer quantity, Double price, String image, Date rentalDay) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publiser = publiser;
        this.publicYear = publicYear;
        this.genre = genre;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.rentalDay = rentalDay;
    }

    public Book() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubliser() {
        return publiser;
    }

    public void setPubliser(String publiser) {
        this.publiser = publiser;
    }

    public Date getPublicYear() {
        return publicYear;
    }

    public void setPublicYear(Date publicYear) {
        this.publicYear = publicYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getRentalDay() {
        return rentalDay;
    }

    public void setRentalDay(Date rentalDay) {
        this.rentalDay = rentalDay;
    }
}
