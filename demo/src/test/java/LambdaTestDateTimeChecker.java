package com.DateTimeChecker.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LambdaTest Cloud Testing cho DateTimeChecker Application
 * 
 * HƯỚNG DẪN SỬ DỤNG:
 * 1. Đăng ký tài khoản tại https://www.lambdatest.com (có bản free)
 * 2. Lấy Username và Access Key từ LambdaTest Dashboard > Profile Settings
 * 3. Thay thế "your_lambdatest_username" và "your_lambdatest_access_key" bên dưới
 * 4. Cài đặt LambdaTest Tunnel để test localhost:
 *    - Tải từ: https://www.lambdatest.com/support/docs/using-tunnels-for-local-testing/
 *    - Chạy lệnh: lt --user <your_username> --key <your_access_key>
 * 5. Đảm bảo ứng dụng Spring Boot đang chạy trên localhost:8080
 * 6. Chạy test này từ IDE hoặc Maven: mvn test -Dtest=LambdaTestDateTimeChecker
 */
public class LambdaTestDateTimeChecker {

    // ===== CÁCH 1: Hardcode trực tiếp (Đơn giản nhất) =====
    // Thay thế dòng dưới bằng Username và Access Key thực của bạn
    private final String lambdatestUsername = "nguyenminhbao28032000";
    private final String lambdatestAccessKey = "LT_IIVqohXpHt0C8J73U7URt28hbq3IFXXV6yCGDsUhVSATvI5";
    
    // ===== CÁCH 2: Dùng biến môi trường hệ thống =====
    // Nếu muốn dùng biến môi trường, uncomment 2 dòng dưới và comment 2 dòng trên
    // private final String lambdatestUsername = System.getenv().getOrDefault("LT_USERNAME", "your_lambdatest_username");
    // private final String lambdatestAccessKey = System.getenv().getOrDefault("LT_ACCESS_KEY", "your_lambdatest_access_key");

    private final String gridUrl = "hub.lambdatest.com/wd/hub";
    private RemoteWebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        // Kiểm tra xem thông tin đăng nhập đã được cấu hình chưa
        if ("your_lambdatest_username".equals(lambdatestUsername) || 
            "your_lambdatest_access_key".equals(lambdatestAccessKey)) {
            throw new IllegalStateException(
                "Vui lòng cấu hình LambdaTest Username và Access Key trong file này!"
            );
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");

        // Cấu hình cho LambdaTest Platform
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", "Windows 11");
        ltOptions.put("build", "DateTimeChecker Project Tests");
        ltOptions.put("visual", true); // Bật tính năng visual regression
        ltOptions.put("video", true); // Ghi lại video của quá trình test
        ltOptions.put("network", true); // Ghi lại logs mạng
        ltOptions.put("console", true); // Ghi lại logs từ console của trình duyệt
        ltOptions.put("tunnel", true); // Rất quan trọng: Bật tính năng tunnel để test localhost

        capabilities.setCapability("LT:Options", ltOptions);

        // Kết nối đến LambdaTest Grid
        try {
            String url = "https://" + lambdatestUsername + ":" + lambdatestAccessKey + "@" + gridUrl;
            driver = new RemoteWebDriver(new URL(url), capabilities);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (MalformedURLException e) {
            System.out.println("Invalid Grid URL");
            throw e;
        }
    }

    @Test
    public void testValidDate() {
        driver.get("http://localhost:8080/");
        
        // Nhập ngày hợp lệ
        WebElement dayInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("day")));
        dayInput.sendKeys("15");
        
        WebElement monthInput = driver.findElement(By.id("month"));
        monthInput.sendKeys("10");
        
        WebElement yearInput = driver.findElement(By.id("year"));
        yearInput.sendKeys("2025");
        
        // Click nút Check
        WebElement checkButton = driver.findElement(By.xpath("//button[contains(text(), 'Check')]"));
        checkButton.click();
        
        // Chờ modal xuất hiện và kiểm tra kết quả
        WebElement modalMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalMessage")));
        String result = modalMessage.getText();
        
        // Đóng modal
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
        okButton.click();
        
