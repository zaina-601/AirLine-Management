package com.alabtaal.airline.PassengerFlightController;

import com.alabtaal.airline.config.DatabaseConfig;
import com.alabtaal.airline.model.Flight; // Import the correct Flight class
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class SelectFlightController {

        @FXML private TableColumn<Flight, String> FromScheduleField;
        @FXML private TableColumn<Flight, String> ToScheduleField;
        @FXML private TableColumn<Flight, String> DepartureDateSchedule;
        @FXML private TableColumn<Flight, String> DepartureTimeSchedule;

        @FXML private TableView<Flight> flightTableView;
        @FXML private ChoiceBox<String> SelectClass;
        @FXML private TextField FromTextField, ToTextField, departureDateTextField, departureTimeTextField;
        @FXML private Button backButton, nextButton;

        private Connection conn;

        @FXML
        public void initialize() {
                connectToDatabase();

                SelectClass.getItems().addAll("Economy", "Business");

                FromScheduleField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFromLocation()));
                ToScheduleField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToLocation()));
                DepartureDateSchedule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureDate().toString()));
                DepartureTimeSchedule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime().toString()));

                flightTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal != null) {
                                FromTextField.setText(newVal.getFromLocation());
                                ToTextField.setText(newVal.getToLocation());
                                setDepartureDate(newVal.getDepartureDate().toString());
                                departureTimeTextField.setText(newVal.getDuration()); // Set duration here
                        }
                });

                loadFlightData();
        }

        private void setDepartureDate(String departureDate) {
                try {
                        departureDateTextField.setText(departureDate);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void connectToDatabase() {
                try {
                        conn = DatabaseConfig.getConnection();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        private void loadFlightData() {
                String query = "SELECT id, flight_number, from_location, to_location, departure_date, " +
                        "departure_time, arrival_date, arrival_time, duration, " +
                        "economy_price, business_price FROM flights";

                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                        while (rs.next()) {
                                // First create the Flight object with the existing constructor
                                Flight flight = new Flight(
                                        rs.getString("flight_number"),
                                        rs.getString("from_location"),
                                        rs.getString("to_location"),
                                        rs.getDate("departure_date").toLocalDate(),
                                        rs.getTime("departure_time").toLocalTime(),
                                        rs.getDate("arrival_date").toLocalDate(),
                                        rs.getTime("arrival_time").toLocalTime(),
                                        rs.getString("duration"),
                                        rs.getDouble("economy_price"),
                                        rs.getDouble("business_price")
                                );

                                // Then set the ID separately using the setId method
                                flight.setId(rs.getInt("id"));

                                flightTableView.getItems().add(flight);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert("Database Error", "Failed to load flight data: " + e.getMessage());
                }
        }

        @FXML
        void nextButton(ActionEvent event) {
                // Get the selected flight and class
                Flight selectedFlight = flightTableView.getSelectionModel().getSelectedItem();
                String selectedClass = SelectClass.getValue();

                // Ensure a flight and class are selected
                if (selectedFlight == null || selectedClass == null) {
                        showAlert("Selection Required", "Please select a flight and class before proceeding.");
                        return;
                }

                try {
                        // Load the next scene (SelectSeatPage)
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectSeatPage.fxml"));
                        Parent root = loader.load();

                        // Get the SelectSeatController and pass the selected flight and class
                        SelectSeatController seatController = loader.getController();
                        seatController.setFlightData(selectedFlight, selectedClass);

                        // Switch to the SelectSeatPage
                        Stage stage = (Stage) nextButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Select Seat");
                        stage.show();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void showAlert(String title, String content) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(title);
                alert.setContentText(content);
                alert.showAndWait();
        }

        @FXML
        void backButton(ActionEvent event) {
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
                }
        }
}