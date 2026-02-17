package com.epam.springCoreTask.util;

import java.util.List;

public interface UsernameGenerator {
    String generateUsername(String firstName, String lastName, List<String> existingUsernames);
}
