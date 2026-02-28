package com.epam.springCoreTask.service;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.epam.springCoreTask.model.User;

/**
 * Service for handling common user-related operations across different entity types.
 */
public interface UserService {

    /**
     * Authenticates an entity by username and password.
     * 
     * @param <T>              the entity type (Trainee or Trainer)
     * @param username         the username
     * @param password         the password
     * @param repositoryFinder function to find entity by username and password
     * @param entityType       the entity type name for logging (e.g., "trainee",
     *                         "trainer")
     * @return the authenticated entity
     * @throws IllegalArgumentException if authentication fails
     */
    <T> T authenticate(String username, String password,
            BiFunction<String, String, Optional<T>> repositoryFinder,
            String entityType);

    /**
     * Changes the password for an entity.
     * 
     * @param <T>          the entity type (Trainee or Trainer)
     * @param username     the username
     * @param oldPassword  the current password
     * @param newPassword  the new password
     * @param authFinder   function to find and authenticate entity
     * @param userGetter   function to extract User from entity
     * @param saver        function to save the entity
     * @param entityType   the entity type name for logging
     * @throws IllegalArgumentException if authentication fails
     */
    <T> void changePassword(String username, String oldPassword, String newPassword,
            BiFunction<String, String, Optional<T>> authFinder,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType);

    /**
     * Activates an entity (sets user.active = true).
     * 
     * @param <T>          the entity type (Trainee or Trainer)
     * @param username     the username
     * @param entityGetter function to get entity by username
     * @param userGetter   function to extract User from entity
     * @param saver        function to save the entity
     * @param entityType   the entity type name for logging
     */
    <T> void activateEntity(String username,
            Function<String, T> entityGetter,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType);

    /**
     * Deactivates an entity (sets user.active = false).
     * 
     * @param <T>          the entity type (Trainee or Trainer)
     * @param username     the username
     * @param entityGetter function to get entity by username
     * @param userGetter   function to extract User from entity
     * @param saver        function to save the entity
     * @param entityType   the entity type name for logging
     */
    <T> void deactivateEntity(String username,
            Function<String, T> entityGetter,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType);
}
