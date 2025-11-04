/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Aspire-5
 */
public class Bird extends Animal {

// Constructor
    public Bird(int id, String name, String breed, int age, String gender,
            SourceType sourceType, String adoptionStatus, String healthStatus) {

        // Calls the parent (Animal) constructor
        // We pass "Bird" as the species
        super(id, name, "Bird", breed, age, gender, sourceType, adoptionStatus, healthStatus);
    }

    // POLYMORPHISM: Implementing the abstract method
    @Override
    public String getDetails() {
        // Example of a Bird-specific detail
        return "Breed: " + breed + ", Health: " + healthStatus;
    }
}
