package org.example.apimonitor.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private String department;
    private LocalDateTime createdAt;
    private String status;

    public UserResponse() {}

    public UserResponse(String id, String name, String email, Integer age,
                        String department, LocalDateTime createdAt, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.department = department;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}