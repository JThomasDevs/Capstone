package com.rescueanimals.models;

public class Monkey extends RescueAnimal {
    private String tailLength;
    private String height;
    private String bodyLength;
    private String species;

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

    // Getters and Setters
    public String getTailLength() { return tailLength; }
    public void setTailLength(String tailLength) { this.tailLength = tailLength; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getBodyLength() { return bodyLength; }
    public void setBodyLength(String bodyLength) { this.bodyLength = bodyLength; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
} 