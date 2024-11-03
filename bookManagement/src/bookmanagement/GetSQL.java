package bookmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GetSQL {

    public static Connection connectDb(){
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");


            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bookmanager?useSSL=false", "root", "0946628952p");
        }catch(Exception e){e.printStackTrace();}
        return null;
    }

    public static ObservableList<Book> getBook() throws SQLException {
        ObservableList<Book> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM book";

        Connection con = connectDb();
        assert con != null;
        PreparedStatement prepared = con.prepareStatement(sql);
        ResultSet result = prepared.executeQuery();
        Book book;
        while(result.next()) {
            book = new Book(result.getInt("id"),
                            result.getString("title"),
                            result.getString("author"),
                            result.getString("publiser"),
                            result.getDate("public_year"),
                            result.getString("genre"),
                            result.getInt("quantity"),
                            result.getDouble("price"),
                            result.getString("image")
                    );
            list.add(book);
        }
        return list;
    }

    public static ObservableList<Customer> getCustomer() throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer";

        Connection con = connectDb();
        assert con != null;
        PreparedStatement prepared = con.prepareStatement(sql);
        ResultSet result = prepared.executeQuery();
        Customer customer;
        while(result.next()) {
            customer = new Customer(result.getInt("customerId"),
                    result.getString("fullName"),
                    result.getString("phone"),
                    result.getString("address"),
                    result.getString("email")
                    );

            list.add(customer);
        }
        return list;
    }

    public static ObservableList<Rental> getRental() throws SQLException {
        ObservableList<Rental> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM rental";

        Connection con = connectDb();
        assert con != null;
        PreparedStatement prepared = con.prepareStatement(sql);
        ResultSet result = prepared.executeQuery();
        Rental rental;
        while(result.next()) {
            rental = new Rental(
                    result.getInt("id"),
                    result.getInt("customerId"),
                    result.getString("fullName"),
                    result.getString("phone"),
                    result.getInt("bookId"),
                    result.getString("title"),
                    result.getDouble("rentalPrice"),
                    result.getDate("rentalDate"),
                    result.getDate("dueDate"),
                    result.getDate("returnDate")
            );
            list.add(rental);
        }
        return list;
    }


}
