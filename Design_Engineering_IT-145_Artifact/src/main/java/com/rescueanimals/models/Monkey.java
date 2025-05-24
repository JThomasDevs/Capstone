package com.rescueanimals.models;

/**
 * Monkey class extends RescueAnimal.
 * Represents a monkey in the rescue system, adding species and measurement fields.
 * This design allows for type-specific validation and serialization.
 */
public class Monkey extends RescueAnimal {
    private String tailLength;
    private String height;
    private String bodyLength;
    private String species;

    /**
     * Constructs a Monkey with all required fields.
     * @param name Monkey's name
     * @param tailLength Tail length in feet
     * @param height Height in feet
     * @param bodyLength Body length in feet
     * @param species Monkey's species
     * @param gender Monkey's gender
     * @param age Monkey's age
     * @param weight Monkey's weight
     * @param acquisitionDate Date of acquisition
     * @param acquisitionCountry Country of acquisition
     * @param trainingStatus Current training status
     * @param reserved Reservation status
     * @param inServiceCountry Country where the monkey is in service
     */
    public Monkey(String name, String tailLength, String height, String bodyLength,
                 String species, String gender, String age, String weight,
                 String acquisitionDate, String acquisitionCountry,
                 String trainingStatus, boolean reserved, String inServiceCountry) {
        super(name, gender, age, weight, acquisitionDate, acquisitionCountry,
              trainingStatus, reserved, inServiceCountry);
        this.tailLength = tailLength;
        this.height = height;
        this.bodyLength = bodyLength;
        this.species = species;
    }

    /**
     * Gets the tail length.
     * @return tail length in feet
     */
    public String getTailLength() { return tailLength; }
    public void setTailLength(String tailLength) { this.tailLength = tailLength; }

    /**
     * Gets the height.
     * @return height in feet
     */
    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    /**
     * Gets the body length.
     * @return body length in feet
     */
    public String getBodyLength() { return bodyLength; }
    public void setBodyLength(String bodyLength) { this.bodyLength = bodyLength; }

    /**
     * Gets the species.
     * @return species
     */
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
} 