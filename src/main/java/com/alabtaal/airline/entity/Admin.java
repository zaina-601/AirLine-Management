package com.alabtaal.airline.entity;

import jakarta.persistence.*;


@Entity
@Table(schema = "airline", name = "admins")
public class Admin {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Column(name = "passenger_from")
    private String from;
    @Column(name = "passenger_to")
    private String to;
    @Column(name= "departure_date")
    private String Date;
    @Column(name="departure_time")
    private String time;
    @Column(name="arrival_time")
    private String arrivalTime;
    @Column(name ="arrival_date")
    private String departureTime;
    @Column(name = "passenger_duration")
    private String passengerDuration;
    @Column(name="price_economy")
    private Long priceEconomy;

    public Long getPriceBusiness() {
        return priceBusiness;
    }

    public void setPriceBusiness(Long priceBusiness) {
        this.priceBusiness = priceBusiness;
    }

    @Column(name = "price_business")
    private Long priceBusiness;


    public Long getPriceEconomy() {
        return priceEconomy;
    }

    public void setPriceEconomy(Long priceEconomy) {
        this.priceEconomy = priceEconomy;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }


    public String getDate() {
        return Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }


    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getPassengerDuration() {
        return passengerDuration;
    }

    public void setPassengerDuration(String passengerDuration) {
        this.passengerDuration = passengerDuration;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
