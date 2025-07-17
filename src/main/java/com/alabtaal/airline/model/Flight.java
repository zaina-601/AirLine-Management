package com.alabtaal.airline.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Flight {
    private int id;
    private String flightNumber;
    private String fromLocation;
    private String toLocation;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private String duration;
    private double economyPrice;
    private double businessPrice;

    // Constructor
    public Flight(String flightNumber, String fromLocation, String toLocation,
                  LocalDate departureDate, LocalTime departureTime,
                  LocalDate arrivalDate, LocalTime arrivalTime,
                  String duration, double economyPrice, double businessPrice) {
        this.flightNumber = flightNumber;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
    }


    public void setId(int id) { this.id = id; }
    public int getId() { return id;}
    public String getFlightNumber() { return flightNumber; }
    public String getFromLocation() { return fromLocation; }
    public String getToLocation() { return toLocation; }
    public LocalDate getDepartureDate() { return departureDate; }
    public LocalTime getDepartureTime() { return departureTime; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public String getDuration() { return duration; }
    public double getEconomyPrice() { return economyPrice; }
    public double getBusinessPrice() { return businessPrice; }
}
