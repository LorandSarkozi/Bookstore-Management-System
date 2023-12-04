package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Book;
import model.User;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AdminView {
    private Button logOutButton;
    private Button userViewButton;
    private Button bookViewButton;
    private final Stage window;
    private TableView<User> userTableView;
    private TableView<Book> bookTableView;
    private UserRepository userRepository;

    private RightsRolesRepository rightsRolesRepository;


    public AdminView(Stage primaryStage){

        try{

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Timea.25");
            userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        window=primaryStage;

        primaryStage.setTitle("Admin Menu");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeFields(gridPane);

        primaryStage.show();


    }

    private void initializeFields(GridPane gridPane) {
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        TableColumn<User, String> roleColumn = new TableColumn<>("Role");

        usernameColumn.setMinWidth(300);
        roleColumn.setMinWidth(100);

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTableView = new TableView<>();
        userTableView.setItems(getUser());
        userTableView.getColumns().addAll(usernameColumn);

        gridPane.add(userTableView,0,1);



        logOutButton = new Button("Log Out");
        HBox logOutButtonHBox = new HBox(10);
        logOutButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logOutButtonHBox.getChildren().add(logOutButton);
        gridPane.add(logOutButtonHBox, 0, 4);

        userViewButton = new Button("View User");
        HBox userViewButtonHBox = new HBox(10);
        userViewButtonHBox.setAlignment(Pos.BOTTOM_CENTER);
        userViewButtonHBox.getChildren().add(userViewButton);
        gridPane.add(userViewButtonHBox, 0, 3);

        bookViewButton = new Button("View Book");
        HBox bookViewButtonHBox = new HBox(10);
        bookViewButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bookViewButtonHBox.getChildren().add(bookViewButton);
        gridPane.add(bookViewButtonHBox, 0, 4);


    }

    public ObservableList<User> getUser(){

        List<User> user=userRepository.findAll();
        return FXCollections.observableArrayList(user);
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }
    public Stage getWindow(){
        return this.window;
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logOutButton.setOnAction(logoutButtonListener);
    }

    public void addUserViewButtonListener(EventHandler<ActionEvent> userViewButtonListener) {
        userViewButton.setOnAction(userViewButtonListener);
    }

    public void addBookViewButtonListener(EventHandler<ActionEvent> bookVewButtonListener) {
        bookViewButton.setOnAction(bookVewButtonListener);
    }
}
