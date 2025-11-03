## CodeceptJS API Testing cho DateTimeChecker

Tài liệu này giải thích nhanh cách setup, cách chạy, và nội dung test API đã có cho dự án.

### 1) Yêu cầu
- Node.js 18+ (đang dùng v22.19 trong log cũng OK)
- Ứng dụng Spring Boot chạy tại `http://localhost:8080`
  - Khởi động từ thư mục `demo`: `mvn spring-boot:run`

### 2) Cấu hình đã có
- File cấu hình: `ai-testing/codecept.conf.js`
  - Helpers:
    - `Playwright` (cho test UI nếu cần)
    - `REST` (dùng gọi API): `endpoint = process.env.API_BASE_URL || 'http://localhost:8080'`
- Scripts trong `ai-testing/package.json`:
  - `npm test`: chạy toàn bộ test
  - `npm run test:api`: chạy nhóm API (đã dùng `cross-env` cho Windows)

### 3) Cách chạy API test
1. Bật ứng dụng: trong thư mục `demo` chạy:
   ```bash
   mvn spring-boot:run
   ```
2. Mở terminal khác, cd đến `ai-testing` và chạy một trong các cách:
   - Qua script:
     ```bash
     npm run test:api
     ```
   - Hoặc thủ công (PowerShell):
     ```powershell
     $env:API_BASE_URL="http://localhost:8080"
     npx codeceptjs run --steps --grep "DateTime API"
     ```

### 4) Các test API hiện có (đều PASS trong log gần nhất)
File: `ai-testing/tests/api/date_api_test.js`

- valid date should be accepted
  - Input: `{ day: '15', month: '10', year: '2025' }`
  - Kỳ vọng: HTTP 200, message chứa `is correct date time`

- invalid date should be rejected (31/02/2025)
  - Input: `{ day: '31', month: '2', year: '2025' }`
  - Kỳ vọng: HTTP 200, message chứa `NOT correct date time`

- day out of valid range should be rejected (35/05/2025)
  - Input: `{ day: '35', month: '5', year: '2025' }`
  - Kỳ vọng: HTTP 200, message chứa `NOT correct date time`

- non-numeric input should return validation error
  - Input: `{ day: 'aa', month: 'bb', year: 'cc' }`
  - Kỳ vọng: HTTP 200, message chứa `Lỗi` (thông điệp lỗi tiếng Việt)

Lưu ý: API `POST /check` trả về JSON theo `DateTimeModel` với `message` phản ánh kết quả kiểm tra ngày.

### 5) Ghi chú cho Windows
- Nếu dùng trực tiếp biến môi trường trong lệnh:
  - PowerShell: `$env:API_BASE_URL="http://localhost:8080"`
  - CMD: `set API_BASE_URL=http://localhost:8080`
- Trong `package.json` đã tích hợp `cross-env` để script chạy đồng nhất: `npm run test:api`

### 6) Mở rộng test
- Thêm file mới trong `ai-testing/tests/api/` với hậu tố `_test.js` để tự động được quét.
- Có thể kiểm thử thêm các trường hợp:
  - Năm ngoài khoảng (ví dụ < 1000 hoặc > 3000)
  - Tháng 0 hoặc > 12
  - Ngày 0 hoặc > 31

### 7) Kết luận
- Bạn đã chạy thành công 4 test API (PASS) kiểm chứng các nhánh chính:
  - Hợp lệ, không hợp lệ do quy tắc lịch, ngày vượt phạm vi, và định dạng không phải số.
- Test API giúp kiểm tra trực tiếp endpoint backend `POST /check` không cần UI, chạy nhanh và ổn định.


