## Lab 4 – Hướng dẫn Unit Automation Test và ghi vào Unit Test Defect Log (Excel)

Tài liệu này hướng dẫn bạn chạy unit test, đọc kết quả, xác định defect, và cách điền vào file Excel “Unit Test Defect Log” theo đúng template (các cột: Defect ID, Module, Description, Type, Severity, Priority, Status, Created Date,…).

### 1) Chuẩn bị
- Java JDK 21 + Maven đã cài sẵn (dự án đã dùng)
- Repo này đã có unit tests tại: `demo/src/test/java/com/DateTimeChecker/demo/`
- Thư mục báo cáo Maven: `demo/target/surefire-reports`

### 2) Chạy Unit Test
Mở terminal tại thư mục `demo`:
```bash
mvn test
```
Chạy một class cụ thể:
```bash
mvn test -Dtest=DayInMonthCheckerTest
mvn test -Dtest=DateTimeCheckerTest
```
Kết quả xuất ở console và file trong `demo/target/surefire-reports/` (XML + TXT).

Các file thường xem:
- `TEST-*.xml`: kết quả tổng quan theo JUnit
- `*.txt`: log chi tiết (stacktrace nếu fail)

### 3) Đọc kết quả để xác định Pass/Fail
- PASS: Test chạy thành công, không có assertion failure
- FAIL/ERROR: Có assertion failure hoặc exception; kiểm tra file `*.txt` để biết actual behavior

Mẹo xác định nhanh:
- Dòng tổng kết ở console: `Tests run: X, Failures: Y, Errors: Z, Skipped: K`
- Trong `surefire-reports`, xem file `*-FAILED.txt` (nếu có) để lấy chi tiết.

### 4) Khi nào ghi vào “Unit Test Defect Log” (Excel)
- Chỉ ghi khi có bất thường/khác kỳ vọng: Test Fail hoặc Actual không đúng so với Expected (dù chưa có test bao phủ, bạn có thể viết thêm test nhỏ để tái hiện bug).
- Với test Pass nhưng **kỳ vọng của người dùng** khác thông điệp hiện tại (ví dụ thông điệp tiếng Việt/Anh không thống nhất), có thể log defect về “message/content không chuẩn”.

### 5) Cách map thông tin từ test → dòng defect trong Excel
- Defect ID: STT tăng dần (1,2,3,…)
- Module: nêu vùng kiểm thử (ví dụ: `service.DateTimeCheckerService`, `controller.DtcController`)
- Description: mô tả ngắn + Expected vs Actual
  - Gợi ý cấu trúc: `(<case/đầu vào>) Expected: …; Actual: …`
- Type: phân loại (`Functional`, `Validation`, `Message/Content`, `Boundary`, `Exception Handling`,…)
- Severity: `Fatal/Serious/Minor` (quy ước lớp học)
- Priority: `High/Medium/Low`
- Status: `Open/Pending/Resolved/Closed`
- Created Date: ngày ghi nhận (VD: `30-Oct-2025`)
- Evidence: đường dẫn file log hoặc ảnh minh họa

Ví dụ mô tả (mẫu theo ảnh template):
- Description: `Default value of Status field = blank is incorrect. Expected: default Status = "Open" when create new document.`

Ví dụ cho dự án này:
1) Boundary/Leap Year
   - Case: `checkDate(1900, 2, 29)`
   - Expected: 1900 không phải năm nhuận → invalid
   - Actual: (nếu test chứng minh ra valid) → log defect
   - Type: `Boundary`
   - Severity: `Serious`
   - Priority: `Medium`

2) Validation message không nhất quán ngôn ngữ
   - Case: gửi ký tự cho Day/Month/Year qua controller
   - Expected: thông điệp tiếng Anh chuẩn hóa
   - Actual: thông điệp bắt đầu bằng `"Lỗi: …"` (tiếng Việt)
   - Type: `Message/Content`
   - Severity: `Minor`
   - Priority: `Low`

Lưu ý: Chỉ điền defect khi bạn đã có bằng chứng tái hiện được (bằng unit test hoặc log thực tế).

### 6) Lấy Evidence để đưa vào Excel
- Dán đoạn log từ `demo/target/surefire-reports/*.txt` (hoặc trích dẫn 1-2 dòng quan trọng)
- Ghi đường dẫn tới file log trong repo
- Nếu cần, chụp ảnh console và lưu vào thư mục `evidence/` rồi dẫn link

### 7) Quy trình điền Excel – “Unit Test Defect Log”
1. Mở file template Excel của lớp (Template_Defect_Log.xls)
2. Tạo sheet hoặc file “Unit Test Defect Log”
3. Điền các cột theo bảng (ví dụ):

| Defect ID | Module                        | Description (Expected vs Actual)                                                                 | Type        | Severity | Priority | Status | Created Date | Evidence Path |
|-----------|-------------------------------|---------------------------------------------------------------------------------------------------|-------------|----------|----------|--------|--------------|---------------|
| 1         | service.DateTimeCheckerService| With input (year=1900, month=2, day=29): Expected invalid; Actual returned valid (message …)     | Boundary    | Serious  | Medium   | Open   | 30-Oct-2025  | demo/target/surefire-reports/TEST-…txt |
| 2         | controller.DtcController      | Non-numeric input returns Vietnamese message. Expected standardized English message.             | Message     | Minor    | Low      | Open   | 30-Oct-2025  | demo/target/surefire-reports/TEST-…txt |

(Thay nội dung bằng kết quả thật của bạn; bảng trên chỉ là ví dụ form.)

### 8) Mẹo viết test unit nhanh để “khơi” bug biên
Bạn có thể tạo tạm một test JUnit mới (không commit nếu không cần), ví dụ:
```java
// Pseudo-code ví dụ
@Test
void leapYear_1900_shouldBeInvalid() {
    DateTimeCheckerService svc = new DateTimeCheckerService();
    assertFalse(svc.checkDate(1900, 2, 29));
}
```
Sau đó chạy `mvn test -Dtest=YourNewTest` để xác nhận actual behavior rồi log defect nếu lệch Expected.

### 9) Checklist nộp bài
- [ ] Chạy unit tests và lưu lại log
- [ ] Cập nhật file Excel “Unit Test Case” (nếu lớp yêu cầu bảng test case riêng)
- [ ] Điền “Unit Test Defect Log” theo template (đúng cột, rõ ràng, có evidence)
- [ ] Commit/đính kèm log + ảnh minh họa (nếu được yêu cầu)


