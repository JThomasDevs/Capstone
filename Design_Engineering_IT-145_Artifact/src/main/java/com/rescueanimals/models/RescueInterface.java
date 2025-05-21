package com.rescueanimals.models;

public interface RescueInterface {
    String getName();
    String getTrainingStatus();
    boolean getReserved();
    void setReserved(boolean reserved);
    String getInServiceLocation();
    void setInServiceCountry(String country);
    String getAcquisitionLocation();
} 