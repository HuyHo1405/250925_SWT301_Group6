Feature('E2E Date Time Checker');

// Yêu cầu: Ứng dụng chạy tại http://localhost:8080

Before(({ I }) => {
  I.amOnPage('http://localhost:8080/');
});

Scenario('Valid date shows success message', async ({ I }) => {
  I.fillField('#day', '15');
  I.fillField('#month', '10');
  I.fillField('#year', '2025');
  I.click("//button[contains(text(),'Check')]");

  I.waitForElement('#modalMessage', 5);
  I.see('is correct date time', '#modalMessage');
  I.click("//button[contains(text(),'OK')]");
});

Scenario('Invalid date (31/02/2025) shows error message', async ({ I }) => {
  I.fillField('#day', '31');
  I.fillField('#month', '2');
  I.fillField('#year', '2025');
  I.click("//button[contains(text(),'Check')]");

  I.waitForElement('#modalMessage', 5);
  I.see('NOT correct date time', '#modalMessage');
  I.click("//button[contains(text(),'OK')]");
});

Scenario('Empty input triggers empty validation', async ({ I }) => {
  // Đảm bảo trống
  I.fillField('#day', '');
  I.fillField('#month', '');
  I.fillField('#year', '');

  I.click("//button[contains(text(),'Check')]");
  I.waitForElement('#modalMessage', 5);
  I.see('cannot be empty', '#modalMessage');
  I.click("//button[contains(text(),'OK')]");
});

Scenario('Out of range day triggers range validation', async ({ I }) => {
  I.fillField('#day', '35');
  I.fillField('#month', '5');
  I.fillField('#year', '2025');
  I.click("//button[contains(text(),'Check')]");

  I.waitForElement('#modalMessage', 5);
  I.see('out of range', '#modalMessage');
  I.click("//button[contains(text(),'OK')]");
});

Scenario('Clear button clears all inputs', async ({ I }) => {
  I.fillField('#day', '10');
  I.fillField('#month', '10');
  I.fillField('#year', '2025');
  I.click("//button[contains(text(),'Clear')]");

  I.seeInField('#day', '');
  I.seeInField('#month', '');
  I.seeInField('#year', '');
});