        assertTrue(result.contains("correct date time") || result.contains("is correct"),
            "Expected 'correct date time' message but got: " + result);
    }

    @Test
    public void testInvalidDate() {
        driver.get("http://localhost:8080/");
        
        // Nhập ngày không hợp lệ (31 tháng 2)
        WebElement dayInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("day")));
        dayInput.sendKeys("31");
        
        WebElement monthInput = driver.findElement(By.id("month"));
        monthInput.sendKeys("2");
        
        WebElement yearInput = driver.findElement(By.id("year"));
        yearInput.sendKeys("2025");
        
        // Click nút Check
        WebElement checkButton = driver.findElement(By.xpath("//button[contains(text(), 'Check')]"));
        checkButton.click();
        
        // Chờ modal xuất hiện và kiểm tra kết quả
        WebElement modalMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalMessage")));
        String result = modalMessage.getText();
        
        // Đóng modal
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
        okButton.click();
        
        assertTrue(result.contains("NOT correct date time") || result.contains("is NOT correct"),
            "Expected 'NOT correct date time' message but got: " + result);
    }

    @Test
    public void testLeapYearFebruary29() {
        driver.get("http://localhost:8080/");
        
        // Nhập ngày 29/02 trong năm nhuận
        WebElement dayInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("day")));
        dayInput.sendKeys("29");
        
        WebElement monthInput = driver.findElement(By.id("month"));
        monthInput.sendKeys("2");
        
        WebElement yearInput = driver.findElement(By.id("year"));
        yearInput.sendKeys("2024"); // 2024 là năm nhuận
        
        // Click nút Check
        WebElement checkButton = driver.findElement(By.xpath("//button[contains(text(), 'Check')]"));
        checkButton.click();
        
        // Chờ modal xuất hiện
        WebElement modalMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalMessage")));
        String result = modalMessage.getText();
        
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
        okButton.click();
        
        // 29/02/2024 là hợp lệ vì 2024 là năm nhuận
        assertTrue(result.contains("correct date time") || result.contains("is correct"),
            "Expected valid leap year date (correct date time) but got: " + result);
    }

    @Test
    public void testEmptyInput() {
        driver.get("http://localhost:8080/");
        
        // Để trống các trường input và click Check
        WebElement checkButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Check')]")));
        checkButton.click();
        
        // Chờ modal xuất hiện
        WebElement modalMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalMessage")));
        String result = modalMessage.getText();
        
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
        okButton.click();
        
        assertTrue(result.contains("cannot be empty") || result.contains("empty"),
            "Expected empty input validation message but got: " + result);
    }

    @Test
    public void testOutOfRangeInput() {
        driver.get("http://localhost:8080/");
        
        // Nhập giá trị ngoài phạm vi
        WebElement dayInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("day")));
        dayInput.sendKeys("35"); // Quá 31
        
        WebElement monthInput = driver.findElement(By.id("month"));
        monthInput.sendKeys("5");
        
        WebElement yearInput = driver.findElement(By.id("year"));
        yearInput.sendKeys("2025");
        
        // Click nút Check
        WebElement checkButton = driver.findElement(By.xpath("//button[contains(text(), 'Check')]"));
        checkButton.click();
        
        // Chờ modal xuất hiện
        WebElement modalMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modalMessage")));
        String result = modalMessage.getText();
        
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(), 'OK')]"));
        okButton.click();
        
        assertTrue(result.contains("out of range") || result.contains("range"),
            "Expected out of range message but got: " + result);
    }

    @Test
    public void testClearButton() {
        driver.get("http://localhost:8080/");
        
        // Nhập dữ liệu vào form
        WebElement dayInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("day")));
        dayInput.sendKeys("15");
        
        WebElement monthInput = driver.findElement(By.id("month"));
        monthInput.sendKeys("10");
        
        WebElement yearInput = driver.findElement(By.id("year"));
        yearInput.sendKeys("2025");
        
        // Click nút Clear
        WebElement clearButton = driver.findElement(By.xpath("//button[contains(text(), 'Clear')]"));
        clearButton.click();
        
        // Kiểm tra các trường đã được xóa
        assertEquals("", dayInput.getAttribute("value"), "Day field should be cleared");
        assertEquals("", monthInput.getAttribute("value"), "Month field should be cleared");
        assertEquals("", yearInput.getAttribute("value"), "Year field should be cleared");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            // Đóng phiên làm việc trên LambdaTest để giải phóng tài nguyên
            driver.quit();
        }
    }
}