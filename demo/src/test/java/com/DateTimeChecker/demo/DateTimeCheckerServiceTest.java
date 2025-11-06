package com.DateTimeChecker.demo;

import com.DateTimeChecker.demo.model.DateTimeModel;
import com.DateTimeChecker.demo.service.DateTimeCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeCheckerServiceTest {

    private DateTimeCheckerService service;

    @BeforeEach
    void setUp() {
        service = new DateTimeCheckerService();
    }

    // ----------- TEST daysInMonth() ------------
    @Test
    void testDaysInMonth_LeapYearFebruary() {
        assertEquals(29, service.daysInMonth(2024, 2));
    }

    @Test
    void testDaysInMonth_NonLeapYearFebruary() {
        assertEquals(28, service.daysInMonth(2023, 2));
    }

    @Test
    void testDaysInMonth_ThirtyDaysMonths() {
        assertEquals(30, service.daysInMonth(2023, 4)); // April
        assertEquals(30, service.daysInMonth(2023, 9)); // September
    }

    @Test
    void testDaysInMonth_ThirtyOneDaysMonths() {
        assertEquals(31, service.daysInMonth(2023, 1)); // January
        assertEquals(31, service.daysInMonth(2023, 12)); // December
    }

    @Test
    void testDaysInMonth_InvalidMonth() {
        assertThrows(IllegalArgumentException.class, () -> service.daysInMonth(2023, 13));
    }

    // ----------- TEST checkDate() ------------
    @Test
    void testCheckDate_ValidDate() {
        assertTrue(service.checkDate(2024, 2, 29)); // Leap year
    }

    @Test
    void testCheckDate_InvalidDay() {
        assertFalse(service.checkDate(2023, 2, 29)); // Non-leap year
    }

    @Test
    void testCheckDate_InvalidMonth() {
        assertFalse(service.checkDate(2023, 13, 10));
    }

    @Test
    void testCheckDate_InvalidYear() {
        assertFalse(service.checkDate(999, 12, 10));
    }

    // ----------- TEST process() ------------
    @Test
    void testProcess_ValidDate() {
        DateTimeModel model = new DateTimeModel("2023", "12", "25");
        service.process(model);
        assertEquals("25/12/2023 is correct date time!", model.getMessage());
    }

    @Test
    void testProcess_InvalidDate() {
        DateTimeModel model = new DateTimeModel("2023", "2", "30");
        service.process(model);
        assertEquals("30/02/2023 is NOT correct date time!", model.getMessage());
    }

    @Test
    void testProcess_InvalidNumberInput() {
        DateTimeModel model = new DateTimeModel("abcd", "12", "25");
        service.process(model);
        assertTrue(model.getMessage().contains("Vui lòng nhập số hợp lệ"));
    }

    @Test
    void testProcess_InvalidNumberInputForMonth() {
        DateTimeModel model = new DateTimeModel("2023", "abcd", "25");
        service.process(model);
        assertTrue(model.getMessage().contains("Vui lòng nhập số hợp lệ"));
    }

    @Test
    void testProcess_NullInput() {
        DateTimeModel model = null;
        // Service currently throws NullPointerException due to handling when model is null
        assertThrows(NullPointerException.class, () -> service.process(model));
    }
}
