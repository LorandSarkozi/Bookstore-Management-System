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
import view.LoginView;
import view.CustomerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


public class CustomerController {

    private final AuthenticationService authenticationService;

    private final CustomerView customerView;

    private BookRepository bookRepository;

    private Connection connection;



    public CustomerController(CustomerView customerView, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.customerView = customerView;
        this.customerView.addBuyButtonListener(new BuyButtonListener());
        this.customerView.addLogOutButtonListener(new LogOutButtonListener());
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Timea.25");
            bookRepository = new BookRepositoryMySQL(connection);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    private class BuyButtonListener implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            Book selectedBook = customerView.getTableView().getSelectionModel().getSelectedItem();
            if (selectedBook != null && selectedBook.getQuantity() > 0) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm Purchase");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Do you want to buy the selected book?");
                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    int newQuantity = selectedBook.getQuantity();
                    selectedBook.setQuantity(newQuantity);
                    bookRepository.updateBookQuantity(selectedBook);

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Book purchased successfully!");
                    success.showAndWait();
                    customerView.getTableView().refresh();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book with available quantity!");
                alert.showAndWait();
            }
        }
    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            customerView.getWindow().close();
            LoginView loginView = new LoginView(new Stage());
            LoginController controller = new LoginController(loginView, authenticationService);


        }
    }
}