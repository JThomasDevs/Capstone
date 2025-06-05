package com.rescueanimals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Dog class extends RescueAnimal.
 * Represents a dog in the rescue system, adding breed as a unique field.
 * This separation allows for type-specific logic and serialization.
 */
@Entity
public class Dog extends RescueAnimal {
    @Column(nullable = false)
    private String breed;

    // Default constructor required by JPA
    public Dog() {
        super();
    }

    /**
     * Constructs a Dog with all required fields.
     * @param name Dog's name
     * @param gender Dog's gender
     * @param age Dog's age
     * @param weight Dog's weight
     * @param acquisitionDate Date of acquisition
     * @param acquisitionCountry Country of acquisition
     * @param trainingStatus Current training status
     * @param reserved Reservation status
     * @param inServiceCountry Country where the dog is in service
     * @param breed Dog's breed
     */
    public Dog(String name, String gender, int age, double weight,
              String acquisitionDate, String acquisitionCountry,
              String trainingStatus, boolean reserved, String inServiceCountry,
              String breed) {
        super(name, gender, age, weight, acquisitionDate, acquisitionCountry,
              trainingStatus, reserved, inServiceCountry);
        this.breed = breed;
    }

    // Getters and setters for all fields
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
} 