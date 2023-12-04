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
import repository.security.RightsRolesRepository;
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
    private final RightsRolesRepository rightsRolesRepository;

    public AdminController(AdminView adminView,AuthenticationService authenticationService,RightsRolesRepository rightsRolesRepository){
        this.authenticationService=authenticationService;
        this.adminView=adminView;
        this.rightsRolesRepository = rightsRolesRepository;
        this.adminView.addUserViewButtonListener(new AdminController.UserViewListener());
        this.adminView.addBookViewButtonListener(new AdminController.UserViewListener());
        this.adminView.addLogOutButtonListener(new AdminController.LogOutButtonListener());

    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            adminView.getWindow().close();
            LoginView loginView = new LoginView(new Stage());
            RightsRolesRepository rightRolesRepository = rightsRolesRepository;
            LoginController controller = new LoginController(loginView, authenticationService,rightRolesRepository);

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
