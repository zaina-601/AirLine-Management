package com.alabtaal.airline.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class loginPageController implements Initializable {

    @FXML
    private TextField UsernameTextField;
    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private Button Login;
    @FXML
    private Button Signup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void Login(ActionEvent event) {
        String username = UsernameTextField.getText();
        String password = PasswordTextField.getText();

        if (username.equals("admin") && password.equals("admin123")) {
            loadAdminDashboard();
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    @FXML
    public void Signup(ActionEvent event) {
        System.out.println("Signup button clicked - Load signup page");
    }

    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminDashboard.fxml"));
            Scene adminScene = new Scene(loader.load());
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.setScene(adminScene);
            stage.setTitle("Admin Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
