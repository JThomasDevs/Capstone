package com.rescueanimals.models;

public class Dog extends RescueAnimal {
    private String breed;

    public Dog(String name, String breed, String gender, String age, String weight,
              String acquisitionDate, String acquisitionCountry, String trainingStatus,
              boolean reserved, String inServiceCountry) {
        super(name, gender, age, weight, acquisitionDate, acquisitionCountry,
              trainingStatus, reserved, inServiceCountry);
        this.breed = breed;
    }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
} 