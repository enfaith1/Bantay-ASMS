/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Aspire-5
 */
import model.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import java.util.regex.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class ShelterManager {

    public static ShelterManager instance;
    public static final String FILE_PATH_ADMINS = "src\\data\\admins.json";
    private static final String FILE_PATH_ANIMALS = "src\\data\\animals.json";
    public static final String FILE_PATH_ADOPTERS = "src\\data\\adopters.json";
    public static final String FILE_PATH_ADOPTIONS = "src\\data\\adoptions.json";
    public static final String FILE_PATH_MEDICAL_RECORDS = "src\\data\\medical_records.json";
    public static final String FILE_PATH_PAYMENTS = "src\\data\\payments.json";

    private ArrayList<model.Animal> animals;
    private ArrayList<Adopter> adopters;
    private ArrayList<Adoption> adoptions;
    private ArrayList<MedicalRecord> medicalRecords;

    public ShelterManager() {
        instance = this;
        animals = new ArrayList<>();
        adopters = new ArrayList<>();
        adoptions = new ArrayList<>();
        medicalRecords = new ArrayList<>();

        // Load all data on startup
//        loadAdoptions();
    }

    private static JSONArray readAnimalsJSON() {
        JSONParser parser = new JSONParser();
        JSONArray animalList = new JSONArray();

        File file = new File(FILE_PATH_ANIMALS); // Use the correct file path

        if (!file.exists() || file.length() == 0) {
            System.out.println("animals.json is empty or not found.");
            return animalList; // Return the empty list
        }

        try (FileReader reader = new FileReader(file)) {
            animalList = (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Error reading animals file: " + e.getMessage());
        }

        return animalList;
    }

    public static void loadAnimalsFromJSON(JTable table) {
        JSONArray animalList = readAnimalsJSON(); // Read the file
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table

        // add each animal to the table
        for (Object obj : animalList) {
            JSONObject animal = (JSONObject) obj;

            // Use .get("keyName") just like your example
            // The order here MUST match your JTable columns!
            model.addRow(new Object[]{
                animal.get("id"),
                animal.get("name"),
                animal.get("species"),
                animal.get("breed"),
                animal.get("age"),
                animal.get("gender"),
                animal.get("sourceType"),
                animal.get("adoptionStatus"),
                animal.get("healthStatus")
            });
        }
    }

    public void saveAnimals() {
        JSONArray animalArray = new JSONArray();

        for (Animal a : animals) {
            JSONObject obj = new JSONObject();
            obj.put("id", a.getId());
            obj.put("name", a.getName());
            obj.put("species", a.getSpecies());
            obj.put("age", a.getAge());

            // Detect subclass type and save specific data
            if (a instanceof Dog d) {
                obj.put("breed", d.getBreed());
                obj.put("isTrained", d.isTrained());
            } else if (a instanceof Cat c) {
                obj.put("color", c.getColor());
                obj.put("isIndoor", c.isIndoor());
            } else if (a instanceof Bird b) {
                obj.put("speciesType", b.getSpeciesType());
                obj.put("canTalk", b.canTalk());
            }

            animalArray.add(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH_ANIMALS, false)) { // false to overwrite
            writer.write(animalArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save animals: " + e.getMessage());
        }
    }

    public void addAnimal(Animal a) {
        animals.add(a);
        saveAnimals();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    // =========================================================================
    // ADOPTER MANAGEMENT (NEW)
    // =========================================================================
    private static JSONArray readAdoptersJSON() {
        JSONParser parser = new JSONParser();
        JSONArray adopterList = new JSONArray();
        File file = new File(FILE_PATH_ADOPTERS); // Use the correct file path

        if (!file.exists() || file.length() == 0) {
            System.out.println("adopters.json is empty or not found.");
            return adopterList; // Return the empty list
        }

        try (FileReader reader = new FileReader(file)) {
            adopterList = (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Error reading adopters file: " + e.getMessage());
        }

        return adopterList;
    }

    public Animal findAnimalById(int id) {
        for (Animal animal : animals) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null;
    }

    public Adopter findAdopterById(int id) {
        for (Adopter adopter : adopters) {
            // Your Adopter model might use getId() or getAdopterId()
            // Your saveAdopters method uses "getAdopterId"
            if (adopter.getAdopterId() == id) {
                return adopter;
            }
        }
        return null;
    }

    public static void loadJoinedAdoptionHistoryFromJSON(JTable historyTable) {
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0); // Clear table

        // 1. Read all necessary JSON files
        JSONArray adoptions = readAdoptionsJSON();
        JSONArray animals = readAnimalsJSON();
        JSONArray adopters = readAdoptersJSON();

        // 2. Create HashMaps for fast lookup
        HashMap<Long, JSONObject> animalMap = new HashMap<>();
        for (Object obj : animals) {
            JSONObject animal = (JSONObject) obj;
            animalMap.put((Long) animal.get("id"), animal);
        }

        HashMap<Long, JSONObject> adopterMap = new HashMap<>();
        for (Object obj : adopters) {
            JSONObject adopter = (JSONObject) obj;
            adopterMap.put((Long) adopter.get("id"), adopter);
        }

        // 3. Loop through adoptions and build the joined rows
        for (Object obj : adoptions) {
            JSONObject adoption = (JSONObject) obj;

            long animalId = (Long) adoption.get("animalId");
            long adopterId = (Long) adoption.get("adopterId");

            // Find the matching animal and adopter from the maps
            JSONObject animal = animalMap.get(animalId);
            JSONObject adopter = adopterMap.get(adopterId);

            // Get the data, with fallbacks for missing data
            String animalName = (animal != null) ? (String) animal.get("name") : "Unknown Animal";
            String animalSpecies = (animal != null) ? (String) animal.get("species") : "N/A";
            String adopterName = (adopter != null) ? (String) adopter.get("name") : "Unknown Adopter";
            String adopterContact = (adopter != null) ? (String) adopter.get("contactPhone") : "N/A";

            // Add the joined data as a new row
            model.addRow(new Object[]{
                adoption.get("id"),
                adoption.get("date"), // Date is already a string in the JSON
                animalName,
                animalSpecies,
                adopterName,
                adopterContact
            });
        }
    }

    private static JSONArray readAdoptionsJSON() {
        JSONParser parser = new JSONParser();
        JSONArray adoptionList = new JSONArray();

        File file = new File(FILE_PATH_ADOPTIONS); // Use the correct file path

        if (!file.exists() || file.length() == 0) {
            System.out.println("adoptions.json is empty or not found.");
            return adoptionList; // Return the empty list
        }

        try (FileReader reader = new FileReader(file)) {
            adoptionList = (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Error reading adoptions file: " + e.getMessage());
        }

        return adoptionList;
    }

    public static void loadAdoptionsFromJSON(JTable table) {
        JSONArray adoptionList = readAdoptionsJSON();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table

        for (Object obj : adoptionList) {
            JSONObject adoption = (JSONObject) obj;
            model.addRow(new Object[]{
                adoption.get("id"),
                adoption.get("animalId"),
                adoption.get("adopterId"),
                adoption.get("date")
            });
        }
    }

    public void saveAdopters() {
        JSONArray adopterArray = new JSONArray();
        for (Adopter a : adopters) {
            JSONObject obj = new JSONObject();
            obj.put("id", a.getAdopterId());
            obj.put("name", a.getName());
            obj.put("contactPhone", a.getContactPhone());
            obj.put("address", a.getAddress());
            adopterArray.add(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH_ADOPTERS, false)) {
            writer.write(adopterArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save adopters: " + e.getMessage());
        }
    }

    public void addAdopter(Adopter a) {
        adopters.add(a);
        saveAdopters();
    }

    public ArrayList<Adopter> getAdopters() {
        return adopters;
    }

    // =========================================================================
    // ADOPTION MANAGEMENT
    // =========================================================================
    private void loadAdoptions() {
        adoptions.clear();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(FILE_PATH_ADOPTIONS)) {
            JSONArray adoptionArray = (JSONArray) parser.parse(reader);

            for (Object obj : adoptionArray) {
                JSONObject a = (JSONObject) obj;
                int id = ((Long) a.get("id")).intValue();
                int animalId = ((Long) a.get("animalId")).intValue();
                int adopterId = ((Long) a.get("adopterId")).intValue();
                LocalDate date = LocalDate.parse((String) a.get("date")); // Dates stored as "YYYY-MM-DD"
                adoptions.add(new Adoption(id, animalId, adopterId, date));
            }
        } catch (IOException | ParseException e) {
            System.out.println("No existing adoption data or failed to load: " + e.getMessage());
        }
    }

    public void saveAdoptions() {
        JSONArray adoptionArray = new JSONArray();
        for (Adoption a : adoptions) {
            JSONObject obj = new JSONObject();
            obj.put("id", a.getAdoptionId());
            obj.put("animalId", a.getAnimalId());
            obj.put("adopterId", a.getAdopterId());
            obj.put("date", a.getAdoptionDate().toString()); // Save date as string
            adoptionArray.add(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH_ADOPTIONS, false)) {
            writer.write(adoptionArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save adoptions: " + e.getMessage());
        }
    }

    public void addAdoption(Adoption a) {
        adoptions.add(a);
        saveAdoptions();
    }

    public ArrayList<Adoption> getAdoptions() {
        return adoptions;
    }

    // =========================================================================
    // MEDICAL RECORD MANAGEMENT 
    // =========================================================================
    private static JSONArray readMedicalRecordsJSON() {
        JSONParser parser = new JSONParser();
        JSONArray medicalList = new JSONArray();

        // 1. Create a File object from the path
        File file = new File(FILE_PATH_MEDICAL_RECORDS);

        // 2. Check if the file doesn't exist or is empty
        if (!file.exists() || file.length() == 0) {
            return medicalList; // Return the empty list
        }

        // 3. If the file has content, read it
        try (FileReader reader = new FileReader(file)) {
            // The file is not empty, so we can parse it
            medicalList = (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Error reading medical records file: " + e.getMessage());
        }

        return medicalList;
    }

    public static void loadMedicalRecordsFromJSON(JTable table) {
        JSONArray medicalList = readMedicalRecordsJSON(); // Read the file
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table

        // add each medical record to the table
        for (Object obj : medicalList) {
            JSONObject record = (JSONObject) obj;

            // Use .get("keyName") just like your example
            model.addRow(new Object[]{
                record.get("id"),
                record.get("animalId"),
                record.get("date"),
                record.get("diagnosis"),
                record.get("treatment")
            });
        }
    }

    // TEST
    public void saveMedicalRecords() {
        JSONArray recordArray = new JSONArray();

        // Loop over the ArrayList of MedicalRecord OBJECTS
        for (model.MedicalRecord a : medicalRecords) {
            JSONObject obj = new JSONObject();

            // Use the getters from the object
            obj.put("id", a.getRecordId());
            obj.put("animalId", a.getAnimalId());
            obj.put("diagnosis", a.getDiagnosis());
            obj.put("treatment", a.getTreatment());
            obj.put("date", a.getDate().toString()); // Save date as "YYYY-MM-DD"

            recordArray.add(obj);
        }

        // Now, actually write the file
        try (FileWriter writer = new FileWriter(FILE_PATH_MEDICAL_RECORDS, false)) { // false = overwrite
            writer.write(recordArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save medical records: " + e.getMessage());
        }
    }

    public void addMedicalRecord(MedicalRecord a) {
        medicalRecords.add(a);
        saveMedicalRecords();
    }

    public ArrayList<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    // =========================================================================
    // ID GENERATION (IMPROVED)
    // =========================================================================
    // Gets the highest current ID and adds 1, ensuring no duplicates
    public int getNextAnimalId() {
        return animals.stream()
                .mapToInt(Animal::getId)
                .max()
                .orElse(0) + 1;
    }

    public int getNextAdopterId() {
        return adopters.stream()
                .mapToInt(Adopter::getAdopterId)
                .max()
                .orElse(0) + 1;
    }

    public int getNextAdoptionId() {
        return adoptions.stream()
                .mapToInt(Adoption::getAdoptionId)
                .max()
                .orElse(0) + 1;
    }

    public int getNextMedicalRecordId() {
        return medicalRecords.stream()
                .mapToInt(MedicalRecord::getId)
                .max()
                .orElse(0) + 1;
    }
}
