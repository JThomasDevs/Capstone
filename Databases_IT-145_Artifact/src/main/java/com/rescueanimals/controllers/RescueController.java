package com.rescueanimals.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rescueanimals.models.Dog;
import com.rescueanimals.models.JPAUtil;
import com.rescueanimals.models.Monkey;
import com.rescueanimals.models.dao.DogDAO;
import com.rescueanimals.models.dao.MonkeyDAO;

/**
 * Handles all business logic for the rescue animal system.
 * Acts as the service layer between the REST API (RescueServer) and the data models.
 * This separation allows for easier testing and future expansion.
 */
public class RescueController {
    private static final ArrayList<String> monkeySpeciesList = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private final DogDAO dogDAO;
    private final MonkeyDAO monkeyDAO;

    /**
     * Static initialization block.
     * 
     * This block is used to populate the species list only once for the entire
     * application lifetime. By using a static block, we ensure that all instances of RescueController
     * share the same initial data, prevent accidental re-initialization (which could cause duplicates),
     * and avoid putting this logic in a constructor (which would run every time a controller is created).
     */
    static {
        initializeMonkeySpecies();
    }

    public RescueController() {
        this.dogDAO = new DogDAO();
        this.monkeyDAO = new MonkeyDAO();
        initializeTestData();
    }

    public void shutdown() {
        JPAUtil.shutdown();
    }

    /**
     * Initializes the list of valid monkey species.
     * 
     * This method is called from the static block to ensure the species list is populated only once.
     * Keeping this logic separate allows for easy updates to the list and clear separation of concerns.
     */
    private static void initializeMonkeySpecies() {
        monkeySpeciesList.add("capuchin");
        monkeySpeciesList.add("guenon");
        monkeySpeciesList.add("macaque");
        monkeySpeciesList.add("marmoset");
        monkeySpeciesList.add("squirrel monkey");
        monkeySpeciesList.add("tamarin");
    }

    /**
     * Initializes test data in the database if it's empty.
     * This ensures the system always has some data for testing and demonstration.
     */
    private void initializeTestData() {
        // Create test dogs
        Dog dog1 = new Dog("Max", "male", 3, 30.5, "2024-01-01", "USA", "intake", false, null, "German Shepherd");
        Dog dog2 = new Dog("Bella", "female", 2, 25.0, "2024-01-02", "Canada", "in service", false, "Canada", "Labrador");
        Dog dog3 = new Dog("Rocky", "male", 4, 32.0, "2024-01-03", "UK", "intake", false, null, "Golden Retriever");
        
        // Only insert if not already present
        if (dogDAO.getDogByName(dog1.getName()) == null) dogDAO.saveDog(dog1);
        if (dogDAO.getDogByName(dog2.getName()) == null) dogDAO.saveDog(dog2);
        if (dogDAO.getDogByName(dog3.getName()) == null) dogDAO.saveDog(dog3);
        
        // Create test monkeys
        Monkey monkey1 = new Monkey("Charlie", "male", 5, 15.0, "2024-01-01", "Brazil", "intake", false, null, "Capuchin", 20.0, 30.0, 40.0);
        Monkey monkey2 = new Monkey("Luna", "female", 3, 12.0, "2024-01-02", "Peru", "in service", false, "Peru", "Spider Monkey", 25.0, 35.0, 45.0);
        Monkey monkey3 = new Monkey("Oscar", "male", 4, 18.0, "2024-01-03", "Costa Rica", "in service", false, "Costa Rica", "Howler Monkey", 22.0, 32.0, 42.0);
        
        // Only insert if not already present
        if (monkeyDAO.getMonkeyByName(monkey1.getName()) == null) monkeyDAO.saveMonkey(monkey1);
        if (monkeyDAO.getMonkeyByName(monkey2.getName()) == null) monkeyDAO.saveMonkey(monkey2);
        if (monkeyDAO.getMonkeyByName(monkey3.getName()) == null) monkeyDAO.saveMonkey(monkey3);
    }

    /**
     * Lists all dogs in the system.
     * @return List of all dogs
     */
    public List<Dog> getAllDogs() {
        return dogDAO.getAllDogs();
    }

    /**
     * Lists all monkeys in the system.
     * @return List of all monkeys
     */
    public List<Monkey> getAllMonkeys() {
        return monkeyDAO.getAllMonkeys();
    }

    /**
     * Gets a dog by its name.
     * @param name Dog's name
     * @return Dog object
     */
    public Dog getDogByName(String name) {
        return dogDAO.getDogByName(name);
    }

    /**
     * Gets a monkey by its name.
     * @param name Monkey's name
     * @return Monkey object
     */
    public Monkey getMonkeyByName(String name) {
        return monkeyDAO.getMonkeyByName(name);
    }

    /**
     * Saves a dog to the database.
     * @param dog Dog to save
     */
    public void saveDog(Dog dog) {
        dogDAO.saveDog(dog);
    }

    /**
     * Saves a monkey to the database.
     * @param monkey Monkey to save
     */
    public void saveMonkey(Monkey monkey) {
        monkeyDAO.saveMonkey(monkey);
    }

    /**
     * Updates a dog in the database.
     * @param dog Dog to update
     */
    public void updateDog(Dog dog) {
        dogDAO.updateDog(dog);
    }

    /**
     * Updates a monkey in the database.
     * @param monkey Monkey to update
     */
    public void updateMonkey(Monkey monkey) {
        monkeyDAO.updateMonkey(monkey);
    }

    /**
     * Deletes a dog from the database.
     * @param name Dog's name
     */
    public void deleteDog(String name) {
        dogDAO.deleteDog(name);
    }

    /**
     * Deletes a monkey from the database.
     * @param name Monkey's name
     */
    public void deleteMonkey(String name) {
        monkeyDAO.deleteMonkey(name);
    }

    /**
     * Gets dogs by training status.
     * @param status Training status
     * @return List of dogs
     */
    public List<Dog> getDogsByTrainingStatus(String status) {
        return dogDAO.getDogsByTrainingStatus(status);
    }

    /**
     * Gets monkeys by training status.
     * @param status Training status
     * @return List of monkeys
     */
    public List<Monkey> getMonkeysByTrainingStatus(String status) {
        return monkeyDAO.getMonkeysByTrainingStatus(status);
    }

    /**
     * Reserves an animal for service in a given country.
     * @param type "dog" or "monkey"
     * @param name Animal's name
     * @param country Service country
     * @return true if reservation succeeded, false otherwise
     */
    public boolean reserveAnimal(String type, String name, String country) {
        try {
            if (type.equalsIgnoreCase("dog")) {
                Dog dog = getDogByName(name);
                if (dog != null) {
                    dog.setReserved(true);
                    dog.setInServiceCountry(country);
                    updateDog(dog);
                    return true;
                }
            } else if (type.equalsIgnoreCase("monkey")) {
                Monkey monkey = getMonkeyByName(name);
                if (monkey != null) {
                    monkey.setReserved(true);
                    monkey.setInServiceCountry(country);
                    updateMonkey(monkey);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error reserving animal: " + e.getMessage());
            return false;
        }
    }

    public Map<String, List<?>> getAvailableAnimals() {
        Map<String, List<?>> result = new HashMap<>();
        List<Dog> dogs = dogDAO.getAvailableDogs();
        List<Monkey> monkeys = monkeyDAO.getAvailableMonkeys();
        result.put("dogs", dogs);
        result.put("monkeys", monkeys);
        return result;
    }
} 