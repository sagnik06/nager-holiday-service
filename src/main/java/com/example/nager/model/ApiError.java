package com.example.nager.model;
import java.time.Instant;
public class ApiError {
    private final Instant timestamp = Instant.now(); private final String message; private final String path;
    public ApiError(String message, String path) { this.message = message; this.path = path; }
    public Instant getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
