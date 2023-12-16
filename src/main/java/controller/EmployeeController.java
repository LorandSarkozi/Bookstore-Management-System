package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.customer.CustomerRepository;
import repository.customer.CustomerRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import view.EmployeeView;
import view.LoginView;
import view.CustomerView;

import javax.swing.text.Document;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EmployeeController {

    private final AuthenticationService authenticationService;

    private final EmployeeView employeeView;

    private final LoginView loginView;

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;

    private Connection connection;
    private final RightsRolesRepository rightRolesRepository;

    public EmployeeController(LoginView loginView,EmployeeView customerView, AuthenticationService authenticationService,RightsRolesRepository rightsRolesRepository) {
        this.authenticationService = authenticationService;
        this.rightRolesRepository = rightsRolesRepository;
        this.employeeView = customerView;
        this.loginView = loginView;
        this.employeeView.addCreateButtonListener(new EmployeeController.CreateButtonListener());
        this.employeeView.addLogOutButtonListener(new EmployeeController.LogOutButtonListener());
        this.employeeView.addUpdateButtonListener(new EmployeeController.UpdateButtonListener());
        this.employeeView.addDeleteButtonListener(new EmployeeController.DeleteButtonListener());
        this.employeeView.addSellButtonListener(new EmployeeController.SellButtonListener());
        this.employeeView.addPdfButtonListener(new EmployeeController.PdfButtonListener());
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
            bookRepository = new BookRepositoryMySQL(connection);
            userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);
            customerRepository = new CustomerRepositoryMySQL(connection);
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

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event){
            Book selectedBook = employeeView.getTableView().getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                Book updatedBook = employeeView.getBookDetails();

                selectedBook.setAuthor(updatedBook.getAuthor());
                selectedBook.setTitle(updatedBook.getTitle());
                selectedBook.setPublishedDate(updatedBook.getPublishedDate());
                selectedBook.setQuantity(updatedBook.getQuantity());
                selectedBook.setPrice(updatedBook.getPrice());

                bookRepository.updateBook(selectedBook);

                employeeView.getTableView().refresh();

                employeeView.clearFields();
            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a book to update!");
                errorAlert.showAndWait();
            }
        }
    }



    private class CreateButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

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

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getTableView().getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                boolean isBookDeleted = bookRepository.deleteBookById(selectedBook.getId());

                if (isBookDeleted) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Book deleted successfully!");
                    successAlert.showAndWait();

                    employeeView.getTableView().getItems().remove(selectedBook);
                    employeeView.clearFields();
                    employeeView.getTableView().refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete the book!");
                    errorAlert.showAndWait();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a book to delete!");
                errorAlert.showAndWait();
            }
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            User employee = userRepository.findUserByUsername(loginView.getUsername());
            User customer = userRepository.findUserByUsername(employeeView.getEmail());
            Book selectedBook = employeeView.getTableView().getSelectionModel().getSelectedItem();
            customerRepository.buyBook(employee,customer,selectedBook);


            if (selectedBook != null && selectedBook.getQuantity() > 0) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm ");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Do you want to sell the selected book?");
                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    int newQuantity = selectedBook.getQuantity() ;
                    selectedBook.setQuantity(newQuantity);
                    bookRepository.updateBookQuantity(selectedBook);

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText(null);
                    success.setContentText("Book sold successfully!");
                    success.showAndWait();
                    employeeView.getTableView().refresh();
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

    public class PdfButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            User employee = userRepository.findUserByUsername(loginView.getUsername());
            //List<Book> books = customerRepository.findAllByEmployeeId(employee.getId());

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Books sold by employee: " + employee.getUsername());
                contentStream.newLine();

               /* for (Book book : books) {
                    contentStream.showText(book.getAuthor() + " - " + book.getTitle());
                    contentStream.newLine();
                }*/

                contentStream.endText();

                contentStream.close();

                File file = new File("SoldBooksReport.pdf");
                document.save(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Report");
                alert.setHeaderText(null);
                alert.setContentText("PDF report generated successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to generate the PDF report!");
                alert.showAndWait();
            }
        }
    }
}
