package com.epam.springCoreTask.util;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {

    public String generateUsername(String firstName, String lastName, List<String> existingUsernames) {

        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }
}
