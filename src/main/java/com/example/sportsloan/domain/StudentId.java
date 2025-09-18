// src/main/java/com/example/sportsloan/domain/StudentId.java
package com.example.sportsloan.domain;

public record StudentId(String value) {
    public StudentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("student id required");
        }
    }
}