package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import service.user.AuthenticationService;
import view.EmployeeView;
import view.LoginView;
import view.CustomerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


public class EmployeeController {

    private final AuthenticationService authenticationService;

    private final EmployeeView employeeView;

    private BookRepository bookRepository;

    private Connection connection;
    private final RightsRolesRepository rightRolesRepository;

    public EmployeeController(EmployeeView customerView, AuthenticationService authenticationService,RightsRolesRepository rightsRolesRepository) {
        this.authenticationService = authenticationService;
        this.rightRolesRepository = rightsRolesRepository;
        this.employeeView = customerView;
        this.employeeView.addCreateButtonListener(new EmployeeController.CreateButtonListener());
        this.employeeView.addLogOutButtonListener(new EmployeeController.LogOutButtonListener());
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Timea.25");
            bookRepository = new BookRepositoryMySQL(connection);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            employeeView.getWindow().close();
            LoginView loginView = new LoginView(new Stage());
            LoginController controller = new LoginController(loginView, authenticationService,rightRolesRepository);

        }
    }

    private class CreateButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            Book newBook = employeeView.getBookDetails();

            boolean isBookSaved = bookRepository.save(newBook);


            if (isBookSaved) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Book saved successfully!");
                successAlert.showAndWait();

                employeeView.clearFields();

                employeeView.getTableView().getItems().add(newBook);
                employeeView.getTableView().refresh();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to save the book!");
                errorAlert.showAndWait();
            }
        }
    }

}
