package com.alabtaal.airline.PassengerFlightController;

import com.alabtaal.airline.config.DatabaseConfig;
import com.alabtaal.airline.model.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import org.springframework.stereotype.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SelectSeatController {

    @FXML private Button NextButton;
    @FXML private Button BackButton;

    // Define all RadioButtons for seats in FXML
    @FXML private RadioButton A1, A2, A3, A4, A5, A6, A7, A8, A9;
    @FXML private RadioButton B1, B2, B3, B4, B5, B6, B7, B8, B9;
    @FXML private RadioButton C1, C2, C3, C4, C5, C6, C7, C8, C9;
    @FXML private RadioButton E1, E2, E3, E4, E5, E6, E7, E8, E9;
    @FXML private RadioButton F1, F2, F3, F4, F5, F6, F7, F8, F9;
    @FXML private RadioButton G1, G2, G3, G4, G5, G6, G7, G8, G9;

    private final Map<String, RadioButton> radioButtons = new HashMap<>();
    private final Map<String, Boolean> seatMap = new HashMap<>();
    private String selectedFlightNo;
    private Flight selectedFlight;
    private Connection connection;
    private String selectedSeat;
    private String selectedClass;



    @FXML
    public void initialize() {

        try {
            connection = DatabaseConfig.getConnection();
        } catch (SQLException e) {
            showDatabaseError("Database Connection Error", "Failed to connect to the database", e);
        }
        
        radioButtons.put("A1", A1);
        radioButtons.put("A2", A2);
        radioButtons.put("A3", A3);
        radioButtons.put("A4", A4);
        radioButtons.put("A5", A5);
        radioButtons.put("A6", A6);
        radioButtons.put("A7", A7);
        radioButtons.put("A8", A8);
        radioButtons.put("A9", A9);
        radioButtons.put("B1", B1);
        radioButtons.put("B2", B2);
        radioButtons.put("B3", B3);
        radioButtons.put("B4", B4);
        radioButtons.put("B5", B5);
        radioButtons.put("B6", B6);
        radioButtons.put("B7", B7);
        radioButtons.put("B8", B8);
        radioButtons.put("B9", B9);
        radioButtons.put("C1", C1);
        radioButtons.put("C2", C2);
        radioButtons.put("C3", C3);
        radioButtons.put("C4", C4);
        radioButtons.put("C5", C5);
        radioButtons.put("C6", C6);
        radioButtons.put("C7", C7);
        radioButtons.put("C8", C8);
        radioButtons.put("C9", C9);
        radioButtons.put("E1", E1);
        radioButtons.put("E2", E2);
        radioButtons.put("E3", E3);
        radioButtons.put("E4", E4);
        radioButtons.put("E5", E5);
        radioButtons.put("E6", E6);
        radioButtons.put("E7", E7);
        radioButtons.put("E8", E8);
        radioButtons.put("E9", E9);
        radioButtons.put("F1", F1);
        radioButtons.put("F2", F2);
        radioButtons.put("F3", F3);
        radioButtons.put("F4", F4);
        radioButtons.put("F5", F5);
        radioButtons.put("F6", F6);
        radioButtons.put("F7", F7);
        radioButtons.put("F8", F8);
        radioButtons.put("F9", F9);
        radioButtons.put("G1", G1);
        radioButtons.put("G2", G2);
        radioButtons.put("G3", G3);
        radioButtons.put("G4", G4);
        radioButtons.put("G5", G5);
        radioButtons.put("G6", G6);
        radioButtons.put("G7", G7);
        radioButtons.put("G8", G8);
        radioButtons.put("G9", G9);

        // Initializing seat map with false (not booked)
        for (String seat : radioButtons.keySet()) {
            seatMap.put(seat, false);
        }
    }

    private int selectedFlightId;  // Change from String to int

    public void setFlightData(Flight selectedFlight, String selectedClass) {
        this.selectedFlight = selectedFlight;
        this.selectedClass = selectedClass;
        this.selectedFlightId = selectedFlight.getId();
        loadBookedSeats();
    }




    private boolean updateSeatInDatabase(String seatNumber) {
        String sql = "INSERT INTO seats (flight_id, seat_number, is_booked) VALUES (?, ?, true) " +
                "ON CONFLICT (flight_id, seat_number) DO UPDATE SET is_booked = true " +
                "RETURNING id"; // Explicitly handle the ID

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, selectedFlightId);
            stmt.setString(2, seatNumber);

            // For PostgreSQL, we need to execute as a query to get the returned ID
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true; // Successfully inserted/updated
                }
            }
            return false;
        } catch (SQLException e) {
            showDatabaseError("Database Error", "Failed to update seat status", e);
            return false;
        }
    }
    private void loadBookedSeats() {
        String sql = "SELECT seat_number FROM seats WHERE flight_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, selectedFlightId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String seat = rs.getString("seat_number");
                seatMap.put(seat, true);
                RadioButton rb = radioButtons.get(seat);
                if (rb != null) {
                    rb.setDisable(true);
                }
            }
        } catch (SQLException e) {
            showDatabaseError("Database Error", "Could not load booked seats", e);
        }
    }

    private void handleSeat(String seatNumber) {
        if (seatMap.get(seatNumber)) {
            showAlert("Already Booked", "Seat " + seatNumber + " is already taken.");
            radioButtons.get(seatNumber).setSelected(false);
            return;
        }

        unselectAllSeats();
        seatMap.put(seatNumber, true);
        RadioButton btn = radioButtons.get(seatNumber);
        btn.setSelected(true);

        if (updateSeatInDatabase(seatNumber)) {
            btn.setDisable(true);
            showAlert("Seat Booked", "You have successfully booked seat: " + seatNumber);
        }
    }

    private void unselectAllSeats() {
        radioButtons.values().forEach(rb -> rb.setSelected(false));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDatabaseError(String title, String message, Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Database Operation Failed");
        alert.setContentText(message + "\n\nError: " + e.getMessage());
        alert.showAndWait();
    }
 
    public void handleBackButton(ActionEvent event) {
        loadScene("SelectFlightPage.fxml", "Passenger Dashboard");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlPath));
            Stage stage = (Stage) BackButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load scene: " + e.getMessage());
        }
    }


    @FXML
    public void handleNextButton(ActionEvent event) {
        String selectedSeat = getSelectedSeat();
        if (selectedSeat == null) {
            showAlert("Selection Required", "Please select a seat before proceeding.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BillPage.fxml"));
            Parent root = loader.load();

            PricingDetailController pricingController = loader.getController();
            pricingController.setFlightData(selectedFlight, selectedClass); // pass actual flight

            Stage stage = (Stage) NextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Payment Details");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load payment page: " + e.getMessage());
        }
    }

    private String getSelectedSeat() {
        return radioButtons.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


    // Event handlers for each seat (these methods are mapped dynamically in FXML)
    @FXML void A1(ActionEvent e) { handleSeat("A1"); }
    @FXML void A2(ActionEvent e) { handleSeat("A2"); }
    @FXML void A3(ActionEvent e) { handleSeat("A3"); }
    @FXML void A4(ActionEvent e) { handleSeat("A4"); }
    @FXML void A5(ActionEvent e) { handleSeat("A5"); }
    @FXML void A6(ActionEvent e) { handleSeat("A6"); }
    @FXML void A7(ActionEvent e) { handleSeat("A7"); }
    @FXML void A8(ActionEvent e) { handleSeat("A8"); }
    @FXML void A9(ActionEvent e) { handleSeat("A9"); }

    @FXML void B1(ActionEvent e) { handleSeat("B1"); }
    @FXML void B2(ActionEvent e) { handleSeat("B2"); }
    @FXML void B3(ActionEvent e) { handleSeat("B3"); }
    @FXML void B4(ActionEvent e) { handleSeat("B4"); }
    @FXML void B5(ActionEvent e) { handleSeat("B5"); }
    @FXML void B6(ActionEvent e) { handleSeat("B6"); }
    @FXML void B7(ActionEvent e) { handleSeat("B7"); }
    @FXML void B8(ActionEvent e) { handleSeat("B8"); }
    @FXML void B9(ActionEvent e) { handleSeat("B9"); }

    @FXML void C1(ActionEvent e) { handleSeat("C1"); }
    @FXML void C2(ActionEvent e) { handleSeat("C2"); }
    @FXML void C3(ActionEvent e) { handleSeat("C3"); }
    @FXML void C4(ActionEvent e) { handleSeat("C4"); }
    @FXML void C5(ActionEvent e) { handleSeat("C5"); }
    @FXML void C6(ActionEvent e) { handleSeat("C6"); }
    @FXML void C7(ActionEvent e) { handleSeat("C7"); }
    @FXML void C8(ActionEvent e) { handleSeat("C8"); }
    @FXML void C9(ActionEvent e) { handleSeat("C9"); }

    @FXML void E1(ActionEvent e) { handleSeat("E1"); }
    @FXML void E2(ActionEvent e) { handleSeat("E2"); }
    @FXML void E3(ActionEvent e) { handleSeat("E3"); }
    @FXML void E4(ActionEvent e) { handleSeat("E4"); }
    @FXML void E5(ActionEvent e) { handleSeat("E5"); }
    @FXML void E6(ActionEvent e) { handleSeat("E6"); }
    @FXML void E7(ActionEvent e) { handleSeat("E7"); }
    @FXML void E8(ActionEvent e) { handleSeat("E8"); }
    @FXML void E9(ActionEvent e) { handleSeat("E9"); }

    @FXML void F1(ActionEvent e) { handleSeat("F1"); }
    @FXML void F2(ActionEvent e) { handleSeat("F2"); }
    @FXML void F3(ActionEvent e) { handleSeat("F3"); }
    @FXML void F4(ActionEvent e) { handleSeat("F4"); }
    @FXML void F5(ActionEvent e) { handleSeat("F5"); }
    @FXML void F6(ActionEvent e) { handleSeat("F6"); }
    @FXML void F7(ActionEvent e) { handleSeat("F7"); }
    @FXML void F8(ActionEvent e) { handleSeat("F8"); }
    @FXML void F9(ActionEvent e) { handleSeat("F9"); }

    @FXML void G1(ActionEvent e) { handleSeat("G1"); }
    @FXML void G2(ActionEvent e) { handleSeat("G2"); }
    @FXML void G3(ActionEvent e) { handleSeat("G3"); }
    @FXML void G4(ActionEvent e) { handleSeat("G4"); }
    @FXML void G5(ActionEvent e) { handleSeat("G5"); }
    @FXML void G6(ActionEvent e) { handleSeat("G6"); }
    @FXML void G7(ActionEvent e) { handleSeat("G7"); }
    @FXML void G8(ActionEvent e) { handleSeat("G8"); }
    @FXML void G9(ActionEvent e) { handleSeat("G9"); }

}
