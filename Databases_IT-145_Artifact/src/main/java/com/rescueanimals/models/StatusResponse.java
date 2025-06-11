package com.rescueanimals.models;

/**
 * Simple response wrapper for API endpoints.
 * Used to communicate success/failure status to the frontend in a consistent JSON format.
 * This design makes it easy for the frontend to parse and handle API responses.
 */
public class StatusResponse {
    public final boolean success;

    /**
     * Constructs a StatusResponse with the given success value.
     * @param success true if the operation succeeded, false otherwise
     */
    public StatusResponse(boolean success) {
        this.success = success;
    }
} 