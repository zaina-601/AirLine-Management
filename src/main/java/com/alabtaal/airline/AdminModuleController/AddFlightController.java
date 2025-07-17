package com.alabtaal.airline.AdminModuleController;

import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;
import java.time.*;

public class AddFlightController {

        @FXML private TextField FromTextField, ToTextField, DurationTextField;
        @FXML private TextField EconomyPriceTextField, BusinessPriceTextField;
        @FXML private DatePicker DepartureDatePicker, ArrivalDatePicker;
        @FXML private JFXTimePicker DepartureTimePicker, ArrivalTimePicker;
        @FXML private Button backButton;

        private static final String INSERT_FLIGHT_SQL =
                "INSERT INTO flights (flight_number, from_location, to_location, departure_date, departure_time, arrival_date, arrival_time, duration, economy_price, business_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?::interval, ?, ?)";

        @FXML
        void initialize() {
                // Disable past dates
                setDateCellFactory(DepartureDatePicker);
                setDateCellFactory(ArrivalDatePicker);

                // Auto calculate duration when user sets arrival time/date
                ArrivalDatePicker.setOnAction(e -> calculateDuration());
                ArrivalTimePicker.setOnAction(e -> calculateDuration());
        }

        private void setDateCellFactory(DatePicker datePicker) {
                datePicker.setDayCellFactory(picker -> new DateCell() {
                        @Override
                        public void updateItem(LocalDate date, boolean empty) {
                                super.updateItem(date, empty);
                                setDisable(empty || date.isBefore(LocalDate.now()));
                        }
                });
        }

        private void calculateDuration() {
                LocalDate depDate = DepartureDatePicker.getValue();
                LocalTime depTime = DepartureTimePicker.getValue();
                LocalDate arrDate = ArrivalDatePicker.getValue();
                LocalTime arrTime = ArrivalTimePicker.getValue();

                if (depDate == null || depTime == null || arrDate == null || arrTime == null) return;

                LocalDateTime dep = LocalDateTime.of(depDate, depTime);
                LocalDateTime arr = LocalDateTime.of(arrDate, arrTime);

                if (arr.isBefore(dep)) {
                        DurationTextField.setText("Invalid Timing!");
                        return;
                }

                Duration duration = Duration.between(dep, arr);
                DurationTextField.setText(duration.toHours() + "h " + duration.toMinutesPart() + "m");
        }

        @FXML
        void SubmitButton(ActionEvent event) {
                // Collect input data
                String from = FromTextField.getText();
                String to = ToTextField.getText();
                LocalDate departureDate = DepartureDatePicker.getValue();
                LocalTime departureTime = DepartureTimePicker.getValue();
                LocalDate arrivalDate = ArrivalDatePicker.getValue();
                LocalTime arrivalTime = ArrivalTimePicker.getValue();
                double economyPrice = parseDouble(EconomyPriceTextField);
                double businessPrice = parseDouble(BusinessPriceTextField);

                // Calculate duration
                Duration duration = Duration.between(LocalDateTime.of(departureDate, departureTime), LocalDateTime.of(arrivalDate, arrivalTime));

                // Convert the duration to PostgreSQL INTERVAL format
                String durationFormatted = String.format("%d hours %d minutes", duration.toHours(), duration.toMinutesPart());

                // Insert flight into the database
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/airline_db", "airline_admin", "airline123");
                     PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FLIGHT_SQL)) {

                        // Set parameters
                        preparedStatement.setString(1, "F" + System.currentTimeMillis());  // Flight number, e.g., based on current time
                        preparedStatement.setString(2, from);
                        preparedStatement.setString(3, to);
                        preparedStatement.setDate(4, java.sql.Date.valueOf(departureDate));
                        preparedStatement.setTime(5, java.sql.Time.valueOf(departureTime));
                        preparedStatement.setDate(6, java.sql.Date.valueOf(arrivalDate));
                        preparedStatement.setTime(7, java.sql.Time.valueOf(arrivalTime));
                        preparedStatement.setString(8, durationFormatted);  // Pass the duration as a string in PostgreSQL's expected format
                        preparedStatement.setDouble(9, economyPrice);
                        preparedStatement.setDouble(10, businessPrice);

                        // Execute insert
                        preparedStatement.executeUpdate();
                        showAlert(Alert.AlertType.INFORMATION, "Flight Added Successfully!", "Flight details have been added to the database.");

                } catch (SQLException e) {
                        e.printStackTrace();  // This will print the actual error message for better debugging
                        showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());  // Show the actual error message
                }
        }

        private double parseDouble(TextField textField) {
                try {
                        return Double.parseDouble(textField.getText());
                } catch (NumberFormatException e) {
                        return 0.0;  // Default value if the input is invalid
                }
        }

        private void showAlert(Alert.AlertType type, String header, String content) {
                Alert alert = new Alert(type);
                alert.setTitle(type == Alert.AlertType.INFORMATION ? "Flight Details" : "Error");
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
        }

        @FXML
        void backButton(ActionEvent event) {
                try {
                        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminNextSelection.fxml"));
                        Stage stage = (Stage) backButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Admin Dashboard");
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
