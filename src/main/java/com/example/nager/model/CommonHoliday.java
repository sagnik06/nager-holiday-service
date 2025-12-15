package com.example.nager.model;
import java.time.LocalDate;
public class CommonHoliday {
    private LocalDate date; private String localNameA; private String localNameB;
    public CommonHoliday(LocalDate date, String localNameA, String localNameB) { this.date = date; this.localNameA = localNameA; this.localNameB = localNameB; }
    public LocalDate getDate() { return date; }
    public String getLocalNameA() { return localNameA; }
    public String getLocalNameB() { return localNameB; }
}
