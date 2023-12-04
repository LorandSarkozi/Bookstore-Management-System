package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.AdminView;
import view.EmployeeView;
import view.LoginView;
import view.CustomerView;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

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
                loginView.setActionTargetText("LogIn Successful!");
                loginView.getWindow().close();
                
                //if() {

                /*CustomerView customerView = new CustomerView(new Stage());
                CustomerController controller = new CustomerController(customerView, authenticationService);*/

                // }else if() {

                AdminView adminView = new AdminView(new Stage());
                AdminController controller = new AdminController(adminView,authenticationService);

                //}else{

                /*EmployeeView employeeView = new EmployeeView(new Stage());
                EmployeeController controller = new EmployeeController(employeeView,authenticationService);*/


                //}

                //loginView.addLoginButtonListener();
                // primaryStage ?


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