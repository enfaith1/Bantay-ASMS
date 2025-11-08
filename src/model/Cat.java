/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Aspire-5
 */
import java.time.LocalDate; // <-- ADD THIS IMPORT

public class Cat extends Animal {

    // The constructor just passes all info to the parent
    public Cat(int id, String name, String breed, int age, String gender,
            SourceType sourceType, String adoptionStatus, String healthStatus,
            LocalDate dateArrived) { // <-- 1. ADD TO CONSTRUCTOR

        super(id, name, "Cat", breed, age, gender, sourceType, adoptionStatus, healthStatus, dateArrived); // <-- 2. PASS TO SUPER
    }

    @Override
    public String getDetails() {
        // Example of a Cat-specific detail
        return "Breed: " + breed + ", Health: " + healthStatus;
    }

}
