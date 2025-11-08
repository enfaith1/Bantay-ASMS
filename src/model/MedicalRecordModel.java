/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Aspire-5
 */
import java.time.LocalDate;

public class MedicalRecordModel {

    private int recordId;
    private int animalId;
    private String diagnosis;
    private String treatment;
    private LocalDate date;
    private String notes;

    public MedicalRecordModel(int recordId, int animalId, String diagnosis, String treatment, LocalDate date, String notes) {
        this.recordId = recordId;
        this.animalId = animalId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
        this.notes = notes;
    }

    // Getters
    public int getRecordId() {
        return recordId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Animal #" + animalId + " - " + diagnosis + " (" + date + ")";
    }
}
