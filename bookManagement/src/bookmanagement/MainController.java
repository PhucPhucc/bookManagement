package bookmanagement;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane main;

    @FXML
    private Button home;

    @FXML
    private Button bookStorage;

    @FXML
    private Button rentalManagement;

    @FXML
    private Button customerManagement;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label numOfBooks;

    @FXML
    private Label numOfRental;

    @FXML
    private BarChart<String, Number> chartBook;

    @FXML
    private AnchorPane book_form;

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
    private TextField inputQuantity;

    @FXML
    private TextField inputPrice;

    @FXML
    private DatePicker inputPublicYear;

    @FXML
    private ImageView importImg;

    @FXML
    private AnchorPane customer_form;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputPhone;

    @FXML
    private TextField inputAddress;

    @FXML
    private TextField inputEmail;

    @FXML
    private TableView<Customer> customerView;

    @FXML
    private TableColumn<Customer, Integer> customerId;

    @FXML
    private TableColumn<Customer, String> customerName;

    @FXML
    private TableColumn<Customer, String> customerPhone;

    @FXML
    private TableColumn<Customer, String> customerAddress;

    @FXML
    private TableColumn<Customer, String> customerEmail;

    @FXML
    private TextField searchCustomer;

    @FXML
    private AnchorPane rental_form;

    @FXML
    private TextField searchRental;

    @FXML
    private TableView<Rental> rentalDetail;

    @FXML
    private TableColumn<Rental, Integer> rentalId;

    @FXML
    private TableColumn<Rental, Integer> rentalCustomerId;

    @FXML
    private TableColumn<Rental, String> rentalName;

    @FXML
    private TableColumn<Rental, String> rentalPhone;

    @FXML
    private TableColumn<Rental, String> rentalTitleBook;

    @FXML
    private TableColumn<Rental, Double> rentalPrice;

    @FXML
    private TableColumn<Rental, Date> rentalDate;

    @FXML
    private TableColumn<Rental, Date> rentalDue;

    @FXML
    private TableColumn<Rental, Date> rentalReturn;

    @FXML
    private TextField inputRentalCustomerId;

    @FXML
    private TextField inputRentalName;

    @FXML
    private TextField inputRentalPhone;

    @FXML
    private TextField inputRentalPrice;

    @FXML
    private DatePicker inputRentalDate;

    @FXML
    private DatePicker inputDueDate;

    @FXML
    private Label labelBookId;

    @FXML
    private Label labelTitleBook;

    @FXML
    private TextField inputCustomerId;

    private ObservableList<Book> listBooks;
    private ObservableList<Customer> listCustomers;
    private ObservableList<Rental> listRental;
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
    public ObservableList<Customer> getCustomerData() {
        try {
            return GetSQL.getCustomer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Rental> getRentalData() {
        try {
            return GetSQL.getRental();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getChar() {
        chartBook.getData().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Title Book");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity");

        listBooks = this.getBookData();
        listRental = this.getRentalData();

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
        dataSeries1.setName("Number of books");

        int lenBook = listBooks.size();
        int[] totalBook = new int[lenBook];
        int[] totalRental = new int[lenBook];

        for(int i = 0; i < lenBook; i++) {
            totalRental[i] = 0;
            totalBook[i] += listBooks.get(i).getQuantity();
            for(Rental r : listRental) {
                if(Objects.equals(r.getIdBook(), listBooks.get(i).getId())) {
                    totalBook[i]++;
                    totalRental[i]++;
                }
            }
        }

        for(int i = 0; i < lenBook; i++) {
            dataSeries1.getData().add(new XYChart.Data<String, Number>(listBooks.get(i).getTitle(), totalBook[i]));
        }

        XYChart.Series<String, Number> dataSeries2 = new XYChart.Series<String, Number>();
        dataSeries2.setName("Number of rentals");

        for(int i = 0; i < lenBook; i++) {
            dataSeries2.getData().add(new XYChart.Data<String, Number>(listBooks.get(i).getTitle(), totalRental[i]));
        }

        chartBook.getData().add(dataSeries1);
        chartBook.getData().add(dataSeries2);

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

                    this.showListBook();
                    this.resetListData();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetListData() {
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

        inputCustomerId.setText("");
        inputName.setText("");
        inputPhone.setText("");
        inputAddress.setText("");
        inputEmail.setText("");

        labelBookId.setText("");
        labelTitleBook.setText("");
        inputRentalCustomerId.setText("");
        inputRentalName.setText("");
        inputRentalPhone.setText("");
        inputRentalPrice.setText("");
        inputRentalDate.setValue(null);
        inputDueDate.setValue(null);
    }

    public void updateListBook() {
        String uri = path;
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
                    inputPrice.getText().isEmpty() ||
                    uri.isEmpty()) {
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
                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setString(1, inputTitle.getText());
                        prepare.setString(2, inputAuthor.getText());
                        prepare.setString(3, inputPubliser.getText());
                        prepare.setDate(4, java.sql.Date.valueOf(inputPublicYear.getValue()));
                        prepare.setString(5, inputGenre.getText());
                        prepare.setInt(6, Integer.parseInt(inputQuantity.getText()));
                        prepare.setDouble(7, Double.parseDouble(inputPrice.getText()));
                        prepare.setString(8, uri);
                        prepare.setInt(9, Integer.parseInt(inputBookId.getText()));

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            this.showListBook();
                            this.resetListData();
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
        }
    }

    public void deleteListBook() {
        Alert alert;
        String delete = "DELETE FROM rental WHERE bookId = '" + inputBookId.getText() + "'";

        String sql = "DELETE FROM book WHERE id = '"+ inputBookId.getText() + "';";

        connect = GetSQL.connectDb();
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cofirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE book ID: " + inputBookId.getText() + "?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
            try {
                statement = connect.createStatement();
                statement.executeUpdate(delete);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();

            this.showListBook();
            this.resetListData();
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

                if(searchKey.matches("\\d+")) {
                    return predicateBookData.getId().toString().contains(searchKey);
                } else {
                    return predicateBookData.getTitle().toLowerCase().contains(searchKey);
                }

            });
        });

        SortedList<Book> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(bookDetail.comparatorProperty());
        bookDetail.setItems(sortList);
    }

    public double getRentalPrice(double price, int quantity) {
        double rentalValue = (quantity != 0) ? (price * 0.2 / quantity ) : 0.0;
        rentalValue = Math.round(rentalValue * 100.0) / 100.0;
        if(rentalValue > 20) rentalValue = 20;
        if(rentalValue < 2 ) rentalValue = 2;
        return rentalValue;
    }

    public void rentalBtn() {
        int num = bookDetail.getSelectionModel().getSelectedIndex();

        if(num < 0) return;

        home_form.setVisible(false);
        book_form.setVisible(false);
        rental_form.setVisible(true);

        rentalManagement.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
        bookStorage.setStyle("-fx-background-color:transparent;");
        home.setStyle("-fx-background-color:transparent;");

        double rentalPrice = getRentalPrice(Double.parseDouble(inputPrice.getText()), Integer.parseInt(inputQuantity.getText()));

        labelBookId.setText(inputBookId.getText());
        labelTitleBook.setText(inputTitle.getText());

        inputRentalPrice.setText(String.valueOf(rentalPrice));
        inputRentalDate.setValue(LocalDate.now());

        this.showListRental();
    }

    public void cancelBtn() {
        home_form.setVisible(false);
        book_form.setVisible(true);
        rental_form.setVisible(false);
        customer_form.setVisible(false);

        bookStorage.setStyle("-fx-background-color:#ccc;\n" +
                " -fx-text-fill:#000;");
        home.setStyle("-fx-background-color:transparent;");
        rentalManagement.setStyle("-fx-background-color:transparent;");
        customerManagement.setStyle("-fx-background-color:transparent;");

        this.showListBook();
        this.searchBook();
        this.resetListData();
    }

    public void showListCustomer() {
        listCustomers = this.getCustomerData();

        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        customerView.setItems(listCustomers);
    }

    public void selectCustomer() {
        Customer customer = customerView.getSelectionModel().getSelectedItem();
        int num = customerView.getSelectionModel().getSelectedIndex();

        if(num < 0) return;

        inputCustomerId.setText(String.valueOf(customer.getCustomerId()));
        inputName.setText(customer.getFullName());
        inputPhone.setText(customer.getPhone());
        inputAddress.setText(customer.getAddress());
        inputEmail.setText(customer.getEmail());

    }

    public void addListCustomer() {
        String sql = "INSERT INTO customer (customerId, fullName, phone, address, email)" +
                "VALUES (?, ?, ?, ?, ?)";
        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if(inputCustomerId.getText().isEmpty()
                    || inputName.getText().isEmpty()
                    || inputPhone.getText().isEmpty()
                    || inputAddress.getText().isEmpty()
                    || inputEmail.getText().isEmpty()
            ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String check = "SELECT customerID FROM customer WHERE customerID = '"
                        + inputCustomerId.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer ID: " + inputCustomerId.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    assert connect != null;
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, Integer.parseInt(inputCustomerId.getText()));
                    prepare.setString(2, inputName.getText());
                    prepare.setString(3, inputPhone.getText());
                    prepare.setString(4, inputAddress.getText());

                    if(inputEmail.getText().contains("@")) {
                        prepare.setString(5, inputEmail.getText());
                    } else {
                        prepare.setString(5, inputEmail.getText() + "@gmail.com");
                    }

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    this.showListCustomer();
                    this.resetListData();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateListCustomer() {
        String sql = "UPDATE customer SET fullName = ?, phone = ?, address = ?, email = ? WHERE customerId = ?";
        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if(inputCustomerId.getText().isEmpty()
                    || inputName.getText().isEmpty()
                    || inputPhone.getText().isEmpty()
                    || inputAddress.getText().isEmpty()
                    || inputEmail.getText().isEmpty()
            ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE customerId: " + inputCustomerId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    // Prepare statement
                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setString(1, inputName.getText());
                        prepare.setString(2, inputPhone.getText());
                        prepare.setString(3, inputAddress.getText());
                        if(inputEmail.getText().contains("@")) {
                            prepare.setString(4, inputEmail.getText());
                        } else {
                            prepare.setString(4, inputEmail.getText() + "@gmail.com");
                        }

                        prepare.setString(5, inputCustomerId.getText());

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            this.showListCustomer();
                            this.resetListData();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("No customer found with the selected.");
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
            alert.setContentText("An error occurred while updating the customer: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteListCustomer() {
        Alert alert;
        int id = customerView.getSelectionModel().getSelectedIndex() + 1;

        String sql = "DELETE FROM customer WHERE customerId = '"
                + id + "'";

        connect = GetSQL.connectDb();
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cofirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE customer ID: " + id + "?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
            try {
                statement = connect.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();

            this.showListCustomer();
            this.resetListData();
        }
    }

    public void setSearchCustomer() {

        FilteredList<Customer> filter = new FilteredList<>(listCustomers, e -> true);

        searchCustomer.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateCustomerData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(searchKey.matches("\\d+")) {
                    return predicateCustomerData.getPhone().toLowerCase().contains(searchKey);
                } else if(predicateCustomerData.getEmail().toLowerCase().contains(searchKey)) {
                    return true;
                } else return predicateCustomerData.getFullName().toLowerCase().contains(searchKey);
            });
        });

        SortedList<Customer> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(customerView.comparatorProperty());
        customerView.setItems(sortList);
    }

    public void getRetalPhone() {

        String sql = "SELECT customerId, fullName FROM customer WHERE phone = '" + inputRentalPhone.getText() + "'";
        connect = GetSQL.connectDb();
//
        String check = "SELECT phone FROM customer WHERE phone = '"
                + inputRentalPhone.getText() + "'";

        try {
            assert connect != null;
            statement = connect.createStatement();
            result = statement.executeQuery(check);

            if(result.next()) {
                PreparedStatement prepared = connect.prepareStatement(sql);
                ResultSet newResult = prepared.executeQuery();
                if(newResult.next()) {
                    inputRentalName.setText(newResult.getString("fullName"));
                    inputRentalCustomerId.setText(String.valueOf(newResult.getInt(("customerId"))));
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The phone number you entered could not be found");
                alert.showAndWait();
                inputRentalPhone.setText("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void getRetalCustomerId() {

        String sql = "SELECT fullName, phone FROM customer WHERE customerId = '" + inputRentalCustomerId.getText() + "'";
        connect = GetSQL.connectDb();
//
        String check = "SELECT customerId FROM customer WHERE customerId = '"
                + inputRentalCustomerId.getText() + "'";

        try {
            assert connect != null;
            statement = connect.createStatement();
            result = statement.executeQuery(check);

            if(result.next()) {
                PreparedStatement prepared = connect.prepareStatement(sql);
                ResultSet newResult = prepared.executeQuery();
                if(newResult.next()) {
                    inputRentalName.setText(newResult.getString("fullName"));
                    inputRentalPhone.setText(newResult.getString(("phone")));
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The CustomerId you entered could not be found");
                alert.showAndWait();
                inputRentalCustomerId.setText("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void showListRental() {
        listRental = this.getRentalData();

        rentalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        rentalCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        rentalName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        rentalPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        rentalTitleBook.setCellValueFactory(new PropertyValueFactory<>("title"));
        rentalPrice.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));
        rentalDate.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        rentalDue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        rentalReturn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        rentalDetail.setItems(listRental);
    }

    public void addListRental() {

        connect = GetSQL.connectDb();

        try {
            Alert alert;
            if(inputRentalCustomerId.getText().isEmpty()
                    || inputRentalName.getText().isEmpty()
                    || inputRentalPhone.getText().isEmpty()
                    || inputRentalPrice.getText().isEmpty()
                    || inputRentalDate.getValue() == null
                    || inputDueDate.getValue() == null
            ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String updateQuantitySQL = "UPDATE book SET quantity = ? WHERE id = '" + labelBookId.getText() + "'";

                String check = "SELECT quantity FROM book WHERE id = '"
                        + labelBookId.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    if(result.getInt("quantity") < 1) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("The product is out of stock, please choose another product");
                        alert.showAndWait();
                        this.rentalBtn();
                        return;
                    }

                }
                String sql = "INSERT INTO rental (customerId, fullName, phone, bookId, title, rentalPrice, rentalDate, dueDate)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                assert connect != null;
                prepare = connect.prepareStatement(sql);
                prepare.setInt(1, Integer.parseInt(inputRentalCustomerId.getText()));
                prepare.setString(2, inputRentalName.getText());
                prepare.setString(3, inputRentalPhone.getText());
                prepare.setInt(4, Integer.parseInt(labelBookId.getText()));
                prepare.setString(5, labelTitleBook.getText());
                prepare.setDouble(6, Double.parseDouble(inputRentalPrice.getText()));
                prepare.setDate(7, java.sql.Date.valueOf(inputRentalDate.getValue()));
                prepare.setDate(8, java.sql.Date.valueOf(inputDueDate.getValue()));

                prepare.executeUpdate();

                PreparedStatement prepareUpdate = connect.prepareStatement(updateQuantitySQL);
                prepareUpdate.setInt(1, result.getInt("quantity") - 1);

                prepareUpdate.executeUpdate();
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                this.showListRental();
                this.resetListData();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void selectRental() {
        Rental rental = rentalDetail.getSelectionModel().getSelectedItem();
        int num = rentalDetail.getSelectionModel().getSelectedIndex();

        if(num < 0) return;

        labelBookId.setText(String.valueOf(rental.getId()));
        labelTitleBook.setText(rental.getTitle());
        inputRentalCustomerId.setText(String.valueOf(rental.getCustomerId()));
        inputRentalName.setText(rental.getFullName());
        inputRentalPhone.setText(rental.getPhone());
        inputRentalPrice.setText(String.valueOf(rental.getRentalPrice()));
        inputRentalDate.setValue(rental.getRentalDate().toLocalDate());
        inputDueDate.setValue(rental.getDueDate().toLocalDate());

    }

    public void updateListRental() {
        Rental rental = rentalDetail.getSelectionModel().getSelectedItem();
        connect = GetSQL.connectDb();
        try {
            Alert alert;
            if(inputRentalCustomerId.getText().isEmpty()
                    || inputRentalName.getText().isEmpty()
                    || inputRentalPhone.getText().isEmpty()
                    || inputRentalPrice.getText().isEmpty()
                    || inputRentalDate.getValue() == null
                    || inputDueDate.getValue() == null
            ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE rental ID: " + rental.getId() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    String sql = "UPDATE rental SET customerId = ?, fullName = ?, phone = ?, rentalPrice = ?, rentalDate = ?, dueDate = ? WHERE id = ?";

                    try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                        prepare.setString(1, inputRentalCustomerId.getText());
                        prepare.setString(2, inputRentalName.getText());
                        prepare.setString(3, inputRentalPhone.getText());


                        prepare.setDouble(4, Double.parseDouble(inputRentalPrice.getText()));
                        prepare.setDate(5, java.sql.Date.valueOf(inputRentalDate.getValue()));
                        prepare.setDate(6, java.sql.Date.valueOf(inputDueDate.getValue()));
                        prepare.setInt(7, rental.getId());

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();
                            this.showListRental();
                            this.resetListData();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("No customer found with the selected.");
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
            alert.setContentText("An error occurred while updating the customer: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void deleteListRental() {
        Rental rental = rentalDetail.getSelectionModel().getSelectedItem();
        Alert alert;

        String sql = "DELETE FROM rental WHERE id = '"
                + rental.getId() + "'";

        connect = GetSQL.connectDb();
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cofirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE rental ID: " +  rental.getId() + "?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
            try {
                statement = connect.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();

            this.showListRental();
            this.resetListData();
        }
    }

    public void returnListRental() {
        Rental rental = rentalDetail.getSelectionModel().getSelectedItem();
        int num = rentalDetail.getSelectionModel().getSelectedIndex();
        if(num < 0) return;
        connect = GetSQL.connectDb();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return: " + rental.getTitle() + " is rented by " + rental.getFullName() + "?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            String sql = "UPDATE rental SET returnDate = ? WHERE id = ?";
            String updateQuantitySQL = "UPDATE book SET quantity = ? WHERE id = '" + rental.getIdBook() + "'";
            String getQuantitySQL = "SELECT quantity FROM book WHERE id = '" + rental.getIdBook() + "'";
            try {
                PreparedStatement prepare = connect.prepareStatement(sql);
                prepare.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                prepare.setInt(2, rental.getId());

                statement = connect.createStatement();
                result = statement.executeQuery(getQuantitySQL);
                result.next();
                PreparedStatement prepareUpdate = connect.prepareStatement(updateQuantitySQL);
                prepareUpdate.setInt(1, result.getInt("quantity") + 1);

                prepareUpdate.executeUpdate();

                int rowsAffected = prepare.executeUpdate();
                if (rowsAffected > 0) {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                    this.showListRental();
                    this.resetListData();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("No customer found with the selected.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void getTotalBook() {
        String totalBookSQL = "SELECT COUNT(id) FROM book";
        String totalRentalSQL = "SELECT COUNT(id) FROM rental";

        connect = GetSQL.connectDb();
        int countData = 0;
        try {
            assert connect != null;
            statement = connect.createStatement();
            result = statement.executeQuery(totalBookSQL);

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            numOfBooks.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTotalRental() {
        String totalRentalSQL = "SELECT COUNT(id) FROM rental";

        connect = GetSQL.connectDb();
        int countData = 0;
        try {
            assert connect != null;
            statement = connect.createStatement();
            result = statement.executeQuery(totalRentalSQL);

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            numOfRental.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setSearchRenTal() {

        FilteredList<Rental> filter = new FilteredList<>(listRental, e -> true);

        searchRental.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateCustomerData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();


                if(searchKey.matches("\\d+")) {
                    return predicateCustomerData.getPhone().contains(searchKey);
                }
                 else return predicateCustomerData.getFullName().toLowerCase().contains(searchKey);
            });
        });

        SortedList<Rental> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(rentalDetail.comparatorProperty());
        rentalDetail.setItems(sortList);
    }

    public void switchForm(ActionEvent e) {
        if(e.getSource() == home) {
            home_form.setVisible(true);
            book_form.setVisible(false);
            rental_form.setVisible(false);
            customer_form.setVisible(false);

            home.setStyle("-fx-background-color:#ccc;\n" +
                    " -fx-text-fill:#000;");
            bookStorage.setStyle("-fx-background-color:transparent;");
            rentalManagement.setStyle("-fx-background-color:transparent;");
            customerManagement.setStyle("-fx-background-color:transparent;");
            this.getChar();
            this.getTotalBook();
            this.getTotalRental();
        } else if(e.getSource() == bookStorage) {
            this.cancelBtn();
        } else if(e.getSource() == rentalManagement) {
            home_form.setVisible(false);
            book_form.setVisible(false);
            rental_form.setVisible(true);
            customer_form.setVisible(false);

            rentalManagement.setStyle("-fx-background-color:#ccc;\n" +
                    " -fx-text-fill:#000;");
            bookStorage.setStyle("-fx-background-color:transparent;");
            home.setStyle("-fx-background-color:transparent;");
            customerManagement.setStyle("-fx-background-color:transparent;");

            this.showListRental();
            this.setSearchRenTal();
        } else if(e.getSource() == customerManagement) {
            home_form.setVisible(false);
            book_form.setVisible(false);
            rental_form.setVisible(false);
            customer_form.setVisible(true);

            customerManagement.setStyle("-fx-background-color:#ccc;\n" +
                    " -fx-text-fill:#000;");
            bookStorage.setStyle("-fx-background-color:transparent;");
            home.setStyle("-fx-background-color:transparent;");
            rentalManagement.setStyle("-fx-background-color:transparent;");

            this.showListCustomer();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        home.setStyle("-fx-background-color:#ccc;\n" +
                " -fx-text-fill:#000;");
        this.getTotalBook();
        this.getTotalRental();

        inputRentalPhone.setOnAction(event -> this.getRetalPhone());
        inputRentalCustomerId.setOnAction(event -> this.getRetalCustomerId());
        this.getChar();
        this.showListBook();
    }
}
