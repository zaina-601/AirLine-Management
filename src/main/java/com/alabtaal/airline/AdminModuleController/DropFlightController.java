package com.alabtaal.airline.AdminModuleController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.io.IOException;

@Controller
public class DropFlightController {

    @FXML private Button DeleteButton, backButton;
    @FXML private TextField FlightNo;
    @FXML private TextField FromTextField;
    @FXML private TextField ToTextField;

    @FXML
    private void DeleteButton(ActionEvent event) {
        String flightNumber = FlightNo.getText().trim();

        if (flightNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Field", "Please enter a flight number to delete.");
            return;
        }

        String deleteFlightSQL = "DELETE FROM flights WHERE flight_number = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/airline_db", "airline_admin", "airline123");
             PreparedStatement preparedStatement = connection.prepareStatement(deleteFlightSQL)) {

            preparedStatement.setString(1, flightNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight deleted successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No flight found with the provided flight number.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the flight.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        FromTextField.clear();
        ToTextField.clear();
        FlightNo.clear();
    }

    @FXML
    private void backButton(ActionEvent event) {
        loadScene("/fxml/AdminNextSelection.fxml", "Admin Dashboard");
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
}
