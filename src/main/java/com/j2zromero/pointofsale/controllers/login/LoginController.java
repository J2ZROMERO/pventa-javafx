package com.j2zromero.pointofsale.controllers.login;

import com.j2zromero.pointofsale.models.login.Login;
import com.j2zromero.pointofsale.services.login.LoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {


    private  LoginService loginService = new LoginService();

    public static void main(String[] args) {
        LoginController e = new LoginController();
        Login test = e.authenticate();
        System.out.println(test.getCode());
    }


    public Login authenticate() {
        return loginService.authenticate("ac");

    }



}
