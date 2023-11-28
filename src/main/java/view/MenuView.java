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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import repository.book.BookRepository;

import java.util.List;

public class MenuView {

    private TableView<Book> table;
    private Button buyButton;
    private Button logOutButton;

    private Book book;

    public MenuView(Stage primaryStage){
        primaryStage.setTitle("Customer Menu");

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
        authorColumn.setMinWidth(200);
        titleColumn.setMinWidth(200);
        quantityColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table = new TableView<>();
        table.setItems(table.getBook());
        table.getColumns().addAll(authorColumn,titleColumn,quantityColumn);

        gridPane.add(table,0,1);

        buyButton = new Button("Buy");
        HBox buyButtonHBox = new HBox(10);
        buyButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        buyButtonHBox.getChildren().add(buyButton);
        gridPane.add(buyButtonHBox, 1, 3);

        logOutButton = new Button("Log Out");
        HBox logOutButtonHBox = new HBox(10);
        logOutButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logOutButtonHBox.getChildren().add(logOutButton);
        gridPane.add(logOutButtonHBox, 0, 3);


    }


    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public ObservableList<Book> getBook(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll();

        return books;
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logOutButton.setOnAction(logoutButtonListener);


    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }

}
