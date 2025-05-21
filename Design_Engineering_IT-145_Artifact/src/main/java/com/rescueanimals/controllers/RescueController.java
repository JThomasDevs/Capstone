package com.rescueanimals.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rescueanimals.models.*;
import java.util.ArrayList;

public class RescueController {
    private static final ArrayList<Dog> dogList = new ArrayList<>();
    private static final ArrayList<Monkey> monkeyList = new ArrayList<>();
    private static final ArrayList<String> monkeySpeciesList = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RescueController() {
        initializeMonkeySpecies();
    }

    private void initializeMonkeySpecies() {
        monkeySpeciesList.add("capuchin");
        monkeySpeciesList.add("guenon");
        monkeySpeciesList.add("macaque");
        monkeySpeciesList.add("marmoset");
        monkeySpeciesList.add("squirrel monkey");
        monkeySpeciesList.add("tamarin");
    }

    public String listDogs() {
        return gson.toJson(dogList);
    }

    public String listMonkeys() {
        return gson.toJson(monkeyList);
    }

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
        return gson.toJson(availableAnimals);
    }

    public boolean addDog(Dog dog) {
        for (Dog existingDog : dogList) {
            if (existingDog.getName().equalsIgnoreCase(dog.getName())) {
                return false;
            }
        }
        dogList.add(dog);
        return true;
    }

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