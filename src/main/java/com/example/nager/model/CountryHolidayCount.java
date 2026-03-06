package com.example.nager.model;
public record CountryHolidayCount(String countryCode, int weekdayHolidayCount) implements Comparable<CountryHolidayCount> {
    @Override
    public int compareTo(CountryHolidayCount o) {
        int cmp = Integer.compare(o.weekdayHolidayCount, this.weekdayHolidayCount);
        if (cmp != 0) return cmp; return this.countryCode.compareTo(o.countryCode);
    }

    // Backwards-compatible getters
    public String getCountryCode() { return countryCode; }
    public int getWeekdayHolidayCount() { return weekdayHolidayCount; }
}
