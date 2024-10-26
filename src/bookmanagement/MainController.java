package bookmanagement;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane main;

    @FXML
    private Button home;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AnchorPane book_form;

    @FXML
    private AnchorPane rental_form;

    @FXML
    private Button bookSotorage;

    @FXML
    private Button rentalManagement;

    @FXML
    private Button logOut;

    @FXML
    private Label numOfBooks;

    @FXML
    private Label numOfRental;

    @FXML
    private StackedAreaChart<?, ?> chartBook;

    @FXML
    private TextField searchBox;

    @FXML
    private TableView<Book> bookDetail;

    @FXML
    private TableColumn<Book, Integer> bookId;

    @FXML
    private TableColumn<Book, String> bookTitle;

    @FXML
    private TableColumn<Book, String> bookAuthor;

    @FXML
    private TableColumn<Book, String> bookPubliser;

    @FXML
    private TableColumn<Book, Date> bookPublicYear;

    @FXML
    private TableColumn<Book, String> bookGenre;

    @FXML
    private TableColumn<Book, Integer> bookQuantity;

    @FXML
    private TableColumn<Book, Double> bookPrice;

    @FXML
    private TextField inputBookId;

    @FXML
    private TextField inputTitle;

    @FXML
    private TextField inputAuthor;

    @FXML
    private TextField inputGenre;

    @FXML
    private TextField inputPubliser;

    @FXML
    private DatePicker inputPublicYear;

    @FXML
    private TextField inputQuantity;

    @FXML
    private TextField inputPrice;

    @FXML
    private ImageView importImg;

    @FXML
    private Button importImgBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button delBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button addBtn;

    @FXML
    private TextField inputManageId;

    @FXML
    private TextField inputManageTitle;

    @FXML
    private TextField inputManageQuantity;

    @FXML
    private TextField inputManagePrice;

    @FXML
    private TextField inputManageRental;

    @FXML
    private DatePicker inputManageDate;

    @FXML
    private TableView<Book> rentalDetail;

    @FXML
    private TableColumn<Book, Integer> manageBookId;

    @FXML
    private TableColumn<Book, String> manageTitle;

    @FXML
    private TableColumn<Book, Integer> manageQuantity;

    @FXML
    private TableColumn<Book, Double> managePrice;

    @FXML
    private TableColumn<Book, Double> manageRental;

    @FXML
    private TableColumn<Book, Date> manageIsRental;



    private ObservableList<Book> listBooks;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private String path;

    public ObservableList<Book> getBookData() {
        try {
            return GetSQL.getBook();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showListBook() {
        listBooks = this.getBookData();

        bookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPubliser.setCellValueFactory(new PropertyValueFactory<>("publiser"));
        bookPublicYear.setCellValueFactory(new PropertyValueFactory<>("publicYear"));
        bookGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        bookQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bookPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookDetail.setItems(listBooks);
    }

    public void selectBook() {
        Book book = bookDetail.getSelectionModel().getSelectedItem();
        int num = bookDetail.getSelectionModel().getSelectedIndex();

        if(num < 0) return;

        inputBookId.setText(String.valueOf(book.getId()));
        inputTitle.setText(book.getTitle());
        inputAuthor.setText(book.getAuthor());
        inputGenre.setText(book.getGenre());
        inputPubliser.setText(book.getPubliser());
        inputPublicYear.setValue(book.getPublicYear().toLocalDate());
        inputQuantity.setText(String.valueOf(book.getQuantity()));
        inputPrice.setText(String.valueOf(book.getPrice()));

        String url = String.format("file:%s", book.getImage());
        path = book.getImage();
        image = new Image(url, 150, 200, false, true);
        importImg.setImage(image);
    }

    public void insertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main.getScene().getWindow());

        if(file != null) {
            path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 150, 200, false, true);
            importImg.setImage(image);
        }
    }

    public void addListBook() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO book (id, title, author, publiser, public_year, genre, quantity, price, image)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if(inputBookId.getText().isEmpty()
                    || inputTitle.getText().isEmpty()
                    || inputAuthor.getText().isEmpty()
                    || inputPubliser.getText().isEmpty()
                    || inputPublicYear.getValue() == null
                    || inputGenre.getText().isEmpty()
                    || inputQuantity.getText().isEmpty()
                    || inputPrice.getText().isEmpty()
            ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String check = "SELECT id FROM book WHERE id = '"
                        + inputBookId.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book ID: " + inputBookId.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    assert connect != null;
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, inputBookId.getText());
                    prepare.setString(2, inputTitle.getText());
                    prepare.setString(3, inputAuthor.getText());
                    prepare.setString(4, inputPubliser.getText());
                    prepare.setDate(5, java.sql.Date.valueOf(inputPublicYear.getValue()));
                    prepare.setString(6, inputGenre.getText());
                    prepare.setInt(7, Integer.parseInt(inputQuantity.getText()));
                    prepare.setDouble(8, Double.parseDouble(inputPrice.getText()));

                    String uri = path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(9, uri);

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    showListBook();
                    resetListBook();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetListBook() {
        inputBookId.setText("");
        inputTitle.setText("");
        inputAuthor.setText("");
        inputPubliser.setText("");
        inputPublicYear.setValue(null);
        inputGenre.setText("");
        inputQuantity.setText("");
        inputPrice.setText("");

        importImg.setImage(null);
        path = "";

        inputManageId.setText("");
        inputManageTitle.setText("");
        inputManageQuantity.setText("");
        inputManagePrice.setText("");
        inputManageRental.setText("");
        inputManageDate.setValue(null);

    }

    public void updateListBook() {
        String uri = path.replace("\\", "\\\\"); // Chỉ cần sử dụng replace một lần

        String sql = "UPDATE book SET title = ?, author = ?, publiser = ?, public_year = ?, genre = ?, quantity = ?, price = ?, image = ? WHERE id = ?";

        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if (inputBookId.getText().isEmpty() ||
                    inputTitle.getText().isEmpty() ||
                    inputAuthor.getText().isEmpty() ||
                    inputPubliser.getText().isEmpty() ||
                    inputPublicYear.getValue() == null ||
                    inputGenre.getText().isEmpty() ||
                    inputQuantity.getText().isEmpty() ||
                    inputPrice.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Book ID: " + inputBookId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    // Prepare statement
                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setString(1, inputTitle.getText());
                        prepare.setString(2, inputAuthor.getText());
                        prepare.setString(3, inputPubliser.getText());
                        prepare.setDate(4, java.sql.Date.valueOf(inputPublicYear.getValue())); // Sử dụng java.sql.Date cho giá trị ngày
                        prepare.setString(5, inputGenre.getText());
                        prepare.setInt(6, Integer.parseInt(inputQuantity.getText()));
                        prepare.setDouble(7, Double.parseDouble(inputPrice.getText()));
                        prepare.setString(8, uri);
                        prepare.setInt(9, Integer.parseInt(inputBookId.getText()));

                        // Thực hiện cập nhật
                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            showListBook();
                            resetListBook();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("No book found with the provided ID.");
                            alert.showAndWait();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the book: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null && !connect.isClosed()) {
                    connect.close(); // Đóng kết nối nếu còn mở
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteListBook() {

        String sql = "DELETE FROM book WHERE id = '"
                + inputBookId.getText() + "'";

        connect = GetSQL.connectDb();

        try {

            Alert alert;
            if (inputBookId.getText().isEmpty() ||
                    inputTitle.getText().isEmpty() ||
                    inputAuthor.getText().isEmpty() ||
                    inputPubliser.getText().isEmpty() ||
                    inputPublicYear.getValue() == null ||
                    inputGenre.getText().isEmpty() ||
                    inputQuantity.getText().isEmpty() ||
                    inputPrice.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE book ID: " + inputBookId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    showListBook();
                    resetListBook();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void searchBook() {

        FilteredList<Book> filter = new FilteredList<>(listBooks, e -> true);

        searchBox.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateBookData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateBookData.getId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getTitle().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getAuthor().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getPubliser().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getPublicYear().toString().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getGenre().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getQuantity().toString().contains(searchKey)) {
                    return true;
                } else if (predicateBookData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Book> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(bookDetail.comparatorProperty());
        bookDetail.setItems(sortList);
    }

    public void selectRentalBook() {
        Book book = rentalDetail.getSelectionModel().getSelectedItem();
        int num = rentalDetail.getSelectionModel().getSelectedIndex();

        if(num < 0) return;

        inputManageId.setText(String.valueOf(book.getId()));
        inputManageTitle.setText(book.getTitle());
        inputManageQuantity.setText(String.valueOf(book.getQuantity()));
        inputManagePrice.setText(String.valueOf(book.getPrice()));

        inputManageRental.setText(String.valueOf(book.getRental()));
        if(book.getRentalDay() == null) {
            inputManageDate.setValue(null);
        } else {
            inputManageDate.setValue(book.getRentalDay().toLocalDate());

        }
    }

    public void showRentalListBook() {
        listBooks = this.getBookData();

        manageBookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        manageTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        manageQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        managePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        manageRental.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return new SimpleDoubleProperty(book.getRental()).asObject();
        });
        manageIsRental.setCellValueFactory(new PropertyValueFactory<>("rentalDay"));

        rentalDetail.setItems(listBooks);
    }

    public void updateRentalListBook() {

        String sql = "UPDATE book SET title = ?, quantity = ?, price = ?, rental_day = ? WHERE id = ?";

        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if (inputManageId.getText().isEmpty() ||
                    inputManageTitle.getText().isEmpty() ||
                    inputManageQuantity.getText().isEmpty() ||
                    inputManagePrice.getText().isEmpty() ||
                    inputManageRental.getText().isEmpty() ||
                    inputManageDate.getValue() == null)
            {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Book ID: " + inputManageId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    // Prepare statement
                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setString(1, inputManageTitle.getText());
                        prepare.setInt(2, Integer.parseInt(inputManageQuantity.getText()));
                        prepare.setDouble(3, Double.parseDouble(inputManagePrice.getText()));
                        prepare.setDate(4, java.sql.Date.valueOf(inputManageDate.getValue()));
                        prepare.setInt(5, Integer.parseInt(inputManageId.getText()));

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            showRentalListBook();
                            resetListBook();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("No book found with the provided ID.");
                            alert.showAndWait();
                        }
                    }
                }
            }

            showRentalListBook();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the book: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null && !connect.isClosed()) {
                    connect.close(); // Đóng kết nối nếu còn mở
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRentalListBook() {
        String sql = "DELETE FROM book WHERE id = '"
                + inputManageId.getText() + "'";

        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if (inputManageId.getText().isEmpty() ||
                    inputManageTitle.getText().isEmpty() ||
                    inputManageQuantity.getText().isEmpty() ||
                    inputManagePrice.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE book ID: " + inputManageId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    this.showRentalListBook();

                    resetListBook();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void returnRentalBook() {
        String sql = "UPDATE book set rental_day = ? WHERE id = ?";

        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if (inputManageDate.getValue() == null)
            {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Rental day is not empty");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to RETURN Book ID: " + inputManageId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setDate(1, null);
                        prepare.setInt(2, Integer.parseInt(inputManageId.getText()));

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            showRentalListBook();
                            resetListBook();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("No book found with the provided ID.");
                            alert.showAndWait();
                        }
                    }
                }
            }

            showRentalListBook();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("SQL Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the book: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null && !connect.isClosed()) {
                    connect.close(); // Đóng kết nối nếu còn mở
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchForm(ActionEvent e) {
        if(e.getSource() == home) {
            home_form.setVisible(true);
            book_form.setVisible(false);
            rental_form.setVisible(false);

            home.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            bookSotorage.setStyle("-fx-background-color:transparent;");
            rentalManagement.setStyle("-fx-background-color:transparent;");

            this.totalBooks();
            
        } else if(e.getSource() == bookSotorage) {
            home_form.setVisible(false);
            book_form.setVisible(true);
            rental_form.setVisible(false);

            bookSotorage.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            home.setStyle("-fx-background-color:transparent;");
            rentalManagement.setStyle("-fx-background-color:transparent;");
            this.showListBook();
            searchBook();
        } else if(e.getSource() == rentalManagement) {
            home_form.setVisible(false);
            book_form.setVisible(false);
            rental_form.setVisible(true);

            rentalManagement.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            bookSotorage.setStyle("-fx-background-color:transparent;");
            home.setStyle("-fx-background-color:transparent;");
            this.showRentalListBook();

        }
    }

    public void totalBooks() {
        listBooks = this.getBookData();
        int total = listBooks.size();

        int count = 0;
        for(Book list : listBooks) {
            if (list.getRentalDay() != null) count++;
        }

        numOfRental.setText(String.valueOf(count));
        numOfBooks.setText(String.valueOf(total));
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookDetail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rentalDetail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.totalBooks();
        this.showListBook();
        this.showRentalListBook();
    }




}
