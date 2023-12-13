package controller;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Book;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import view.AdminView;
import view.LoginView;
import view.CustomerView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static database.Constants.Roles.CUSTOMER;

public class AdminController {


    private final AuthenticationService authenticationService;

    private final AdminView adminView;

    private UserRepository userRepository;

    private Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminController(AdminView adminView,AuthenticationService authenticationService,RightsRolesRepository rightsRolesRepository){
        this.authenticationService=authenticationService;
        this.adminView=adminView;
        this.rightsRolesRepository = rightsRolesRepository;

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Timea.25");
            userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);
        }
        catch(SQLException e){
            e.printStackTrace();
        }



        this.adminView.addCreateButtonListener(new AdminController.CreateButtonListener());
        this.adminView.addLogOutButtonListener(new AdminController.LogOutButtonListener());
        this.adminView.addDeleteButtonListener(new AdminController.DeleteButtonListener());
        this.adminView.addUpdateButtonListener(new AdminController.UpdateButtonListener());

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

    private class CreateButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            Role customerRole = rightsRolesRepository.findRoleByTitle(adminView.getRole());

            User user = new UserBuilder()
                    .setUsername(adminView.getUsername())
                    .setPassword(adminView.getPassword())
                    .setRoles(Collections.singletonList(customerRole))
                    .build();

            UserValidator userValidator = new UserValidator(user);

            boolean userValid = userValidator.validate();
            Notification<Boolean> userRegisterNotification = new Notification<>();

            if (!userValid){
                userValidator.getErrors().forEach(userRegisterNotification::addError);
                userRegisterNotification.setResult(Boolean.FALSE);
            } else {
                user.setPassword(hashPassword(adminView.getPassword()));
                userRegisterNotification.setResult(userRepository.save(user));

                adminView.getTableView().getItems().add(user);
                adminView.getTableView().refresh();
                adminView.clearFields();
            }
        }

        private String hashPassword(String password) {
            try {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();

                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }

                return hexString.toString();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }


    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            User selectedUser = adminView.getTableView().getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                boolean isBookDeleted = userRepository.deleteUserById(selectedUser.getId());

                if (isBookDeleted) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("User deleted successfully!");
                    successAlert.showAndWait();

                    adminView.getTableView().getItems().remove(selectedUser);
                    adminView.clearFields();
                    adminView.getTableView().refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete the User!");
                    errorAlert.showAndWait();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a User to delete!");
                errorAlert.showAndWait();
            }
        }
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            Role customerRole = rightsRolesRepository.findRoleByTitle(adminView.getRole());

            User selectedUser = adminView.getTableView().getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                User updatedUser = new UserBuilder()
                        .setUsername(adminView.getUsername())
                        .setPassword(adminView.getPassword())
                        .setRoles(Collections.singletonList(customerRole))
                        .build();

                selectedUser.setUsername(updatedUser.getUsername());
                selectedUser.setPassword(updatedUser.getPassword());
                selectedUser.setRoles(updatedUser.getRoles());

                UserValidator userValidator = new UserValidator(selectedUser);

                boolean userValid = userValidator.validate();
                Notification<Boolean> userRegisterNotification = new Notification<>();

                if (!userValid){
                    userValidator.getErrors().forEach(userRegisterNotification::addError);
                    userRegisterNotification.setResult(Boolean.FALSE);
                } else {
                    selectedUser.setPassword(hashPassword(adminView.getPassword()));
                    userRegisterNotification.setResult(userRepository.updateUser(selectedUser));

                    adminView.getTableView().refresh();
                    adminView.clearFields();

                }

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please select a User to update!");
                errorAlert.showAndWait();
            }
        }
        private String hashPassword(String password) {
            try {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();

                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }

                return hexString.toString();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }



}
