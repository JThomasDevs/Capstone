package com.rescueanimals.models.dao;

import java.util.List;

import com.rescueanimals.models.Dog;
import com.rescueanimals.models.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Data Access Object for Dog entities.
 * Handles all database operations related to dogs.
 * Uses JPA for persistence and transaction management.
 */
public class DogDAO {
    /**
     * Retrieves all dogs from the database.
     * 
     * @return List of all dogs in the system
     */
    public List<Dog> getAllDogs() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Dog> query = em.createQuery("SELECT d FROM Dog d", Dog.class);
            return query.getResultList();
        }
    }

    /**
     * Retrieves a dog by its name.
     * 
     * @param name The name of the dog
     * @return The dog with the specified name
     */
    public Dog getDogByName(String name) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Dog.class, name);
        }
    }

    /**
     * Adds a new dog to the database.
     * 
     * @param dog The dog to add
     */
    public void saveDog(Dog dog) {
        if (dog.getName() == null || dog.getName().trim().isEmpty()) {
            System.err.println("Error: Dog name cannot be null or empty. Skipping save.");
            return;
        }
        try (EntityManager em = JPAUtil.getEntityManager()) {
            if (em.find(Dog.class, dog.getName()) != null) {
                System.err.println("Error: Dog with name '" + dog.getName() + "' already exists. Skipping save.");
                return;
            }
            em.getTransaction().begin();
            em.persist(dog);
            em.getTransaction().commit();
        }
    }

    /**
     * Updates a dog in the database.
     * 
     * @param dog The dog to update
     */
    public void updateDog(Dog dog) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(dog);
            em.getTransaction().commit();
        }
    }

    /**
     * Deletes a dog from the database.
     * 
     * @param name The name of the dog to delete
     */
    public void deleteDog(String name) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            Dog dog = em.find(Dog.class, name);
            if (dog != null) {
                em.remove(dog);
            }
            em.getTransaction().commit();
        }
    }

    /**
     * Retrieves dogs by their training status.
     * 
     * @param status The training status of the dogs
     * @return List of dogs with the specified training status
     */
    public List<Dog> getDogsByTrainingStatus(String status) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Dog> query = em.createQuery(
                "SELECT d FROM Dog d WHERE d.trainingStatus = :status", Dog.class);
            query.setParameter("status", status);
            return query.getResultList();
        }
    }

    /**
     * Updates a dog's reservation status and service country.
     * 
     * @param name The name of the dog to update
     * @param reserved The new reservation status
     * @param inServiceCountry The new service country
     * @throws RuntimeException if the operation fails
     */
    public void updateDogStatus(String name, boolean reserved, String inServiceCountry) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();
                Dog dog = em.find(Dog.class, name);
                if (dog != null) {
                    dog.setReserved(reserved);
                    dog.setInServiceCountry(inServiceCountry);
                }
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error: Failed to update dog status");
                System.err.println("Details: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Retrieves all available (non-reserved) dogs.
     * 
     * @return List of available dogs
     */
    public List<Dog> getAvailableDogs() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Dog> query = em.createQuery(
                "SELECT d FROM Dog d WHERE d.reserved = false AND LOWER(d.trainingStatus) = 'in service'", Dog.class);
            return query.getResultList();
        }
    }
} 