package com.rescueanimals.models;

/**
 * Abstract base class for all rescue animals.
 * Defines the common fields and methods for both Dog and Monkey.
 * This design allows for polymorphic handling of animals in the controller logic.
 * Fields are kept as Strings for easy serialization and compatibility with JSON APIs.
 */
public abstract class RescueAnimal {
    protected String name;
    protected String gender;
    protected String age;
    protected String weight;
    protected String acquisitionDate;
    protected String acquisitionCountry;
    protected String trainingStatus;
    protected boolean reserved;
    protected String inServiceCountry;

    /**
     * Constructs a RescueAnimal with all required fields.
     * @param name Animal's name
     * @param gender Animal's gender
     * @param age Animal's age
     * @param weight Animal's weight
     * @param acquisitionDate Date of acquisition
     * @param acquisitionCountry Country of acquisition
     * @param trainingStatus Current training status
     * @param reserved Reservation status
     * @param inServiceCountry Country where the animal is in service
     */
    public RescueAnimal(String name, String gender, String age, String weight, String acquisitionDate, String acquisitionCountry, String trainingStatus, boolean reserved, String inServiceCountry) {
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

    /**
     * Gets the animal's name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Gets the animal's training status.
     * @return training status
     */
    public String getTrainingStatus() { return trainingStatus; }

    /**
     * Gets the reservation status.
     * @return true if reserved, false otherwise
     */
    public boolean getReserved() { return reserved; }

    /**
     * Sets the reservation status.
     * @param reserved true to reserve, false to unreserve
     */
    public void setReserved(boolean reserved) { this.reserved = reserved; }

    /**
     * Gets the country where the animal is in service.
     * @return in service country
     */
    public String getInServiceCountry() { return inServiceCountry; }

    /**
     * Sets the country where the animal is in service.
     * @param country country name
     */
    public void setInServiceCountry(String country) { this.inServiceCountry = country; }

    /**
     * Gets the country where the animal was acquired.
     * @return acquisition country
     */
    public String getAcquisitionCountry() { return acquisitionCountry; }

    /**
     * Gets the gender of the animal.
     * @return the gender
     */
    public String getGender() { return gender; }

    /**
     * Sets the gender of the animal.
     * @param gender the gender to set
     */
    public void setGender(String gender) { this.gender = gender; }

    /**
     * Gets the age of the animal.
     * @return the age
     */
    public String getAge() { return age; }

    /**
     * Sets the age of the animal.
     * @param age the age to set
     */
    public void setAge(String age) { this.age = age; }

    /**
     * Gets the weight of the animal.
     * @return the weight
     */
    public String getWeight() { return weight; }

    /**
     * Sets the weight of the animal.
     * @param weight the weight to set
     */
    public void setWeight(String weight) { this.weight = weight; }

    /**
     * Gets the acquisition date of the animal.
     * @return the acquisition date
     */
    public String getAcquisitionDate() { return acquisitionDate; }

    /**
     * Sets the acquisition date of the animal.
     * @param acquisitionDate the acquisition date to set
     */
    public void setAcquisitionDate(String acquisitionDate) { this.acquisitionDate = acquisitionDate; }

    /**
     * Gets the acquisition country of the animal.
     * @return the acquisition country
     */
    public String getAcquisitionLocation() { return acquisitionCountry; }

    /**
     * Sets the acquisition country of the animal.
     * @param acquisitionCountry the acquisition country to set
     */
    public void setAcquisitionCountry(String acquisitionCountry) { this.acquisitionCountry = acquisitionCountry; }
} 