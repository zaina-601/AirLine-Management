package com.alabtaal.airline.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class AdminVsPassengerController implements Initializable {
    @FXML
    private Button AsAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void AsAdmin(ActionEvent actionEvent) {
        loadScene("/fxml/AsAdminLoginPage.fxml", "Admin Login");
    }


    public void AsPassanger(ActionEvent actionEvent) {
        loadScene("/fxml/AsPassengerSelection.fxml", "Passenger Selection");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) AsAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}