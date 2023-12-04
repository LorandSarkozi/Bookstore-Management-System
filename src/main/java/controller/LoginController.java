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
               /* User loggedInUser = loginNotification.getResult();
                if (loggedInUser != null) {
                    List<Role> userRoles = rightsRolesRepository.findRolesForUser(loggedInUser.getId());
                    if (userRoles != null && !userRoles.isEmpty()) {
                        for (Role role : userRoles) {
                            if ("ADMINISTRATOR".equals(role.getRole())) {
                                AdminView adminView = new AdminView(new Stage());
                                AdminController controller = new AdminController(adminView, authenticationService,rightsRolesRepository);
                                loginView.setActionTargetText("Admin login successful!");
                                loginView.getWindow().close();
                                break;
                            } else if ("EMPLOYEE".equals(role.getRole())) {
                                EmployeeView employeeView = new EmployeeView(new Stage());
                                EmployeeController controller = new EmployeeController(employeeView, authenticationService,rightsRolesRepository);
                                loginView.setActionTargetText("Employee login successful!");
                                loginView.getWindow().close();
                                break;
                            } else if("CUSTOMER".equals(role.getRole())) {
                                CustomerView customerView = new CustomerView(new Stage());
                                CustomerController controller = new CustomerController(customerView, authenticationService,rightsRolesRepository);
                                loginView.setActionTargetText("Login successful!");
                                loginView.getWindow().close();
                                break;
                            }
                        }
                    }
                }*/

                /*CustomerView customerView = new CustomerView(new Stage());
                CustomerController controller = new CustomerController(customerView, authenticationService,rightsRolesRepository);
                loginView.setActionTargetText("Login successful!");
                loginView.getWindow().close();*/

                /*AdminView adminView = new AdminView(new Stage());
                AdminController controller = new AdminController(adminView, authenticationService,rightsRolesRepository);
                loginView.setActionTargetText("Admin login successful!");
                loginView.getWindow().close();*/

                EmployeeView employeeView = new EmployeeView(new Stage());
                EmployeeController controller = new EmployeeController(employeeView, authenticationService,rightsRolesRepository);
                loginView.setActionTargetText("Employee login successful!");
                loginView.getWindow().close();
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