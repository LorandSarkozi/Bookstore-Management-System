package view;

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
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class EmployeeView {

    private TableView<Book> table;
    private Button createButton;
    private Button logOutButton;

    private BookRepositoryMySQL bookRepository;

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

        primaryStage.setTitle("Employee Menu");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeFields(gridPane);

        primaryStage.show();
    }

    private void initializeFields(GridPane gridPane) {
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        TableColumn<Book, Float> priceColumn = new TableColumn<>("Price");
        authorColumn.setMinWidth(200);
        titleColumn.setMinWidth(200);
        quantityColumn.setMinWidth(100);
        priceColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table = new TableView<>();
        table.setItems(getBook());
        table.getColumns().addAll(authorColumn,titleColumn,quantityColumn,priceColumn);

        gridPane.add(table,0,1);

        createButton = new Button("Create");
        HBox buyButtonHBox = new HBox(10);
        buyButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        buyButtonHBox.getChildren().add(createButton);
        gridPane.add(buyButtonHBox, 1, 3);

        logOutButton = new Button("Log Out");
        HBox logOutButtonHBox = new HBox(10);
        logOutButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logOutButtonHBox.getChildren().add(logOutButton);
        gridPane.add(logOutButtonHBox, 0, 3);


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
}
