package com.alabtaal.airline.controller;

import com.alabtaal.airline.config.DatabaseConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
public class NewPassengerLoginController {

    @FXML
    private TextField passportIdField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button backButton;

    @FXML
    public void handleSignup(ActionEvent actionEvent) {
        String passportId = passportIdField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (passportId.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO new_passenger (passport_id, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, passportId);
                statement.setString(2, username);
                statement.setString(3, password);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Success", "Passenger Signed Up Successfully!");
                    loadScene("/fxml/SelectFlightPage.fxml", "Passenger Dashboard");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to insert new passenger.");
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
