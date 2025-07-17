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

public class AASignupController {

    @FXML
    private TextField NameAASTextField, AASPasswordField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private Button backButton, signUpButton;

    public void initialize() {
        roleChoiceBox.getItems().addAll("Admin Manager", "Flight Coordinator", "Finance Admin", "Customer Support");
        roleChoiceBox.setValue("Admin Manager");
    }

    public void NextAAS(ActionEvent actionEvent) {
        String name = NameAASTextField.getText();
        String password = AASPasswordField.getText();
        String role = roleChoiceBox.getValue();

        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
            showAlert("Error", "Name and Password are required!");
            return;
        }

        if (insertAdmin(name, password, role)) {
            showAlert("Success", "Signup Successful!");
            loadScene("/fxml/AdminNextSelection.fxml", "Admin Dashboard");
        } else {
            showAlert("Error", "Error during signup.");
        }
    }

    private boolean insertAdmin(String name, String password, String role) {
        String query = "INSERT INTO admins(name, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, role);

            stmt.executeUpdate();
            return true;
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

    public void goBack(ActionEvent actionEvent) {
        loadScene("AsAdminLoginPage.fxml", "Admin Login");
    }
}
