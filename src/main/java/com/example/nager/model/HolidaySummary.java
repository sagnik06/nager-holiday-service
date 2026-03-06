package com.example.nager.model;
import java.time.LocalDate;
public record HolidaySummary(LocalDate date, String name) {
    // Backwards-compatible bean-style getters used in tests and other code
    public LocalDate getDate() { return date; }
    public String getName() { return name; }
}
