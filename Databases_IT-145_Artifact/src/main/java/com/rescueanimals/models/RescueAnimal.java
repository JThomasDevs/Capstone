// RescueAnimal is now a non-entity class for business logic only.
// It is not mapped to the database.

package com.rescueanimals.models;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class RescueAnimal {
    @Id
    private String name;
    private String gender;
    private int age;
    private double weight;
    private String acquisitionDate;
    private String acquisitionCountry;
    private String trainingStatus;
    private boolean reserved;
    private String inServiceCountry;

    public RescueAnimal() {}

    public RescueAnimal(String name, String gender, int age, double weight,
                       String acquisitionDate, String acquisitionCountry,
                       String trainingStatus, boolean reserved, String inServiceCountry) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.acquisitionDate = acquisitionDate;
        this.acquisitionCountry = acquisitionCountry;
        this.trainingStatus = trainingStatus;
        this.reserved = reserved;
        this.inServiceCountry = inServiceCountry;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getAcquisitionDate() { return acquisitionDate; }
    public void setAcquisitionDate(String acquisitionDate) { this.acquisitionDate = acquisitionDate; }
    public String getAcquisitionCountry() { return acquisitionCountry; }
    public void setAcquisitionCountry(String acquisitionCountry) { this.acquisitionCountry = acquisitionCountry; }
    public String getTrainingStatus() { return trainingStatus; }
    public void setTrainingStatus(String trainingStatus) { this.trainingStatus = trainingStatus; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    public String getInServiceCountry() { return inServiceCountry; }
    public void setInServiceCountry(String inServiceCountry) { this.inServiceCountry = inServiceCountry; }
} 