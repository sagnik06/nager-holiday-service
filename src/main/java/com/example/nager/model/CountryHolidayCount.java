package com.example.nager.model;
public class CountryHolidayCount implements Comparable<CountryHolidayCount> {
    private String countryCode; private int weekdayHolidayCount;
    public CountryHolidayCount(String countryCode, int weekdayHolidayCount) { this.countryCode = countryCode; this.weekdayHolidayCount = weekdayHolidayCount; }
    public String getCountryCode() { return countryCode; }
    public int getWeekdayHolidayCount() { return weekdayHolidayCount; }
    @Override public int compareTo(CountryHolidayCount o) {
        int cmp = Integer.compare(o.weekdayHolidayCount, this.weekdayHolidayCount);
        if (cmp != 0) return cmp; return this.countryCode.compareTo(o.countryCode);
    }
}
