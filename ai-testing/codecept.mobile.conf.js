const { setHeadlessWhen, setCommonPlugins } = require('@codeceptjs/configure');

require('dotenv').config();

// Mobile testing không dùng headless
setHeadlessWhen(false);
setCommonPlugins();

/**
 * CodeceptJS config cho Mobile Web (Android Chrome) qua Appium
 * Yêu cầu:
 * - Appium server đang chạy (appium)
 * - Thiết bị/emulator Android đã bật, có Chrome
 * - ANDROID_HOME, JAVA_HOME đã cấu hình
 */
exports.config = {
  tests: './tests/mobile/*_test.js',
  output: 'output',
  helpers: {
    WebDriver: {
      url: process.env.MOBILE_BASE_URL || 'http://10.0.2.2:8080',
      host: 'localhost',
      port: 4723,
      path: '/',
      smartWait: 5000,
      waitForTimeout: 10000,
      capabilities: {
        platformName: 'Android',
        'appium:automationName': 'UiAutomator2',
        'appium:deviceName': process.env.ANDROID_DEVICE || 'Android Emulator',
        'appium:platformVersion': process.env.ANDROID_VERSION || '13',
        // ...(process.env.ANDROID_UDID && { 'appium:udid': process.env.ANDROID_UDID }),
        'appium:browserName': 'Chrome',
        // Tự động tải Chromedriver phù hợp với Chrome version
        'appium:chromedriverAutodownload': true,
        ...(process.env.CHROMEDRIVER_DIR && { 'appium:chromedriverExecutableDir': process.env.CHROMEDRIVER_DIR }),
        // Bỏ qua version check nếu không tìm thấy driver chính xác
        'appium:chromedriverDisableBuildCheck': false,
        'appium:nativeWebScreenshot': true,
        'appium:newCommandTimeout': 300
      },
      protocol: 'http',
      strictSSL: false,
      // Disable capability validation to allow Appium-specific capabilities
      logLevel: 'warn'
    }
  },
  include: {
    I: './steps_file.js'
  },
  plugins: {
    htmlReporter: { enabled: true },
  },
  name: 'ai-testing-mobile'
};


