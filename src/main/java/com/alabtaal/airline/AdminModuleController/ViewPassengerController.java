package com.alabtaal.airline.AdminModuleController;

import com.alabtaal.airline.config.DatabaseConfig;
import com.alabtaal.airline.model.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewPassengerController {

        @FXML private TableView<Flight> flightTableView;
        @FXML private TableColumn<Flight, String> flightNumberCol;
        @FXML private TableColumn<Flight, String> fromCol;
        @FXML private TableColumn<Flight, String> toCol;
        @FXML private TableColumn<Flight, java.time.LocalDate> departureDateCol;
        @FXML private TableColumn<Flight, java.time.LocalTime> departureTimeCol;
        @FXML private TableColumn<Flight, java.time.LocalDate> arrivalDateCol;
        @FXML private TableColumn<Flight, java.time.LocalTime> arrivalTimeCol;
        @FXML private TableColumn<Flight, String> durationCol;
        @FXML private TableColumn<Flight, Double> economyPriceCol;
        @FXML private TableColumn<Flight, Double> businessPriceCol;
        @FXML private Button backButton;

        @FXML
        public void initialize() {
                // Bind columns with Flight class fields
                flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
                fromCol.setCellValueFactory(new PropertyValueFactory<>("fromLocation"));
                toCol.setCellValueFactory(new PropertyValueFactory<>("toLocation"));
                departureDateCol.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
                departureTimeCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
                arrivalDateCol.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
                arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
                durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
                economyPriceCol.setCellValueFactory(new PropertyValueFactory<>("economyPrice"));
                businessPriceCol.setCellValueFactory(new PropertyValueFactory<>("businessPrice"));

                loadFlightData();
        }

        private void loadFlightData() {
                ObservableList<Flight> flightData = FXCollections.observableArrayList();
                String query = "SELECT * FROM flights";

                try (Connection conn = DatabaseConfig.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                        while (rs.next()) {
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
                                flightData.add(flight);
                        }

                        flightTableView.setItems(flightData);

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        public void backButton(ActionEvent event) {
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
