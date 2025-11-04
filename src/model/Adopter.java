/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Aspire-5
 */
public class Adopter {

    private int adopterId;
    private String name;
    private String contactPhone;
    private String address;

    public Adopter(int adopterId, String name, String contactPhone, String address) {
        this.adopterId = adopterId;
        this.name = name;
        this.contactPhone = contactPhone;
        this.address = address;
    }

    // Getters
    public int getAdopterId() {
        return adopterId;
    }

    public String getName() {
        return name;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " (ID: " + adopterId + ")";
    }
}
