package com.alabtaal.airline.AdminModuleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
    public class AdminNextSelection {

        @FXML
        private Button addFlight;

        @FXML
        private Button backButton;

        @FXML
        private Button dropFlight;

        @FXML
        private Button viewFlights;

        @FXML
        void addFlight(ActionEvent event) {
            loadScene("/fxml/AddFlightPage.fxml", "Add Flight");
        }

        @FXML
        void backButton(ActionEvent event) {
            loadScene("/fxml/AsAdminLoginPage.fxml", "Admin Login");
        }

        @FXML
        void dropFlight(ActionEvent event) {
            loadScene("/fxml/dropFlightPage.fxml", "Drop Flight");
        }

        @FXML
        void viewFlights(ActionEvent event) {
            loadScene("/fxml/viewFlights.fxml", "Passenger Details");
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


