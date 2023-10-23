package com.example.pe.entity;

public class Contact {
    public int id;
    public String firstName;
    public String lastName;
    public String email;
    public String address;
    public String phone;
    public String company;
    public String imageUri;

    public Contact(int id, String firstName, String lastName, String email, String address, String phone, String company, String imageUri) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.company = company;
        this.imageUri = imageUri;
    }
    public Contact(){

    }

    public Contact(int id, String firstName, String lastName, String email, String phone, String imageUri) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.imageUri = imageUri;
    }
}
