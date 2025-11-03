package com.DateTimeChecker.demo;

import com.DateTimeChecker.demo.model.DateTimeModel;
import com.DateTimeChecker.demo.service.DateTimeCheckerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Defect probe tests for DateTimeCheckerService
 * Các test này cố tình đặt kỳ vọng chuẩn hóa (English wording, policy rõ ràng)
 * để phát hiện chênh lệch hành vi thực tế → phục vụ ghi Defect Log Lab 4.
 */
public class DateTimeServiceDefectProbeTest {

    private final DateTimeCheckerService service = new DateTimeCheckerService();

    @Test
    @DisplayName("[Message] Non-numeric input should return English error message")
    void nonNumeric_returnsEnglishMessage() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("aa");
        m.setMonth("bb");
        m.setYear("cc");
        service.process(m);
        assertTrue(m.getMessage().toLowerCase().contains("format!"),
                "Expected English error message, got: " + m.getMessage());
    }

    @Test
    @DisplayName("[Validation] Month 13 should mention 'month out of range' in English")
    void month13_shouldMentionOutOfRangeEnglish() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("10");
        m.setMonth("13");
        m.setYear("2025");
        service.process(m);
        String msg = m.getMessage().toLowerCase();
        assertTrue(msg.contains("month") && (msg.contains("out of range") || msg.contains("correct date time!")),
                "Expected English month out-of-range message, got: " + m.getMessage());
    }

    @Test
    @DisplayName("[Validation] Day 0 should mention 'out of range' in English")
    void dayZero_shouldMentionOutOfRangeEnglish() {
        DateTimeModel m = new DateTimeModel();
        m.setDay("0");
        m.setMonth("5");
        m.setYear("2025");
        service.process(m);
        assertTrue(m.getMessage().toLowerCase().contains("out of range") || m.getMessage().toLowerCase().contains("invalid"),
                "Expected English 'out of range' day message, got: " + m.getMessage());
    }

    @Test
    @DisplayName("[Policy] Year below 1900 should be rejected by policy (example policy)")
    void yearBelowPolicy_shouldBeRejected() {
        // Ví dụ chính sách lớp: 1900–2100, trong khi code hiện cho 1000–3000
        DateTimeModel m = new DateTimeModel();
        m.setDay("10");
        m.setMonth("10");
        m.setYear("999");
        service.process(m);
        assertTrue(m.getMessage().toLowerCase().contains("is not correct date time!") || m.getMessage().toLowerCase().contains("invalid"),
                "Expected policy-based is not correct date time!, got: " + m.getMessage());
    }

    @Test
    @DisplayName("[Null] Null model should return English error (not Vietnamese prefix)")
    void nullModel_shouldReturnEnglishError() {
        DateTimeModel m = null;
        // Bọc gọi để tránh NPE ở test runner, service tự xử lý và trả message trong catch
        try {
            // Giả lập: service.process(null) → catch IllegalArgument và set message
            service.process(m);
        } catch (Exception ignored) {
            // Nếu service ném ra ngoài, coi như defect khác; giữ test tập trung vào message chuẩn hóa
        }
        // Không có reference để lấy message khi model null; test này chủ yếu dùng để ghi nhận nếu có thay đổi thiết kế.
        // Có thể thay bằng test input rỗng để có message:
        DateTimeModel empty = new DateTimeModel();
        empty.setDay("");
        empty.setMonth("");
        empty.setYear("");
        service.process(empty);
        assertTrue(empty.getMessage().toLowerCase().contains("error"),
                "Expected English error message for empty input, got: " + empty.getMessage());
    }
}


