package com.alabtaal.airline.controller;

import com.alabtaal.airline.config.DatabaseConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AALoginController {

    @FXML
    private TextField UserAATextField;
    @FXML
    private PasswordField PasswordAATextField;
    @FXML
    private Button backButton;

    public void AdminLoginButton(ActionEvent actionEvent) {
        String username = UserAATextField.getText();
        String password = PasswordAATextField.getText();

        if (username == null || username.isEmpty()) {
            showAlert("Error", "Username is required!");
            return;
        }
        if (password == null || password.isEmpty()) {
            showAlert("Error", "Password is required!");
            return;
        }

        if (loginAdmin(username, password)) {
            showAlert("Success", "Login Successful!");
            loadScene("/fxml/AdminNextSelection.fxml", "Admin Dashboard");
        } else {
            showAlert("Error", "Invalid Username or Password.");
        }
    }

    private boolean loginAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE name = ? AND password = ?"; // SQL query
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If user found in DB, returns true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goBack(ActionEvent actionEvent) {
        loadScene("/fxml/AdminVsPassenger.fxml", "Choose Admin or Passenger");
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
        }
    }

    public void AdminSignup(ActionEvent actionEvent) {
     loadScene("/fxml/AsAdminSignupPage.fxml", "Admin Signup");
    }
}