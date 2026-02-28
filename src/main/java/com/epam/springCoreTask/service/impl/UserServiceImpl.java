package com.epam.springCoreTask.service.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.epam.springCoreTask.model.User;
import com.epam.springCoreTask.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public <T> T authenticate(String username, String password,
            BiFunction<String, String, Optional<T>> repositoryFinder,
            String entityType) {
        log.debug("Authenticating {}: username={}", entityType, username);

        T entity = repositoryFinder.apply(username, password)
                .orElseThrow(() -> {
                    log.warn("Authentication failed for {}: username={}", entityType, username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        log.info("{} authenticated successfully: username={}", 
                entityType.substring(0, 1).toUpperCase() + entityType.substring(1), username);
        return entity;
    }

    @Override
    public <T> void changePassword(String username, String oldPassword, String newPassword,
            BiFunction<String, String, Optional<T>> authFinder,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType) {
        log.debug("Changing password for {}: username={}", entityType, username);

        T entity = authFinder.apply(username, oldPassword)
                .orElseThrow(() -> {
                    log.warn("Password change failed - invalid credentials for {}: username={}", 
                            entityType, username);
                    return new IllegalArgumentException("Invalid username or password");
                });

        User user = userGetter.apply(entity);
        user.setPassword(newPassword);
        saver.accept(entity);

        log.info("Password changed successfully for {}: username={}", entityType, username);
    }

    @Override
    public <T> void activateEntity(String username,
            Function<String, T> entityGetter,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType) {
        log.debug("Activating {}: username={}", entityType, username);

        T entity = entityGetter.apply(username);
        User user = userGetter.apply(entity);
        user.setActive(true);
        saver.accept(entity);

        log.info("{} activated successfully: username={}", 
                entityType.substring(0, 1).toUpperCase() + entityType.substring(1), username);
    }

    @Override
    public <T> void deactivateEntity(String username,
            Function<String, T> entityGetter,
            Function<T, User> userGetter,
            Consumer<T> saver,
            String entityType) {
        log.debug("Deactivating {}: username={}", entityType, username);

        T entity = entityGetter.apply(username);
        User user = userGetter.apply(entity);
        user.setActive(false);
        saver.accept(entity);

        log.info("{} deactivated successfully: username={}", 
                entityType.substring(0, 1).toUpperCase() + entityType.substring(1), username);
    }
}
