/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author Aspire-5
 */
public class Adoption {

    private int adoptionId;
    private int animalId;
    private int adopterId;
    private LocalDate adoptionDate;

    public Adoption(int adoptionId, int animalId, int adopterId, LocalDate adoptionDate) {
        this.adoptionId = adoptionId;
        this.animalId = animalId;
        this.adopterId = adopterId;
        this.adoptionDate = adoptionDate;
    }

    // Getters
    public int getAdoptionId() {
        return adoptionId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getAdopterId() {
        return adopterId;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    // Setters
    public void setAdoptionId(int adoptionId) {
        this.adoptionId = adoptionId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    @Override
    public String toString() {
        return "Adoption #" + adoptionId + " (Animal: " + animalId + ", Adopter: " + adopterId + ")";
    }
}
