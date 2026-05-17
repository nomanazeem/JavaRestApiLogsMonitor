package org.example.apimonitor.controller;

import org.example.apimonitor.dto.UserRequest;
import org.example.apimonitor.dto.UserResponse;
import org.example.apimonitor.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final Timer requestTimer;

    public UserController(UserService userService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.requestTimer = Timer.builder("api.requests.duration")
                .description("Time taken to process API requests")
                .register(meterRegistry);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        // Set correlation IDs for tracing
        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("operation", "getAllUsers");

        log.info("Fetching all users - Request received");

        Timer.Sample sample = Timer.start();
        try {
            List<UserResponse> users = userService.getAllUsers();
            log.info("Successfully retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(requestTimer);
            MDC.clear();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable String id,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("userId", id);
        MDC.put("operation", "getUserById");

        log.info("Fetching user with ID: {}", id);

        Timer.Sample sample = Timer.start();
        try {
            UserResponse user = userService.getUserById(id);
            log.info("User found: {} - {}", id, user.getName());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.warn("User not found with ID: {}", id);
            throw e;
        } finally {
            sample.stop(requestTimer);
            MDC.clear();
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest userRequest,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("operation", "createUser");
        MDC.put("userEmail", userRequest.getEmail());

        log.info("Creating new user: {}", userRequest.getName());

        Timer.Sample sample = Timer.start();
        try {
            UserResponse createdUser = userService.createUser(userRequest);
            log.info("User created successfully with ID: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(requestTimer);
            MDC.clear();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequest userRequest,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("userId", id);
        MDC.put("operation", "updateUser");

        log.info("Updating user with ID: {}", id);

        Timer.Sample sample = Timer.start();
        try {
            UserResponse updatedUser = userService.updateUser(id, userRequest);
            log.info("User updated successfully: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            log.warn("Cannot update - User not found: {}", id);
            throw e;
        } finally {
            sample.stop(requestTimer);
            MDC.clear();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("userId", id);
        MDC.put("operation", "deleteUser");

        log.info("Deleting user with ID: {}", id);

        Timer.Sample sample = Timer.start();
        try {
            userService.deleteUser(id);
            log.info("User deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Cannot delete - User not found: {}", id);
            throw e;
        } finally {
            sample.stop(requestTimer);
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam String keyword,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("searchKeyword", keyword);
        MDC.put("operation", "searchUsers");

        log.info("Searching users with keyword: {}", keyword);

        List<UserResponse> users = userService.searchUsers(keyword);
        log.info("Found {} users matching keyword: {}", users.size(), keyword);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<UserResponse>> getUsersByDepartment(
            @PathVariable String department,
            @RequestHeader(value = "X-Request-ID", required = false) String requestId) {

        String traceId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("department", department);
        MDC.put("operation", "getUsersByDepartment");

        log.info("Fetching users from department: {}", department);

        List<UserResponse> users = userService.getUsersByDepartment(department);
        log.info("Found {} users in department: {}", users.size(), department);

        return ResponseEntity.ok(users);
    }
}