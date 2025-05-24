package com.rescueanimals.controllers;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rescueanimals.models.Dog;
import com.rescueanimals.models.Monkey;
import com.rescueanimals.models.RescueAnimal;

/**
 * Handles all business logic for the rescue animal system.
 * Acts as the service layer between the REST API (RescueServer) and the data models.
 * This separation allows for easier testing and future expansion (e.g., database integration).
 */
public class RescueController {
    private static final ArrayList<Dog> dogList = new ArrayList<>();
    private static final ArrayList<Monkey> monkeyList = new ArrayList<>();
    private static final ArrayList<String> monkeySpeciesList = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /**
     * Static initialization block.
     * 
     * This block is used to populate the species and default animal lists only once for the entire
     * application lifetime. By using a static block, we ensure that all instances of RescueController
     * share the same initial data, prevent accidental re-initialization (which could cause duplicates),
     * and avoid putting this logic in a constructor (which would run every time a controller is created).
     * This design is important for maintaining consistent state and performance across the application.
     */
    static {
        initializeMonkeySpecies();
        initializeDefaultAnimals();
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
     * Initializes the default animals for the system.
     * 
     * This method is called from the static block to provide a known starting state for the application.
     * It helps with testing, demonstration, and ensures the system is never empty on startup.
     */
    private static void initializeDefaultAnimals() {
        // Add the original test dogs
        dogList.add(new Dog("Spot", "German Shepherd", "male", "1", "25.6",
                           "05-12-2019", "United States", "intake",
                           false, "United States"));
        dogList.add(new Dog("Rex", "Great Dane", "male", "3", "35.2",
                           "02-03-2020", "United States", "in service",
                           false, "United States"));
        dogList.add(new Dog("Bella", "Chihuahua", "female", "4", "25.6",
                           "12-12-2019", "Canada", "in service",
                           true, "Canada"));

        // Add the original test monkeys
        monkeyList.add(new Monkey("Winston", "1.2", "3.3", "4",
                                 "Capuchin", "male", "2", "12.5",
                                 "05-12-2019", "United States", "intake",
                                 false, "United States"));
        monkeyList.add(new Monkey("Marcel", "1.5", "2.8", "3.2",
                                 "Guenon", "female", "3", "10.5",
                                 "02-03-2020", "United States", "in service",
                                 false, "United States"));
        monkeyList.add(new Monkey("Shmooby", "1.8", "2.1", "2.8",
                                 "Marmoset", "male", "4", "8.5",
                                 "12-12-2019", "Canada", "in service",
                                 true, "Canada"));
    }

    /**
     * Utility wrapper for returning lists of animals in API responses.
     * 
     * This generic inner class allows the controller to package up lists of dogs, monkeys, or available animals
     * in a consistent JSON structure for the frontend. By using a single wrapper, we avoid duplicating code for
     * each type of list response and keep API responses uniform. The static factory methods make it easy to create
     * responses with only the relevant field populated, while the use of generics allows flexibility for different
     * animal types. This class is used in listDogs(), listMonkeys(), and listAvailable() to serialize the correct
     * list of animals for each endpoint.
     */
    private static class AnimalListJson<T> {
        final ArrayList<T> dogs;
        final ArrayList<T> monkeys;
        final ArrayList<T> available;

        AnimalListJson(ArrayList<T> dogs, ArrayList<T> monkeys, ArrayList<T> available) {
            this.dogs = dogs;
            this.monkeys = monkeys;
            this.available = available;
        }

        AnimalListJson(ArrayList<T> dogs) {
            this.dogs = dogs;
            this.monkeys = null;
            this.available = null;
        }

        static <T> AnimalListJson<T> forMonkeys(ArrayList<T> monkeys) {
            return new AnimalListJson<>(null, monkeys, null);
        }

        static <T> AnimalListJson<T> forAvailable(ArrayList<T> available) {
            return new AnimalListJson<>(null, null, available);
        }
    }

    /**
     * Returns a JSON string of all dogs.
     * 
     * This method serializes the current list of dogs using Gson. Returning JSON here keeps the controller
     * decoupled from the API layer, allowing for easier testing and future changes to serialization.
     * @return JSON array of dogs
     */
    public String listDogs() {
        return gson.toJson(new AnimalListJson<>(new ArrayList<>(dogList)));
    }

    /**
     * Returns a JSON string of all monkeys.
     * 
     * This method serializes the current list of monkeys using Gson. Returning JSON here keeps the controller
     * decoupled from the API layer, allowing for easier testing and future changes to serialization.
     * @return JSON array of monkeys
     */
    public String listMonkeys() {
        return gson.toJson(AnimalListJson.forMonkeys(new ArrayList<>(monkeyList)));
    }

    /**
     * Returns a JSON string of all available (unreserved) animals.
     * 
     * This method filters the lists for animals that are "in service" and not reserved, then serializes the result.
     * This logic is centralized here to keep the API layer simple and maintainable.
     * @return JSON array of available animals
     */
    public String listAvailable() {
        ArrayList<RescueAnimal> availableAnimals = new ArrayList<>();
        for (Dog dog : dogList) {
            if (dog.getTrainingStatus().equals("in service") && !dog.getReserved()) {
                availableAnimals.add(dog);
            }
        }
        for (Monkey monkey : monkeyList) {
            if (monkey.getTrainingStatus().equals("in service") && !monkey.getReserved()) {
                availableAnimals.add(monkey);
            }
        }
        return gson.toJson(AnimalListJson.forAvailable(availableAnimals));
    }

    /**
     * Adds a new dog to the system.
     * @param dog Dog to add
     * @return true if added successfully, false if duplicate name
     */
    public boolean addDog(Dog dog) {
        for (Dog existingDog : dogList) {
            if (existingDog.getName().equalsIgnoreCase(dog.getName())) {
                return false;
            }
        }
        dogList.add(dog);
        return true;
    }

    /**
     * Adds a new monkey to the system.
     * @param monkey Monkey to add
     * @return true if added successfully, false if duplicate name
     */
    public boolean addMonkey(Monkey monkey) {
        if (!monkeySpeciesList.contains(monkey.getSpecies().toLowerCase())) {
            return false;
        }
        for (Monkey existingMonkey : monkeyList) {
            if (existingMonkey.getName().equalsIgnoreCase(monkey.getName())) {
                return false;
            }
        }
        monkeyList.add(monkey);
        return true;
    }

    /**
     * Reserves an animal for service in a given country.
     * @param type "dog" or "monkey"
     * @param name Animal's name
     * @param country Service country
     * @return true if reservation succeeded, false otherwise
     */
    public boolean reserveAnimal(String type, String name, String country) {
        if (type.equalsIgnoreCase("dog")) {
            for (Dog dog : dogList) {
                if (dog.getName().equalsIgnoreCase(name) && 
                    dog.getTrainingStatus().equals("in service") && 
                    !dog.getReserved()) {
                    dog.setReserved(true);
                    dog.setInServiceCountry(country);
                    return true;
                }
            }
        } else if (type.equalsIgnoreCase("monkey")) {
            for (Monkey monkey : monkeyList) {
                if (monkey.getName().equalsIgnoreCase(name) && 
                    monkey.getTrainingStatus().equals("in service") && 
                    !monkey.getReserved()) {
                    monkey.setReserved(true);
                    monkey.setInServiceCountry(country);
                    return true;
                }
            }
        }
        return false;
    }
} 