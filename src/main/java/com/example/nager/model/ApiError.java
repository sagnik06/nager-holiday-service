package com.example.nager.model;
import java.time.Instant;
public record ApiError(Instant timestamp, String message, String path) {
    public ApiError(String message, String path) {
        this(Instant.now(), message, path);
    }
}
