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
import model.User;
import model.builder.UserBuilder;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdminView {
    private Button logOutButton;
    private Button createButton;
    private Button updateButton;
    private Button deleteButton;

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final TextField roleField;

    private final Stage window;
    private TableView<User> userTableView;
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

        this.passwordField = new PasswordField();
        this.usernameField = new TextField();
        this.roleField = new TextField();

        primaryStage.setTitle("Admin Menu");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 890, 680);
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
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        userTableView = new TableView<>();
        userTableView.setItems(getUser());
        userTableView.getColumns().addAll(usernameColumn/*,roleColumn*/);



        gridPane.add(userTableView,4,4);


        gridPane.add(new Label("username:"), 0, 0);
        gridPane.add(usernameField, 0, 1);

        gridPane.add(new Label("Password:"), 1, 0);
        gridPane.add(passwordField, 1, 1);

        gridPane.add(new Label("Role:"), 2, 0);
        gridPane.add(roleField, 2, 1);

        logOutButton = new Button(" Log Out ");
        HBox logOutButtonHBox = new HBox(20);
        logOutButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        logOutButtonHBox.getChildren().add(logOutButton);
        gridPane.add(logOutButtonHBox, 4, 6);

        createButton = new Button(" Create ");
        HBox createButtonHBox = new HBox(10);
        createButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        createButtonHBox.getChildren().add(createButton);
        gridPane.add(createButtonHBox, 0, 3);

        deleteButton = new Button(" Delete ");
        HBox deleteButtonHBox = new HBox(10);
        deleteButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        deleteButtonHBox.getChildren().add(deleteButton);
        gridPane.add(deleteButtonHBox, 1, 3);

        updateButton = new Button(" Update ");
        HBox updateButtonHBox = new HBox(10);
        updateButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        updateButtonHBox.getChildren().add(updateButton);
        gridPane.add(updateButtonHBox, 2, 3);



    }

    public ObservableList<User> getUser(){

        List<User> user=userRepository.findAll();
        return FXCollections.observableArrayList(user);
    }

    public TableView<User> getTableView(){
        return this.userTableView;
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

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonListener) {
        createButton.setOnAction(createButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }


    public String getPassword(){
        return passwordField.getText();
    }

    public String getUsername(){
        return usernameField.getText();
    }



    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
    }

    public String getRole(){
        return roleField.getText();
    }
}
