package com.alabtaal.airline.PassengerFlightController;

import com.alabtaal.airline.model.Flight;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PricingDetailController {

        @FXML private TextField TicketCost;
        @FXML private TextField TaxAmount;
        @FXML private TextField TotalPrice;

        private String flightNumber;
        private String selectedClass;
        private double ticketCost;

        @FXML
        public void initialize() {
                System.out.println("PricingDetailController initialized");
        }

        public void setFlightData(Flight flight, String selectedClass) {
                this.flightNumber = flight.getFlightNumber();
                this.selectedClass = selectedClass;
                loadPricingDetails();
        }

        private void loadPricingDetails() {
                System.out.println("Loading pricing details for flight: " + flightNumber);

                if (flightNumber == null || flightNumber.isEmpty()) {
                        showAlert("Error", "Invalid flight selection");
                        return;
                }

                String query = "SELECT economy_price, business_price FROM flights WHERE flight_number = ?";

                try (Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/airline_db",
                        "airline_admin",
                        "airline123");
                     PreparedStatement stmt = connection.prepareStatement(query)) {

                        stmt.setString(1, flightNumber);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                                double economyPrice = rs.getDouble("economy_price");
                                double businessPrice = rs.getDouble("business_price");

                                this.ticketCost = selectedClass.equalsIgnoreCase("economy") ? economyPrice : businessPrice;
                                double tax = ticketCost * 0.05;
                                double total = ticketCost + tax;

                                Platform.runLater(() -> {
                                        TicketCost.setText(String.format("PKR %.2f", ticketCost));
                                        TaxAmount.setText(String.format("PKR %.2f", tax));
                                        TotalPrice.setText(String.format("PKR %.2f", total));
                                        System.out.println("Prices loaded successfully");
                                });
                        } else {
                                showAlert("Error", "No pricing data found for this flight");
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Database Error", "Failed to load pricing: " + e.getMessage());
                }
        }

        @FXML
        private void Payment() {
                if (flightNumber == null || selectedClass == null || ticketCost <= 0) {
                        showAlert("Error", "Flight data not properly loaded.");
                        return;
                }

                // Create a custom input dialog for card details
                TextField cardNumberField = new TextField();
                cardNumberField.setPromptText("Enter 13-digit Card Number");

                Alert cardDialog = new Alert(Alert.AlertType.CONFIRMATION);
                cardDialog.setTitle("Card Payment");
                cardDialog.setHeaderText("Enter your Card Details");
                cardDialog.getDialogPane().setContent(cardNumberField);

                cardDialog.showAndWait().ifPresent(response -> {
                        String cardNumber = cardNumberField.getText().trim();

                        // Validate card number
                        if (!cardNumber.matches("\\d{13}")) {
                                showAlert("Invalid Card", "Please enter a valid 13-digit card number.");
                                return;
                        }

                        // If valid, show sweet success message
                        double tax = ticketCost * 0.05;
                        double total = ticketCost + tax;

                        String sweetMessage = "🎉 Thank you for choosing Al-Parwaz Airlines! ✈️\n" +
                                "We wish you a safe and joyful journey!\n\n" +
                                "Your booking summary:\n" +
                                "Flight Number: " + flightNumber + "\n" +
                                "Class: " + selectedClass + "\n" +
                                "Ticket Cost: PKR " + String.format("%.2f", ticketCost) + "\n" +
                                "Tax (5%): PKR " + String.format("%.2f", tax) + "\n" +
                                "Total Amount Paid: PKR " + String.format("%.2f", total) + "\n\n" +
                                "💺 Your seat is reserved.";

                        showAlert("Payment Successful", sweetMessage);
                });
        }


        private void showAlert(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
}