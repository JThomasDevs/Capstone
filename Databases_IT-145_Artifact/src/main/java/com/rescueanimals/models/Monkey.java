package com.rescueanimals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Monkey class extends RescueAnimal.
 * Represents a monkey in the rescue system, adding species and measurement fields.
 * This design allows for type-specific validation and serialization.
 */
@Entity
public class Monkey extends RescueAnimal {
    @Column
    private String species;
    @Column
    private double tailLength;
    @Column
    private double height;
    @Column
    private double bodyLength;

    // Default constructor required by JPA
    public Monkey() {
        super();
    }

    /**
     * Constructs a Monkey with all required fields.
     * @param name Monkey's name
     * @param gender Monkey's gender
     * @param age Monkey's age
     * @param weight Monkey's weight
     * @param acquisitionDate Date of acquisition
     * @param acquisitionCountry Country of acquisition
     * @param trainingStatus Current training status
     * @param reserved Reservation status
     * @param inServiceCountry Country where the monkey is in service
     * @param species Monkey's species
     * @param tailLength Tail length in feet
     * @param height Height in feet
     * @param bodyLength Body length in feet
     */
    public Monkey(String name, String gender, int age, double weight,
                 String acquisitionDate, String acquisitionCountry,
                 String trainingStatus, boolean reserved, String inServiceCountry,
                 String species, double tailLength, double height, double bodyLength) {
        super(name, gender, age, weight, acquisitionDate, acquisitionCountry,
              trainingStatus, reserved, inServiceCountry);
        this.species = species;
        this.tailLength = tailLength;
        this.height = height;
        this.bodyLength = bodyLength;
    }

    // Getters and setters for all fields
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public double getTailLength() { return tailLength; }
    public void setTailLength(double tailLength) { this.tailLength = tailLength; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getBodyLength() { return bodyLength; }
    public void setBodyLength(double bodyLength) { this.bodyLength = bodyLength; }
} 