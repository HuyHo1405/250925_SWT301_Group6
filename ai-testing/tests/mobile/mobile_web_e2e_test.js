Feature('Mobile Web - Date Time Checker (Android Chrome)');

// Mặc định MOBILE_BASE_URL sử dụng 10.0.2.2 (Android emulator trỏ về host machine)
// Với thiết bị thật, đặt MOBILE_BASE_URL="http://<IP_MAY_TINH>:8080"

Before(({ I }) => {
  I.amOnPage('/');
});

Scenario('Valid date flow on mobile', async ({ I }) => {
  I.fillField('#day', '15');
  I.fillField('#month', '10');
  I.fillField('#year', '2025');
  // I.tap('.btn-check');
  I.click('.btn-check');  // nút Check

  I.waitForElement('#modalMessage', 10);
  I.see('is correct date time!', '#modalMessage');
  // I.tap("//button[contains(text(),'OK')]");
});

Scenario('Invalid date flow on mobile (31/02/2025)', async ({ I }) => {
  I.fillField('#day', '31');
  I.fillField('#month', '2');
  I.fillField('#year', '2025');
  // I.tap("//button[contains(text(),'Check')]");
  I.click('.btn-check');  // nút Check

  I.waitForElement('#modalMessage', 10);
  I.see('NOT correct date time!', '#modalMessage');
  // I.tap("//button[contains(text(),'OK')]");
});


