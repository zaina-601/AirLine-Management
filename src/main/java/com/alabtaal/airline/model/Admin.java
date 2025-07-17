package com.alabtaal.airline.model;

public class Admin {
    private int id;
    private String name;
    private String password;
    private String role;

    public Admin(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

}
