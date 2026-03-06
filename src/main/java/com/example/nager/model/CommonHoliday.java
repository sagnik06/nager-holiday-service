package com.example.nager.model;
import java.time.LocalDate;
public record CommonHoliday(LocalDate date, String localNameA, String localNameB) {
    public LocalDate getDate() { return date; }
    public String getLocalNameA() { return localNameA; }
    public String getLocalNameB() { return localNameB; }
}
