package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.Role;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import service.user.AuthenticationService;
import view.AdminView;
import view.EmployeeView;
import view.LoginView;
import view.CustomerView;

import java.util.List;

import static database.Constants.Roles.*;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final RightsRolesRepository rightsRolesRepository;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, RightsRolesRepository rightsRolesRepository) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.rightsRolesRepository = rightsRolesRepository;
        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Login successful!");
                loginView.getWindow().close();
                User loggedInUser = loginNotification.getResult();
                List<Role> userRoles = loggedInUser.getRoles();
                String role = userRoles.get(0).getRole();
                switch (role){
                    case ADMINISTRATOR: {
                        AdminView adminView = new AdminView(new Stage());
                        AdminController controller = new AdminController(adminView, authenticationService,rightsRolesRepository);
                        break;
                    }
                    case EMPLOYEE: {
                        EmployeeView employeeView = new EmployeeView(new Stage());
                        EmployeeController controller = new EmployeeController(employeeView, authenticationService,rightsRolesRepository);
                        break;
                    }
                    case CUSTOMER: {
                        CustomerView customerView = new CustomerView(new Stage());
                        CustomerController controller = new CustomerController(customerView, authenticationService,rightsRolesRepository);
                        break;
                    }
                }

            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}