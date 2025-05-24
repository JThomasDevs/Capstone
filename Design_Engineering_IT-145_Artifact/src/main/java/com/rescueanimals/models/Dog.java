package com.rescueanimals.models;

/**
 * Dog class extends RescueAnimal.
 * Represents a dog in the rescue system, adding breed as a unique field.
 * This separation allows for type-specific logic and serialization.
 */
public class Dog extends RescueAnimal {
    private String breed;

    /**
     * Constructs a Dog with all required fields.
     * @param name Dog's name
     * @param breed Dog's breed
     * @param gender Dog's gender
     * @param age Dog's age
     * @param weight Dog's weight
     * @param acquisitionDate Date of acquisition
     * @param acquisitionCountry Country of acquisition
     * @param trainingStatus Current training status
     * @param reserved Reservation status
     * @param inServiceCountry Country where the dog is in service
     */
    public Dog(String name, String breed, String gender, String age, String weight,
              String acquisitionDate, String acquisitionCountry, String trainingStatus,
              boolean reserved, String inServiceCountry) {
        super(name, gender, age, weight, acquisitionDate, acquisitionCountry,
              trainingStatus, reserved, inServiceCountry);
        this.breed = breed;
    }

    /**
     * Gets the dog's breed.
     * @return breed
     */
    public String getBreed() { return breed; }

    /**
     * Sets the dog's breed.
     * @param breed breed name
     */
    public void setBreed(String breed) { this.breed = breed; }
} 