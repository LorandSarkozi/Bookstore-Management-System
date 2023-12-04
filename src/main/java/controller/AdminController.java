package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Book;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.user.AuthenticationService;
import view.AdminView;
import view.LoginView;
import view.CustomerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {


    private final AuthenticationService authenticationService;

    private final AdminView adminView;

    private BookRepository bookRepository;

    private Connection connection;

    public AdminController(AdminView adminView,AuthenticationService authenticationService){
        this.authenticationService=authenticationService;
        this.adminView=adminView;
        this.adminView.addUserViewButtonListener(new AdminController.UserViewListener());
        this.adminView.addBookViewButtonListener(new AdminController.UserViewListener());
        this.adminView.addLogOutButtonListener(new AdminController.LogOutButtonListener());

    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            adminView.getWindow().close();
            LoginView loginView = new LoginView(new Stage());
            LoginController controller = new LoginController(loginView, authenticationService);

        }
    }

    private class UserViewListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

        }
    }

    private class BookViewListener implements EventHandler<ActionEvent> {


        @Override
        public void handle(javafx.event.ActionEvent event){

        }
    }
}
