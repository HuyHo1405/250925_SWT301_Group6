package com.DateTimeChecker.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class DateTimeCheckerTest {

    @Test
    @DisplayName("Test valid dates for each month")
    void testValidDatesForEachMonth() {
        // January - 31 days
        assertTrue(isValidDate(30, 1, 2024));
        assertFalse(isValidDate(52, 1, 2024));
        
        // February - 28 days (non-leap year)
        assertTrue(isValidDate(28, 2, 2023));
        assertFalse(isValidDate(29, 2, 2023));
        
        // February - 29 days (leap year)
        assertTrue(isValidDate(29, 2, 2024));
        assertFalse(isValidDate(30, 2, 2024));
        
        // March - 31 days
        assertTrue(isValidDate(31, 3, 2024));
        assertFalse(isValidDate(32, 3, 2024));
        
        // April - 30 days
        assertTrue(isValidDate(30, 4, 2024));
        assertFalse(isValidDate(31, 4, 2024));
        
        // May - 31 days
        assertTrue(isValidDate(31, 5, 2024));
        assertFalse(isValidDate(32, 5, 2024));
        
        // June - 30 days
        assertTrue(isValidDate(30, 6, 2024));
        assertFalse(isValidDate(31, 6, 2024));
        
        // July - 31 days
        assertTrue(isValidDate(31, 7, 2024));
        assertFalse(isValidDate(32, 7, 2024));
        
        // August - 31 days
        assertTrue(isValidDate(31, 8, 2024));
        assertFalse(isValidDate(32, 8, 2024));
        
        // September - 30 days
        assertTrue(isValidDate(30, 9, 2024));
        assertFalse(isValidDate(31, 9, 2024));
        
        // October - 31 days
        assertTrue(isValidDate(31, 10, 2024));
        assertFalse(isValidDate(32, 10, 2024));
        
        // November - 30 days
        assertTrue(isValidDate(30, 11, 2024));
        assertFalse(isValidDate(31, 11, 2024));
        
        // December - 31 days
        assertTrue(isValidDate(31, 12, 2024));
        assertFalse(isValidDate(32, 12, 2024));
    }

    @ParameterizedTest
    @DisplayName("Test days in months with 31 days")
    @ValueSource(ints = {1, 3, 5, 7, 8, 10, 12})
    void testMonthsWith31Days(int month) {
        assertTrue(isValidDate(31, month, 2024));
        assertFalse(isValidDate(32, month, 2024));
    }

    @ParameterizedTest
    @DisplayName("Test days in months with 30 days")
    @ValueSource(ints = {4, 6, 9, 11})
    void testMonthsWith30Days(int month) {
        assertTrue(isValidDate(30, month, 2024));
        assertFalse(isValidDate(31, month, 2024));
    }

    @ParameterizedTest
    @DisplayName("Test February in leap years")
    @CsvSource({
        "29, 2, 2024, true",   // Leap year
        "29, 2, 2020, true",   // Leap year
        "29, 2, 2000, true",   // Leap year (divisible by 400)
        "29, 2, 2023, false",  // Not leap year
        "29, 2, 1900, false",  // Not leap year (divisible by 100 but not 400)
        "28, 2, 2023, true",   // Valid for non-leap year
        "28, 2, 2024, true"    // Valid for leap year
    })
    void testFebruaryLeapYear(int day, int month, int year, boolean expected) {
        assertEquals(expected, isValidDate(day, month, year));
    }

    @Test
    @DisplayName("Test invalid month values")
    void testInvalidMonths() {
        assertFalse(isValidDate(15, 0, 2024));   // Month 0
        assertFalse(isValidDate(15, 13, 2024));  // Month 13
        assertFalse(isValidDate(15, -1, 2024));  // Negative month
        assertFalse(isValidDate(15, -5, 2024));
        assertFalse(isValidDate(15, -47, 2024));
    }

    @Test
    @DisplayName("Test invalid day values")
    void testInvalidDays() {
        assertFalse(isValidDate(0, 1, 2024));    // Day 0
        assertFalse(isValidDate(-1, 1, 2024));   // Negative day
        assertFalse(isValidDate(32, 1, 2024));   // Day > 31
    }

    @Test
    @DisplayName("Test invalid year values")
    void testInvalidYears() {
        assertFalse(isValidDate(15, 6, 999));    // Year < 1000
        assertFalse(isValidDate(15, 6, 3001));   // Year > 3000
        assertTrue(isValidDate(15, 6, 1000));    // Boundary: Year = 1000
        assertTrue(isValidDate(15, 6, 3000));    // Boundary: Year = 3000
    }

    @Test
    @DisplayName("Test boundary dates")
    void testBoundaryDates() {
        // First and last day of months
        assertTrue(isValidDate(1, 1, 2024));     // Jan 1st
        assertTrue(isValidDate(31, 12, 2024));   // Dec 31st
        
        // February boundaries
        assertTrue(isValidDate(28, 2, 2023));    // Last day Feb non-leap
        assertTrue(isValidDate(29, 2, 2024));    // Last day Feb leap
        assertFalse(isValidDate(30, 2, 2024));   // Invalid Feb date
    }

    // Helper method - you should implement this based on your actual logic
    private boolean isValidDate(int day, int month, int year) {
        // This should match your actual implementation
        if (year < 1000 || year > 3000) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        // Check for leap year
        if (isLeapYear(year)) {
            daysInMonth[1] = 29;
        }
        
        return day <= daysInMonth[month - 1];
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}