/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Aspire-5
 */
import model.SourceType;

public abstract class Animal {

    // These fields match your new animals.json
    protected int id;
    protected String name;
    protected String species;
    protected String breed;
    protected int age;
    protected String gender;
    protected SourceType sourceType;
    protected String adoptionStatus;
    protected String healthStatus;

    public Animal(int id, String name, String species, String breed, int age,
            String gender, SourceType sourceType, String adoptionStatus,
            String healthStatus) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.sourceType = sourceType;
        this.adoptionStatus = adoptionStatus;
        this.healthStatus = healthStatus;
    }

    // Abstract method for Polymorphism
    public abstract String getDetails();

    // Getters for all fields (needed for the table)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    // toString() for the JComboBox
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

    // ENCAPSULATION: Public setters for fields that can change
    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
}
