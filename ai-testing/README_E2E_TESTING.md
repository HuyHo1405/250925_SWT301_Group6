## CodeceptJS End-to-End (E2E) Testing cho DateTimeChecker

Tài liệu này hướng dẫn cách setup, nội dung test E2E đã có, và cách chạy.

### 1) Mục tiêu E2E test là gì?
- Mô phỏng luồng người dùng thật trên giao diện: mở trang, nhập Day/Month/Year, bấm Check/Clear, xem thông báo (modal), đóng modal.
- Xác thực sự tích hợp giữa Frontend (Thymeleaf + JS) và Backend (`POST /check`).

### 2) Yêu cầu môi trường
- Node.js 18+ (log hiện tại dùng v22.19.0 vẫn OK)
- Đã cài dependencies tại thư mục `ai-testing`:
  ```bash
  cd ai-testing
  npm install
  ```
- Ứng dụng Spring Boot chạy tại `http://localhost:8080`:
  ```bash
  cd demo
  mvn spring-boot:run
  ```

### 3) Cấu hình đã có
- File cấu hình CodeceptJS: `ai-testing/codecept.conf.js`
  - Sử dụng helper `Playwright` với `browser: 'chromium'` để chạy UI E2E.
  - Mặc định mở trình duyệt (show: true). Có thể đóng bằng biến `HEADLESS=true`.
- Bộ test E2E: `ai-testing/tests/e2e/datetime_e2e_test.js`
- Scripts trong `ai-testing/package.json`:
  - `npm run test:e2e` → chạy riêng nhóm E2E

### 4) Nội dung test E2E đã có
File: `ai-testing/tests/e2e/datetime_e2e_test.js`

- Valid date shows success message
  - Nhập 15/10/2025 → bấm Check → thấy modal chứa "is correct date time" → bấm OK đóng modal

- Invalid date (31/02/2025) shows error message
  - Nhập 31/02/2025 → bấm Check → thấy modal chứa "NOT correct date time" → bấm OK

- Empty input triggers empty validation
  - Để trống cả 3 field → bấm Check → thấy modal chứa "cannot be empty" → bấm OK

- Out of range day triggers range validation
  - Nhập 35/05/2025 → bấm Check → thấy modal chứa "out of range" → bấm OK

- Clear button clears all inputs
  - Nhập dữ liệu → bấm Clear → cả 3 input `#day`, `#month`, `#year` trống

Lưu ý selectors/UI:
- Inputs: `#day`, `#month`, `#year`
- Nút: button text "Check", "Clear", "OK"
- Modal message: `#modalMessage`

### 5) Cách chạy E2E
1. Bật ứng dụng Spring Boot:
   ```bash
   cd demo
   mvn spring-boot:run
   ```
2. Chạy E2E tests (cửa sổ khác):
   ```bash
   cd ai-testing
   npm run test:e2e
   ```
   - Xem log các bước với `--steps` (đã bật sẵn trong script)
   - Mặc định trình duyệt UI hiển thị. Nếu muốn headless:
     ```bash
     set HEADLESS=true && npm run test:e2e   # CMD
     $env:HEADLESS="true"; npm run test:e2e # PowerShell
     ```

### 6) Troubleshooting nhanh
- Không mở được trang: đảm bảo app đang chạy `http://localhost:8080`.
- Không thấy test: kiểm tra `codecept.conf.js` đã có `tests: './tests/**/*_test.js'`.
- Selector không khớp: kiểm tra lại HTML `templates/index.html` và script `static/js/datetime-checker.js`.

### 7) Kết luận
- E2E đảm bảo các hành vi UI chính và đường dẫn hạnh phúc/lỗi hoạt động như kỳ vọng trên môi trường trình duyệt thật.


