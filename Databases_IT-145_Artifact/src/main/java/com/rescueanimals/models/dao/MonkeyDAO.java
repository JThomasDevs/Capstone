package com.rescueanimals.models.dao;

import java.util.List;

import com.rescueanimals.models.JPAUtil;
import com.rescueanimals.models.Monkey;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Data Access Object for Monkey entities.
 * Handles all database operations related to monkeys.
 * Uses JPA for persistence and transaction management.
 */
public class MonkeyDAO {
    /**
     * Retrieves all monkeys from the database.
     * 
     * @return List of all monkeys in the system
     */
    public List<Monkey> getAllMonkeys() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Monkey> query = em.createQuery("SELECT m FROM Monkey m", Monkey.class);
            return query.getResultList();
        }
    }

    /**
     * Retrieves a monkey by its name.
     * 
     * @param name The name of the monkey
     * @return The monkey with the specified name
     */
    public Monkey getMonkeyByName(String name) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Monkey.class, name);
        }
    }

    /**
     * Saves a new monkey to the database.
     * 
     * @param monkey The monkey to save
     */
    public void saveMonkey(Monkey monkey) {
        if (monkey.getName() == null || monkey.getName().trim().isEmpty()) {
            System.err.println("Error: Monkey name cannot be null or empty. Skipping save.");
            return;
        }
        try (EntityManager em = JPAUtil.getEntityManager()) {
            if (em.find(Monkey.class, monkey.getName()) != null) {
                System.err.println("Error: Monkey with name '" + monkey.getName() + "' already exists. Skipping save.");
                return;
            }
            em.getTransaction().begin();
            em.persist(monkey);
            em.getTransaction().commit();
        }
    }

    /**
     * Updates an existing monkey in the database.
     * 
     * @param monkey The monkey to update
     */
    public void updateMonkey(Monkey monkey) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(monkey);
            em.getTransaction().commit();
        }
    }

    /**
     * Deletes a monkey from the database.
     * 
     * @param name The name of the monkey to delete
     */
    public void deleteMonkey(String name) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();
            Monkey monkey = em.find(Monkey.class, name);
            if (monkey != null) {
                em.remove(monkey);
            }
            em.getTransaction().commit();
        }
    }

    /**
     * Retrieves monkeys by their training status.
     * 
     * @param status The training status of the monkeys
     * @return List of monkeys with the specified training status
     */
    public List<Monkey> getMonkeysByTrainingStatus(String status) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Monkey> query = em.createQuery(
                "SELECT m FROM Monkey m WHERE m.trainingStatus = :status", Monkey.class);
            query.setParameter("status", status);
            return query.getResultList();
        }
    }

    /**
     * Updates a monkey's reservation status and service country.
     * 
     * @param name The name of the monkey to update
     * @param reserved The new reservation status
     * @param inServiceCountry The new service country
     * @throws RuntimeException if the operation fails
     */
    public void updateMonkeyStatus(String name, boolean reserved, String inServiceCountry) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();
                Monkey monkey = em.find(Monkey.class, name);
                if (monkey != null) {
                    monkey.setReserved(reserved);
                    monkey.setInServiceCountry(inServiceCountry);
                }
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error: Failed to update monkey status");
                System.err.println("Details: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Retrieves all available (non-reserved) monkeys.
     * 
     * @return List of available monkeys
     */
    public List<Monkey> getAvailableMonkeys() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Monkey> query = em.createQuery(
                "SELECT m FROM Monkey m WHERE m.reserved = false AND LOWER(m.trainingStatus) = 'in service'", Monkey.class);
            return query.getResultList();
        }
    }
} 