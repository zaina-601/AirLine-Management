package com.alabtaal.airline.controller;

import com.alabtaal.airline.config.DatabaseConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class OldPassengerLoginController implements Initializable {
    @FXML
    private TextField passportIdField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void Next(ActionEvent actionEvent) {
        String passportId = passportIdField.getText();
        String password = passwordField.getText();

        if (passportId.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM new_passenger WHERE passport_id = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, passportId);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    showAlert("Success", "Login Successful!");
                    loadScene("/fxml/SelectFlightPage.fxml", "Passenger Dashboard");
                } else {
                    showAlert("Error", "Invalid Passport ID or Password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to verify login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to the database.");
        }
    }
    @FXML
    public void goBack(ActionEvent actionEvent) {
        loadScene("/fxml/AsPassengerSelection.fxml", "Passenger Selection");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
