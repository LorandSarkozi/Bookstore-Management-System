package view;

import com.mysql.cj.xdevapi.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeView {

    private TableView<Book> table;
    private Button createButton;
    private Button sellButton;
    private Button updateButton;
    private Button logOutButton;
    private Button deleteButton;


    private BookRepositoryMySQL bookRepository;
    private final TextField idField;

    private final TextField authorField;
    private final TextField titleField;
    private final DatePicker publishedDateField;
    private final TextField quantityField;
    private final TextField priceField;

    private Stage window;

    public EmployeeView(Stage primaryStage){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Timea.25");
            bookRepository = new BookRepositoryMySQL(connection);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        window = primaryStage;

        this.idField = new TextField();
        this.authorField = new TextField();
        this.titleField = new TextField();
        this.publishedDateField = new DatePicker();
        this.quantityField = new TextField();
        this.priceField = new TextField();

        primaryStage.setTitle("Employee Menu");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 1324, 680);
        primaryStage.setScene(scene);

        initializeFields(gridPane);

        primaryStage.show();
    }

    private void initializeFields(GridPane gridPane) {
        TableColumn<Book,Long> idColumn = new TableColumn<>("Id");
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        TableColumn<Book, Float> priceColumn = new TableColumn<>("Price");

        idColumn.setMinWidth(50);
        authorColumn.setMinWidth(200);
        titleColumn.setMinWidth(200);
        quantityColumn.setMinWidth(100);
        priceColumn.setMinWidth(100);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table = new TableView<>();
        table.setItems(getBook());
        table.getColumns().addAll(idColumn,authorColumn,titleColumn,quantityColumn,priceColumn);

        gridPane.add(new Label("Author:"), 0, 0);
        gridPane.add(authorField, 0, 1);

        gridPane.add(new Label("Title:"), 1, 0);
        gridPane.add(titleField, 1, 1);

        gridPane.add(new Label("Published Date:"), 2, 0);
        gridPane.add(publishedDateField, 2, 1);

        gridPane.add(new Label("Quantity:"), 3, 0);
        gridPane.add(quantityField, 3, 1);

        gridPane.add(new Label("Price:"), 4, 0);
        gridPane.add(priceField, 4, 1);


        gridPane.add(table,5,6);

        createButton = new Button("  Create  ");
        HBox createButtonHBox = new HBox(20);
        createButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        createButtonHBox.getChildren().add(createButton);
        gridPane.add(createButtonHBox, 0, 3);

        logOutButton = new Button("Log Out");
        HBox logOutButtonHBox = new HBox(20);
        logOutButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        logOutButtonHBox.getChildren().add(logOutButton);
        gridPane.add(logOutButtonHBox, 5, 9);

        sellButton = new Button("  Sell  ");
        HBox sellButtonHBox = new HBox(20);
        sellButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        sellButtonHBox.getChildren().add(sellButton);
        gridPane.add(sellButtonHBox, 1, 3);

        updateButton = new Button("  Update  ");
        HBox updateButtonHBox = new HBox(20);
        updateButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        updateButtonHBox.getChildren().add(updateButton);
        gridPane.add(updateButtonHBox, 1, 4);

        deleteButton = new Button("  Delete  ");
        HBox deleteButtonHBox = new HBox(20);
        deleteButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        deleteButtonHBox.getChildren().add(deleteButton);
        gridPane.add(deleteButtonHBox, 0, 4);



    }

    public TableView<Book> getTableView(){
        return this.table;
    }

    public Stage getWindow(){
        return this.window;
    }


    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public ObservableList<Book> getBook(){

        List<Book> book=bookRepository.findAll();
        return FXCollections.observableArrayList(book);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logOutButton.setOnAction(logoutButtonListener);


    }

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonListener) {
        createButton.setOnAction(createButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public Book getBookDetails() {
        String author = authorField.getText();
        String title = titleField.getText();
        LocalDate publishedDate = publishedDateField.getValue();
        int quantity = Integer.parseInt(quantityField.getText());
        float price = Float.parseFloat(priceField.getText());

        return new BookBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setPublishedDate(publishedDate)
                .setQuantity(quantity)
                .setPrice(price)
                .build();
    }

    public void setBookDetails(Book book){
        authorField.setText(book.getAuthor());
        titleField.setText(book.getTitle());
        publishedDateField.setValue(book.getPublishedDate());
        quantityField.setText(Integer.toString(book.getQuantity()));
        priceField.setText(String.valueOf(book.getPrice()));
    }

    public Long getIdField(){
        return Long.parseLong(idField.getText());
    }


    public void clearFields() {
        authorField.clear();
        titleField.clear();
        publishedDateField.setValue(null);
        quantityField.clear();
        priceField.clear();

    }
}
