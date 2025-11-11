package server;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class AuthenticationServer {
    private static final String USERS_FILE = "backend/data/users.txt";
    private Map<String, String> users;

    public AuthenticationServer() {
        loadUsers();
    }

    private void loadUsers() {
        users = new HashMap<>();
        Path path = Paths.get(USERS_FILE);

        try {
            if (!Files.exists(path)) {
                createDefaultUsers();
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
            System.out.println("Loaded " + users.size() + " users");
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public boolean authenticate(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void createDefaultUsers() throws IOException {
        String defaultUsers = "student1:pass123\n" +
                "student2:pass123\n" +
                "student3:pass123\n" +
                "student4:pass123\n" +
                "student5:pass123\n" +
                "admin1:admin1123\n";

        Path path = Paths.get(USERS_FILE);
        Files.createDirectories(path.getParent());
        Files.writeString(path, defaultUsers);
    }
}