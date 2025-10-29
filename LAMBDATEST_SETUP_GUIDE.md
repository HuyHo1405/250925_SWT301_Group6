## Hướng dẫn thiết lập LambdaTest Cloud Testing cho DateTimeChecker

Tài liệu này giúp bạn chạy Selenium test của dự án Java Spring Boot (DateTimeChecker) trên LambdaTest, bao gồm kiểm thử localhost qua Tunnel.

### 1) Yêu cầu trước khi bắt đầu
- Java JDK 21 và Maven đã cài đặt sẵn (đã dùng trong dự án).
- Tài khoản LambdaTest. Nếu chưa có, đăng ký: `https://www.lambdatest.com`.
- Ứng dụng DateTimeChecker có thể chạy tại `http://localhost:8080`.

### 2) Lấy Username và Access Key của LambdaTest
1. Đăng nhập Dashboard LambdaTest.
2. Vào Profile/Account Settings.
3. Sao chép `Username` và `Access Key` (giữ bí mật, không commit lên git công khai).

### 3) Khởi động LambdaTest Tunnel (test localhost)
Bạn không bắt buộc phải dùng Node.js. Trên Windows, khuyến nghị dùng binary LT.exe.

- Cách A: Dùng LT.exe (Windows, không cần Node)
  1) Tải Tunnel theo hướng dẫn: `https://www.lambdatest.com/support/docs/testing-your-privately-hosted-pages/`
  2) Mở PowerShell/Command Prompt tại thư mục chứa LT.exe, chạy:
     ```bash
     .\LT.exe --user YOUR_USERNAME --key YOUR_ACCESS_KEY
     ```
  3) Chờ thông báo "Tunnel is ready" và giữ cửa sổ này mở trong suốt quá trình test.

- Cách B: Dùng npm (chỉ nếu bạn đã cài Node.js)
  ```bash
  npm install -g lambdatest
  lt --user YOUR_USERNAME --key YOUR_ACCESS_KEY
  ```

Tùy chọn: chỉ định port nếu app không chạy 8080
```bash
lt --user YOUR_USERNAME --key YOUR_ACCESS_KEY --port 8080
```

### 4) Cấu hình file test Selenium
Mở file `demo/src/test/java/LambdaTestDateTimeChecker.java` và cập nhật 2 biến:
```java
private final String lambdatestUsername = "your_lambdatest_username";
private final String lambdatestAccessKey = "your_lambdatest_access_key";
```
Lưu ý:
- Để bảo mật, bạn có thể chuyển sang sử dụng biến môi trường thay vì hardcode (tùy chọn).

### 5) Khởi chạy ứng dụng Spring Boot ở local
Trong terminal tại thư mục `demo`:
```bash
mvn spring-boot:run
```
Hoặc chạy JAR đã build:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```
Kiểm tra truy cập `http://localhost:8080` hoạt động bình thường.

### 6) Chạy test trên LambdaTest
Sau khi Ứng dụng và Tunnel đều đang chạy:

- Chạy tất cả test theo class:
```bash
cd demo
mvn test -Dtest=LambdaTestDateTimeChecker
```

- Chạy 1 method cụ thể (ví dụ `testValidDate`):
```bash
mvn test -Dtest=LambdaTestDateTimeChecker#testValidDate
```

Kỳ vọng kết quả Maven: BUILD SUCCESS. Trong log, bạn sẽ thấy dòng "Running com.DateTimeChecker.demo.LambdaTestDateTimeChecker" và Tests run: > 0.

### 7) Xem kết quả trên LambdaTest Dashboard
- Mở: `https://automation.lambdatest.com` → mục Logs/Builds.
- Mỗi test có: Video, Screenshots, Console logs, Network logs (đã bật trong cấu hình).
- Tên build: "DateTimeChecker Project Tests" (đặt trong capability).

### 8) Lưu ý về cấu hình Maven (pom.xml)
- Dùng `spring-boot-starter-test` để quản lý đồng bộ JUnit 5 (không cần tự thêm `junit-jupiter-api`).
- Đủ `selenium-java` (không cần `selenium-remote-driver` riêng).
- Nếu trước đó gặp lỗi "TestEngine with ID 'junit-jupiter' failed to discover tests", hãy gỡ khai báo JUnit rời rạc và để Spring Boot BOM quản lý phiên bản.

Ví dụ phần test dependencies nên trông như sau:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- Spring Boot sẽ kéo đúng phiên bản JUnit 5 -->
    <!-- Không cần tự khai báo junit-jupiter-api riêng -->
    <!-- Không cần thêm vintage nếu không chạy JUnit4 -->
    
</dependency>
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.14.1</version>
    <scope>test</scope>
    
</dependency>
```

### 9) Troubleshooting nhanh
- Tunnel chưa sẵn sàng → chắc chắn thấy "Tunnel is ready" trước khi test.
- Không truy cập được `localhost:8080` → kiểm tra app đã chạy, đúng port, firewall.
- Lỗi phát hiện test JUnit → dọn `pom.xml` theo Mục 8, rồi `mvn clean test`.
- Timeout/Element not found → kiểm tra lại selectors: `#day`, `#month`, `#year`, `#modalMessage`, nút có text "Check" và "OK"; có thể tăng timeout trong `WebDriverWait`.

### 10) Thực hành tốt (Best Practices)
- Không commit Access Key lên repo công khai. Có thể dùng biến môi trường hoặc Maven profiles.
- Đặt tên build/test rõ ràng để dễ tra cứu trên Dashboard.
- Tắt bớt `video`/`visual` nếu muốn tăng tốc khi chạy số lượng lớn.

---

Tất cả đã sẵn sàng. Nếu bạn làm theo tuần tự 1→10, bạn có thể chạy test cloud thành công như log "BUILD SUCCESS" bạn vừa có. Nếu cần mở rộng kịch bản test, thêm test method trong `LambdaTestDateTimeChecker.java` theo cùng mẫu có sẵn.




