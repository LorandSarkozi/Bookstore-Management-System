package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;
import view.MenuView;

import java.util.EventListener;
import java.util.List;

public class CustomerController {

    private MenuView menuView;

    public CustomerController(MenuView menuView){
        this.menuView=menuView;
        this.menuView.addBuyButtonListener(new BuyButtonListener());
        this.menuView.addLogOutButtonListener(new LogOutButtonListener());
    }



    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {



        }
    }
    private class LogOutButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(javafx.event.ActionEvent event) {





        }
    }
}
