package bookmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GetSQL {

    public static Connection connectDb(){
        try{

            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bookmanager", "root", "0946628952p");
        }catch(Exception e){e.printStackTrace();}
        return null;
    }

    public static ObservableList<Book> getBook() throws SQLException {
        ObservableList<Book> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM book";

        Connection con = connectDb();
        PreparedStatement prepa = con.prepareStatement(sql);
        ResultSet result = prepa.executeQuery();
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
                            result.getString("image"),
                            result.getDate("rental_day")
                    );
            list.add(book);
        }
        return list;
    }

}
