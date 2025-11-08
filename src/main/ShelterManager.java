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
import java.time.format.DateTimeParseException;
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
    private ArrayList<MedicalRecordModel> medicalRecords;

    public ShelterManager() {
        instance = this;
        animals = new ArrayList<>();
        adopters = new ArrayList<>();
        adoptions = new ArrayList<>();
        medicalRecords = new ArrayList<>();
        
        // --- MAKE SURE THESE ARE CALLED ---
        loadAnimalsFromJSON();
        loadAdoptersFromJSON(); // <-- This is the important one
        loadMedicalRecordsFromJSON();
//         loadAdoptionsFromJSON(); // (Add this when ready) 

        System.out.println("ShelterManager initialized and all data loaded.");
    }

    public ArrayList<Animal> getAnimals() {
        return this.animals;
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

    public void loadAnimalsFromJSON() {
        JSONArray animalList = readAnimalsJSON(); // Read the file
        animals.clear(); // Clear the existing list

        // add each animal to the internal list
        for (Object obj : animalList) {
            JSONObject animal = (JSONObject) obj;

            try {
                // Parse all fields from JSON
                int id = ((Long) animal.get("id")).intValue();
                String name = (String) animal.get("name");
                String species = (String) animal.get("species");
                String breed = (String) animal.get("breed");
                int age = ((Long) animal.get("age")).intValue();
                String gender = (String) animal.get("gender");

                // Safely parse SourceType
                String sourceString = (String) animal.get("sourceType");
                SourceType sourceType = SourceType.fromString(sourceString);

                String adoptionStatus = (String) animal.get("adoptionStatus");
                String healthStatus = (String) animal.get("healthStatus");

                // --- 1. HANDLE dateArrived ---
                String dateString = (String) animal.get("dateArrived");
                LocalDate dateArrived = null; // Default to null

                if (dateString != null && !dateString.isEmpty()) {
                    try {
                        // This will parse "2025-11-15"
                        dateArrived = LocalDate.parse(dateString);
                    } catch (DateTimeParseException e) {
                        // This will catch bad data like "2D025-11-15"
                        System.out.println("Could not parse date '" + dateString + "' for animal " + name + ". Setting to null.");
                    }
                }
                // -----------------------------

                // Create the correct object based on species (Polymorphism)
                Animal newAnimal = null;
                switch (species) {
                    case "Dog":
                        newAnimal = new Dog(id, name, breed, age, gender, sourceType, adoptionStatus, healthStatus, dateArrived); // <-- 2. PASS TO CONSTRUCTOR
                        break;
                    case "Cat":
                        newAnimal = new Cat(id, name, breed, age, gender, sourceType, adoptionStatus, healthStatus, dateArrived); // <-- 2. PASS TO CONSTRUCTOR
                        break;
                    case "Bird":
                        newAnimal = new Bird(id, name, breed, age, gender, sourceType, adoptionStatus, healthStatus, dateArrived); // <-- 2. PASS TO CONSTRUCTOR
                        break;
                    default:
                        System.out.println("Unknown species: " + species);
                }

                if (newAnimal != null) {
                    this.animals.add(newAnimal); // Add to the manager's list
                }

            } catch (Exception e) {
                System.out.println("Failed to load animal: " + animal.get("name") + ". Error: " + e.getMessage());
                e.printStackTrace(); // Good for debugging
            }
        }
        System.out.println("Loaded " + animals.size() + " animals into ShelterManager.");
    }

    public void populateAnimalTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table

        // Loop through the 'animals' ArrayList that is already in memory
        for (Animal animal : this.animals) {

            // Add a new row using the getter methods from the Animal object
            // This matches the 8 columns in AnimalProfiles
            model.addRow(new Object[]{
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getAge(),
                animal.getGender(),
                animal.getSourceType(), // This correctly adds the Enum
                animal.getAdoptionStatus(),
                animal.getHealthStatus(), // This correctly adds the Enum
                animal.getDateArrived()
            // We don't add healthStatus or dateArrived here
            // because your table only has 8 columns
            });
        }
    }

    public void saveAnimals() {
        JSONArray animalArray = new JSONArray();

        // Loop through the ArrayList, not the table
        for (Animal animal : this.animals) {
            JSONObject animalDetails = new JSONObject();
            animalDetails.put("id", animal.getId());
            animalDetails.put("name", animal.getName());
            animalDetails.put("species", animal.getSpecies());
            animalDetails.put("breed", animal.getBreed());
            animalDetails.put("age", animal.getAge());
            animalDetails.put("gender", animal.getGender());

            // Convert Enum back to String for JSON
            animalDetails.put("sourceType", animal.getSourceType().name());

            animalDetails.put("adoptionStatus", animal.getAdoptionStatus());
            animalDetails.put("healthStatus", animal.getHealthStatus());

            // --- 3. HANDLE dateArrived ---
            // Convert LocalDate back to String, handle null
            if (animal.getDateArrived() != null) {
                animalDetails.put("dateArrived", animal.getDateArrived().toString());
            } else {
                animalDetails.put("dateArrived", null);
            }
            // -----------------------------

            animalArray.add(animalDetails);
        }

        // Write to file (overwrite)
        try (FileWriter writer = new FileWriter(FILE_PATH_ANIMALS, false)) {
            writer.write(animalArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save animals: " + e.getMessage());
        }
    }

    public void addAnimal(Animal animal) {
        if (animal != null) {
            this.animals.add(animal);
            saveAnimals(); // Save the new animal to animals.json
        }
    }

    // =========================================================================
    // ADOPTER MANAGEMENT (NEW)
    // =========================================================================
    private static JSONArray readAdoptersJSON() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(FILE_PATH_ADOPTERS)) {
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.out.println("Error reading adopters.json: " + e.getMessage());
            return new JSONArray();
        }
    }

    public void loadAdoptersFromJSON() {
       JSONArray adopterList = readAdoptersJSON();
        adopters.clear();
        for (Object obj : adopterList) {
            JSONObject adopter = (JSONObject) obj;
            try {
                int id = ((Long) adopter.get("id")).intValue();
                String name = (String) adopter.get("name");
                String contact = (String) adopter.get("contactPhone");
                String address = (String) adopter.get("address");

                Adopter newAdopter = new Adopter(id, name, contact, address);
                this.adopters.add(newAdopter);
            } catch (Exception e) {
                System.out.println("Failed to load adopter: " + adopter.get("name") + ". Error: " + e.getMessage());
            }
        }
        System.out.println("Loaded " + adopters.size() + " adopters.");
    }

    public Animal findAnimalById(int id) {
        for (Animal animal : this.animals) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null; // Not found
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
        for (Adopter adopter : this.adopters) {
            JSONObject adopterDetails = new JSONObject();
            adopterDetails.put("id", adopter.getAdopterId());
            adopterDetails.put("name", adopter.getName());
            adopterDetails.put("contactPhone", adopter.getContactPhone());
            adopterDetails.put("address", adopter.getAddress());
            adopterArray.add(adopterDetails);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH_ADOPTERS, false)) { // false = overwrite
            writer.write(adopterArray.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save adopters: " + e.getMessage());
        }
    }

    public void addAdopter(Adopter adopter) {
        if (adopter != null) {
            this.adopters.add(adopter);
            saveAdopters(); // Save to file immediately
        }
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

    public void loadMedicalRecordsFromJSON() {
        JSONArray recordList = readMedicalRecordsJSON(); // (Assumes you have a readMedicalRecordsJSON() static method)
        medicalRecords.clear();

        for (Object obj : recordList) {
            JSONObject record = (JSONObject) obj;

            try {
                int recordId = ((Long) record.get("id")).intValue();
                int animalId = ((Long) record.get("animalId")).intValue();
                String diagnosis = (String) record.get("diagnosis");
                String treatment = (String) record.get("treatment");
                String notes = (String) record.get("notes"); // Get notes (might be null)
                LocalDate date = LocalDate.parse((String) record.get("date"));

                // Use the constructor from MedicalRecordModel.java
                MedicalRecordModel newRecord = new MedicalRecordModel(recordId, animalId, diagnosis, treatment, date, notes);
                this.medicalRecords.add(newRecord);

            } catch (Exception e) {
                System.out.println("Failed to load medical record: " + record.get("id") + ". Error: " + e.getMessage());
            }
        }
        System.out.println("Loaded " + medicalRecords.size() + " medical records.");
    }

    public void populateMedicalRecordTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table

        for (MedicalRecordModel record : this.medicalRecords) {
            // --- This is where we get the name ---
            Animal animal = findAnimalById(record.getAnimalId());
            String animalName = "Unknown (ID: " + record.getAnimalId() + ")"; // Default if animal is missing
            if (animal != null) {
                animalName = animal.getName();
            }
            // ------------------------------------

            model.addRow(new Object[]{
                record.getRecordId(),
                animalName, // <-- Here is the animal NAME
                record.getDate(),
                record.getDiagnosis(),
                record.getTreatment()
            // (Assumes your table has 5 columns: ID, Animal Name, Date, Diagnosis, Treatment)
            // (Adjust this row to match your JTable's columns in the design)
            });
        }
    }

    // TEST
    public void saveMedicalRecords() {
        JSONArray recordArray = new JSONArray();

        // Loop over the ArrayList of MedicalRecordModel OBJECTS
        for (model.MedicalRecordModel a : medicalRecords) {
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

    public void addMedicalRecord(MedicalRecordModel a) {
        medicalRecords.add(a);
        saveMedicalRecords();
    }

    public ArrayList<MedicalRecordModel> getMedicalRecords() {
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
                .mapToInt(MedicalRecordModel::getRecordId) // <-- THIS IS THE FIX
                .max()
                .orElse(0) + 1;
    }
}
