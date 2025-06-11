package com.rescueanimals.models;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManager instances.
 * This class provides centralized access to the EntityManagerFactory
 * and handles the creation and cleanup of EntityManager instances.
 * Uses the singleton pattern to ensure only one EntityManagerFactory exists.
 */
public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "rescue-animals";
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            // Try to load the SQLite dialect class to ensure it's included in the shaded JAR
            Class.forName("org.hibernate.community.dialect.SQLiteDialect");
        } catch (ClassNotFoundException e) {
            // Log the error but continue - the dialect will be loaded by Hibernate
            System.err.println("Warning: SQLite dialect class not found: " + e.getMessage());
        }
    }

    /**
     * Gets the singleton instance of EntityManagerFactory.
     * Creates a new instance if one doesn't exist.
     * 
     * @return The EntityManagerFactory instance
     * @throws RuntimeException if the factory cannot be created
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                // Load the SQLite dialect class
                Class.forName("org.hibernate.community.dialect.SQLiteDialect");
                
                // Create EntityManagerFactory
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (ClassNotFoundException e) {
                System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
                throw new RuntimeException("Could not create EntityManagerFactory", e);
            }
        }
        return entityManagerFactory;
    }

    /**
     * Creates and returns a new EntityManager instance.
     * The caller is responsible for closing the EntityManager when done.
     * 
     * @return A new EntityManager instance
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Shuts down the EntityManagerFactory and releases all resources.
     * Should be called when the application is shutting down.
     * This method is idempotent - calling it multiple times is safe.
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
} 