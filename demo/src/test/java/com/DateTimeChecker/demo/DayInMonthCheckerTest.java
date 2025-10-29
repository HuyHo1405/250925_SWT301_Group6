package com.DateTimeChecker.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class DayInMonthCheckerTest {

    // ...existing code...

    // NEW TEST METHODS FOR DAYS IN MONTH FUNCTIONALITY

    @Test
    @DisplayName("Test getDaysInMonth for all months in non-leap year")
    void testGetDaysInMonthNonLeapYear() {
        int year = 2023; // Non-leap year
        
        assertEquals(31, getDaysInMonth(1, year));   // January
        assertEquals(28, getDaysInMonth(2, year));   // February
        assertEquals(31, getDaysInMonth(3, year));   // March
        assertEquals(30, getDaysInMonth(4, year));   // April
        assertEquals(31, getDaysInMonth(5, year));   // May
        assertEquals(30, getDaysInMonth(6, year));   // June
        assertEquals(31, getDaysInMonth(7, year));   // July
        assertEquals(31, getDaysInMonth(8, year));   // August
        assertEquals(30, getDaysInMonth(9, year));   // September
        assertEquals(31, getDaysInMonth(10, year));  // October
        assertEquals(30, getDaysInMonth(11, year));  // November
        assertEquals(31, getDaysInMonth(12, year));  // December
    }

    @Test
    @DisplayName("Test getDaysInMonth for all months in leap year")
    void testGetDaysInMonthLeapYear() {
        int year = 2024; // Leap year
        
        assertEquals(31, getDaysInMonth(1, year));   // January
        assertEquals(29, getDaysInMonth(2, year));   // February - leap year
        assertEquals(31, getDaysInMonth(3, year));   // March
        assertEquals(30, getDaysInMonth(4, year));   // April
        assertEquals(31, getDaysInMonth(5, year));   // May
        assertEquals(30, getDaysInMonth(6, year));   // June
        assertEquals(31, getDaysInMonth(7, year));   // July
        assertEquals(31, getDaysInMonth(8, year));   // August
        assertEquals(30, getDaysInMonth(9, year));   // September
        assertEquals(31, getDaysInMonth(10, year));  // October
        assertEquals(30, getDaysInMonth(11, year));  // November
        assertEquals(31, getDaysInMonth(12, year));  // December
    }

    @ParameterizedTest
    @DisplayName("Test getDaysInMonth for months with 31 days")
    @ValueSource(ints = {1, 3, 5, 7, 8, 10, 12})
    void testGetDaysInMonthWith31Days(int month) {
        assertEquals(31, getDaysInMonth(month, 2024));
        assertEquals(31, getDaysInMonth(month, 2023));
    }

    @ParameterizedTest
    @DisplayName("Test getDaysInMonth for months with 30 days")
    @ValueSource(ints = {4, 6, 9, 11})
    void testGetDaysInMonthWith30Days(int month) {
        assertEquals(30, getDaysInMonth(month, 2024));
        assertEquals(30, getDaysInMonth(month, 2023));
    }

    @ParameterizedTest
    @DisplayName("Test getDaysInMonth for February in different years")
    @CsvSource({
        "2, 2024, 29",  // Leap year
        "2, 2020, 29",  // Leap year
        "2, 2000, 29",  // Leap year (divisible by 400)
        "2, 2023, 28",  // Non-leap year
        "2, 2022, 28",  // Non-leap year
        "2, 1900, 28",  // Non-leap year (divisible by 100 but not 400)
        "2, 2100, 28"   // Non-leap year (divisible by 100 but not 400)
    })
    void testGetDaysInMonthFebruary(int month, int year, int expectedDays) {
        assertEquals(expectedDays, getDaysInMonth(month, year));
    }

    @Test
    @DisplayName("Test getDaysInMonth with invalid month")
    void testGetDaysInMonthInvalidMonth() {
        assertEquals(-1, getDaysInMonth(0, 2024));    // Month 0
        assertEquals(-1, getDaysInMonth(13, 2024));   // Month 13
        assertEquals(-1, getDaysInMonth(-1, 2024));   // Negative month
        assertEquals(-1, getDaysInMonth(-5, 2024));   // Negative month
        assertEquals(-1, getDaysInMonth(25, 2024));   // Invalid month
    }

    @Test
    @DisplayName("Test getDaysInMonth with invalid year")
    void testGetDaysInMonthInvalidYear() {
        assertEquals(-1, getDaysInMonth(6, 999));     // Year < 1000
        assertEquals(-1, getDaysInMonth(6, 3001));    // Year > 3000
        assertEquals(-1, getDaysInMonth(6, -100));    // Negative year
        assertEquals(-1, getDaysInMonth(6, 0));       // Year 0
    }

    @Test
    @DisplayName("Test getDaysInMonth boundary years")
    void testGetDaysInMonthBoundaryYears() {
        // Boundary years
        assertEquals(30, getDaysInMonth(6, 1000));    // Year = 1000
        assertEquals(30, getDaysInMonth(6, 3000));    // Year = 3000
        
        // February in boundary years
        assertEquals(28, getDaysInMonth(2, 1000));    // Year 1000 is NOT leap year (1000 % 4 = 0, but 1000 % 100 = 0 and 1000 % 400 != 0)
        assertEquals(28, getDaysInMonth(2, 3000));    // Year 3000 is not leap year
    }

    @ParameterizedTest
    @DisplayName("Test getDaysInMonth for various leap years")
    @CsvSource({
        "1996, 29",  // Leap year
        "2000, 29",  // Century leap year
        "2004, 29",  // Leap year
        "2008, 29",  // Leap year
        "2012, 29",  // Leap year
        "2016, 29",  // Leap year
        "2020, 29",  // Leap year
        "2024, 29",  // Leap year
        "2028, 29"   // Leap year
    })
    void testGetDaysInMonthLeapYears(int year, int expectedDays) {
        assertEquals(expectedDays, getDaysInMonth(2, year));
    }

    @ParameterizedTest
    @DisplayName("Test getDaysInMonth for various non-leap years")
    @CsvSource({
        "1997, 28",  // Non-leap year
        "1998, 28",  // Non-leap year
        "1999, 28",  // Non-leap year
        "2001, 28",  // Non-leap year
        "2002, 28",  // Non-leap year
        "2003, 28",  // Non-leap year
        "2021, 28",  // Non-leap year
        "2022, 28",  // Non-leap year
        "2023, 28",  // Non-leap year
        "1900, 28",  // Century non-leap year
        "2100, 28"   // Century non-leap year
    })
    void testGetDaysInMonthNonLeapYears(int year, int expectedDays) {
        assertEquals(expectedDays, getDaysInMonth(2, year));
    }

    // ...existing code...

    // ADD TESTS FOR LEAP YEAR LOGIC
    @Test
    @DisplayName("Test isLeapYear method")
    void testIsLeapYear() {
        // Regular leap years (divisible by 4)
        assertTrue(isLeapYear(2024));
        assertTrue(isLeapYear(2020));
        assertTrue(isLeapYear(2016));
        assertTrue(isLeapYear(2012));
        
        // Non-leap years
        assertFalse(isLeapYear(2023));
        assertFalse(isLeapYear(2022));
        assertFalse(isLeapYear(2021));
        
        // Century years - divisible by 100 but not 400 (NOT leap years)
        assertFalse(isLeapYear(1900));
        assertFalse(isLeapYear(2100));
        assertFalse(isLeapYear(2200));
        
        // Century years - divisible by 400 (leap years)
        assertTrue(isLeapYear(2000));
        assertTrue(isLeapYear(2400));
        assertTrue(isLeapYear(1600));
    }

    @ParameterizedTest
    @DisplayName("Test isLeapYear with various years")
    @CsvSource({
        "2000, true",   // Divisible by 400
        "1900, false",  // Divisible by 100 but not 400
        "2004, true",   // Divisible by 4
        "2001, false",  // Not divisible by 4
        "2100, false",  // Divisible by 100 but not 400
        "2400, true",   // Divisible by 400
        "1996, true",   // Divisible by 4
        "1997, false"   // Not divisible by 4
    })
    void testIsLeapYearParameterized(int year, boolean expected) {
        assertEquals(expected, isLeapYear(year));
    }

    // HELPER METHODS
    private int getDaysInMonth(int month, int year) {
        // Validate input
        if (year < 1000 || year > 3000) return -1;
        if (month < 1 || month > 12) return -1;

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        // Check for leap year and adjust February
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
        
        return daysInMonth[month - 1];
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}