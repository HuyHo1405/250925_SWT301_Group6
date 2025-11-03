package com.DateTimeChecker.demo;

import com.DateTimeChecker.demo.model.DateTimeModel;
import com.DateTimeChecker.demo.service.DateTimeCheckerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeServiceSpecTest {

    private final DateTimeCheckerService service = new DateTimeCheckerService();

    // 1) Non-numeric inputs should return English message (kỳ vọng theo URD)
    @Test
    void nonNumeric_shouldReturnEnglishMessage() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("aa");
        m.setMonth("bb");
        m.setYear("cc");
        service.process(m);
        // Kỳ vọng chuẩn hóa tiếng Anh:
        assertTrue(m.getMessage().toLowerCase().contains("error"),
                "Expected English error message, got: " + m.getMessage());
    }

    // 2) Invalid date (31/04/2025) nên báo 'out of range' (kỳ vọng URD)
    @Test
    void april31_shouldReportOutOfRange() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("31");
        m.setMonth("4");
        m.setYear("2025");
        service.process(m);
        assertTrue(m.getMessage().toLowerCase().contains("out of range"),
                "Expected 'out of range' wording, got: " + m.getMessage());
    }

    // 3) Trim input: khoảng trắng vẫn valid và format dd/MM/yyyy
    @Test
    void trimmedInput_shouldBeValidAndFormatted() {
        DateTimeModel m = new DateTimeModel();
        m.setDay(" 5 ");
        m.setMonth(" 7 ");
        m.setYear(" 2025 ");
        service.process(m);
        assertTrue(m.getMessage().contains("05/07/2025"),
                "Expected zero-padded dd/MM/yyyy, got: " + m.getMessage());
    }

    // 4) Month out of range nên có message tiếng Anh & rõ nghĩa
    @Test
    void month13_shouldReturnEnglishOutOfRangeMessage() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("10");
        m.setMonth("13");
        m.setYear("2025");
        service.process(m);
        String msg = m.getMessage().toLowerCase();
        assertTrue(msg.contains("month") && (msg.contains("out of range") || msg.contains("invalid")),
                "Expected English out-of-range month message, got: " + m.getMessage());
    }

    // 5) Chính sách boundary year (nếu URD yêu cầu 1900–2100)
    @Test
    void yearTooLow_shouldBeRejectedByPolicy() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("10");
        m.setMonth("10");
        m.setYear("1899");
        service.process(m);
        assertTrue(m.getMessage().toLowerCase().contains("out of range"),
                "Expected policy-based out-of-range year, got: " + m.getMessage());
    }
}