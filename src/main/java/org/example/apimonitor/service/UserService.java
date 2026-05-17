package org.example.apimonitor.service;

import org.example.apimonitor.dto.UserRequest;
import org.example.apimonitor.dto.UserResponse;
import org.example.apimonitor.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // In-memory database simulation
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    public UserService() {
        // Add sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        log.info("Initializing sample user data");

        User user1 = new User("John Doe", "john.doe@example.com", 30, "Engineering");
        User user2 = new User("Jane Smith", "jane.smith@example.com", 28, "Marketing");
        User user3 = new User("Bob Johnson", "bob.johnson@example.com", 35, "Sales");
        User user4 = new User("Alice Brown", "alice.brown@example.com", 32, "Engineering");
        User user5 = new User("Charlie Wilson", "charlie.wilson@example.com", 29, "Support");

        userStore.put(user1.getId(), user1);
        userStore.put(user2.getId(), user2);
        userStore.put(user3.getId(), user3);
        userStore.put(user4.getId(), user4);
        userStore.put(user5.getId(), user5);

        log.info("Initialized {} sample users", userStore.size());
    }

    public List<UserResponse> getAllUsers() {
        log.debug("Retrieving all users from storage");

        return userStore.values().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(String id) {
        log.debug("Looking up user by ID: {}", id);

        User user = userStore.get(id);
        if (user == null) {
            log.warn("User not found with ID: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }

        log.info("User found: {} - {}", user.getId(), user.getName());
        return convertToResponse(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user with email: {}", userRequest.getEmail());

        // Check if email already exists
        boolean emailExists = userStore.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(userRequest.getEmail()));

        if (emailExists) {
            log.warn("Email already exists: {}", userRequest.getEmail());
            throw new RuntimeException("Email already exists: " + userRequest.getEmail());
        }

        User user = new User(
                userRequest.getName(),
                userRequest.getEmail(),
                userRequest.getAge(),
                userRequest.getDepartment()
        );

        userStore.put(user.getId(), user);

        log.info("User created successfully - ID: {}, Name: {}", user.getId(), user.getName());
        return convertToResponse(user);
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userStore.get(id);
        if (existingUser == null) {
            log.warn("Cannot update - User not found with ID: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }

        // Check email uniqueness (excluding current user)
        boolean emailExists = userStore.values().stream()
                .anyMatch(user -> !user.getId().equals(id) &&
                        user.getEmail().equalsIgnoreCase(userRequest.getEmail()));

        if (emailExists) {
            log.warn("Cannot update - Email already exists: {}", userRequest.getEmail());
            throw new RuntimeException("Email already exists: " + userRequest.getEmail());
        }

        // Update fields
        existingUser.setName(userRequest.getName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setAge(userRequest.getAge());
        existingUser.setDepartment(userRequest.getDepartment());
        existingUser.setUpdatedAt(LocalDateTime.now());

        userStore.put(id, existingUser);

        log.info("User updated successfully - ID: {}, Name: {}", id, existingUser.getName());
        return convertToResponse(existingUser);
    }

    public void deleteUser(String id) {
        log.info("Deleting user with ID: {}", id);

        User removedUser = userStore.remove(id);
        if (removedUser == null) {
            log.warn("Cannot delete - User not found with ID: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }

        log.info("User deleted successfully - ID: {}, Name: {}", id, removedUser.getName());
    }

    public List<UserResponse> searchUsers(String keyword) {
        log.debug("Searching users with keyword: {}", keyword);

        String lowerKeyword = keyword.toLowerCase();
        List<UserResponse> results = userStore.values().stream()
                .filter(user -> user.getName().toLowerCase().contains(lowerKeyword) ||
                        user.getEmail().toLowerCase().contains(lowerKeyword) ||
                        user.getDepartment().toLowerCase().contains(lowerKeyword))
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        log.info("Search completed - Found {} results for keyword: {}", results.size(), keyword);
        return results;
    }

    public List<UserResponse> getUsersByDepartment(String department) {
        log.debug("Fetching users by department: {}", department);

        List<UserResponse> results = userStore.values().stream()
                .filter(user -> user.getDepartment().equalsIgnoreCase(department))
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        log.info("Found {} users in department: {}", results.size(), department);
        return results;
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getDepartment(),
                user.getCreatedAt(),
                user.getActive() ? "ACTIVE" : "INACTIVE"
        );
    }

    // Utility method for metrics
    public long getUserCount() {
        return userStore.size();
    }

    public Map<String, Long> getDepartmentStatistics() {
        return userStore.values().stream()
                .collect(Collectors.groupingBy(
                        User::getDepartment,
                        Collectors.counting()
                ));
    }
}