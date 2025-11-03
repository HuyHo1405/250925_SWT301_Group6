## CodeceptJS Mobile Testing (Android) với Appium

Tài liệu này hướng dẫn setup và chạy kiểm thử Mobile Web (Android Chrome) cho DateTimeChecker bằng CodeceptJS + Appium.

### Mobile testing dùng để test gì?
- Kiểm thử hành vi ứng dụng trên trình duyệt di động (Chrome Android): tương tác chạm, nhập liệu, hiển thị modal, tích hợp backend.
- Có thể mở rộng để test Native App (APK) nếu có, nhưng ở đây tập trung Mobile Web.

### 1) Cài đặt bắt buộc
1. Android Studio (SDK + Platform Tools)
   - Cài từ trang Google, mở SDK Manager cài: Android SDK, Platform-Tools, ít nhất một Android SDK Platform.
   - Thiết lập biến môi trường:
     - `ANDROID_HOME` trỏ tới thư mục SDK (ví dụ: `C:\Users\<User>\AppData\Local\Android\Sdk`)
     - Thêm vào PATH: `%ANDROID_HOME%\platform-tools`
2. Java JDK (đã có cho dự án)
   - Đảm bảo `JAVA_HOME` trỏ tới JDK.
3. Node.js + Appium
   ```bash
   npm i -g appium appium-doctor
   appium driver install uiautomator2
   appium-doctor --android
   ```
   - Đảm bảo `appium-doctor --android` báo OK cho tất cả mục.

### 2) Bật Emulator hoặc kết nối thiết bị thật
- Emulator: mở Android Studio > Device Manager > tạo và Start AVD.
- Thiết bị thật: bật Developer options + USB debugging, cắm USB, kiểm tra `adb devices` thấy thiết bị.

### 3) Khởi chạy Appium server
```bash
appium
```
Mặc định chạy ở `0.0.0.0:4723`.

### 4) Cấu hình CodeceptJS cho Mobile Web
- Đã tạo sẵn `ai-testing/codecept.mobile.conf.js` dùng Appium helper, mobile Chrome.
- Base URL:
  - Android emulator truy cập host bằng `http://10.0.2.2:8080`
  - Thiết bị thật: dùng IP máy bạn, ví dụ `http://192.168.1.100:8080`
  - Có thể override bằng biến môi trường `MOBILE_BASE_URL`.

### 5) Test mẫu đã có
- File: `ai-testing/tests/mobile/mobile_web_e2e_test.js`
  - Valid date trên mobile
  - Invalid date (31/02/2025) trên mobile

### 6) Chạy ứng dụng và chạy test
1. Bật Spring Boot (cửa sổ 1):
   ```bash
   cd demo
   mvn spring-boot:run
   ```
2. Bật Appium (cửa sổ 2):
   ```bash
   appium
   ```
3. Chạy mobile tests (cửa sổ 3):
   - Emulator (mặc định 10.0.2.2):
     ```bash
     cd ai-testing
     npm run test:mobile
     ```
   - Thiết bị thật (thay IP máy):
     ```bash
     cd ai-testing
     cross-env MOBILE_BASE_URL=http://192.168.1.100:8080 npx codeceptjs run -c codecept.mobile.conf.js --steps
     ```

### 7) Tùy biến thiết bị/phiên bản Android
- Qua biến môi trường:
  - `ANDROID_DEVICE` (vd: `Pixel_7_Pro_API_34` hoặc tên adb `deviceId`)
  - `ANDROID_VERSION` (vd: `14`)
- Ví dụ:
  ```bash
  cross-env ANDROID_DEVICE=emulator-5554 ANDROID_VERSION=14 npm run test:mobile
  ```

### 8) Ghi chú
- Nếu Chrome trên thiết bị/emulator khác biệt, Appium sẽ tự tải chromedriver phù hợp nhờ `appium:chromedriverAutodownload: true`.
- Với thiết bị thật và backend chạy localhost, cần dùng IP LAN của máy, không dùng `localhost`.
- Có thể chạy qua cloud device farm (LambdaTest, BrowserStack) bằng cách đổi sang capabilities cloud tương ứng.

### 9) Khắc phục lỗi Chromedriver

**Lỗi: "No Chromedriver found that can automate Chrome 'X.X.X'"**

Có một số cách để khắc phục:

**Cách 1: Cập nhật Appium lên version mới nhất (Khuyến nghị)**
```bash
npm update -g appium
appium driver update uiautomator2
```

**Cách 2: Cài Chromedriver thủ công cho Chrome 109**
1. Kiểm tra Chrome version trên emulator:
   ```bash
   adb shell dumpsys package com.android.chrome | grep versionName
   ```
2. Tải Chromedriver phù hợp từ: https://chromedriver.chromium.org/downloads
   - Với Chrome 109, tải Chromedriver 109.x.x
3. Giải nén và đặt vào thư mục trong PATH hoặc chỉ định trong biến môi trường:
   ```bash
   set CHROMEDRIVER_DIR=C:\path\to\chromedriver\folder
   ```

**Cách 3: Cập nhật Chrome trên emulator**
- Mở Chrome trên emulator, vào Settings > About Chrome để kiểm tra version
- Nếu Chrome quá cũ, cập nhật Chrome thông qua Play Store hoặc cài APK mới hơn

**Cách 4: Sử dụng Chrome version mapping**
- Trong codecept.mobile.conf.js đã có `chromedriverDisableBuildCheck: true` để bỏ qua version check
- Nếu vẫn lỗi, thử restart Appium server và chạy lại test

**Kiểm tra Appium có driver đã cài:**
```bash
appium driver list
# Đảm bảo có: uiautomator2
```

### 10) Khắc phục lỗi "Invalid or unsupported WebDriver capabilities found (webSocketUrl)"

**Lỗi:** `Error: Invalid or unsupported WebDriver capabilities found ("webSocketUrl")`

**Nguyên nhân:**
- WebDriverIO version 7.x có validation capabilities quá nghiêm ngặt
- Appium server trả về capability `webSocketUrl` (không phải W3C standard) trong response
- CodeceptJS WebDriver helper không chấp nhận capability này

**Cách khắc phục:**

**Cách 1: Downgrade WebDriverIO về version 6.x (Khuyến nghị)**
```bash
cd ai-testing
npm install webdriverio@^6.12.1 --save-dev
```
Version 6.x tương thích tốt hơn với CodeceptJS 3.7.5 và Appium.

**Cách 2: Update CodeceptJS và WebDriverIO lên version mới nhất**
```bash
cd ai-testing
npm install codeceptjs@latest webdriverio@latest --save-dev
```
Lưu ý: Có thể cần update code config nếu có breaking changes.

**Cách 3: Thử với Appium server version khác**
```bash
npm install -g appium@latest
# Hoặc thử version cụ thể
npm install -g appium@2.0.0
appium driver update uiautomator2
```

**Cách 4: Kiểm tra Appium server logs**
Kiểm tra xem Appium có đang trả về capabilities không hợp lệ không:
- Xem logs khi chạy `appium`
- Nếu thấy `webSocketUrl` trong response, có thể cần update Appium hoặc dùng cách 1

**Sau khi fix, restart:**
1. Dừng Appium server (Ctrl+C)
2. Restart Appium: `appium`
3. Chạy lại test: `npm run test:mobile`


