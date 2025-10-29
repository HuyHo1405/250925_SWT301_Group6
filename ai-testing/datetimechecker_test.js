Feature('Datetime Checker');

Scenario('Check valid date', async ({ I }) => {
    I.amOnPage('http://localhost:8080');  // link trang của bạn
    I.fillField('#day', '13');
    I.fillField('#month', '10');
    I.fillField('#year', '2025');
    I.click('.btn-check');  // nút Check
    I.see('Valid datetime'); // text mà bạn hiển thị sau khi kiểm tra
});

Scenario('Check invalid date', async ({ I }) => {
    I.amOnPage('http://localhost:8080');
    I.fillField('#day', '31');
    I.fillField('#month', '2');
    I.fillField('#year', '2025');
    I.click('.btn-check');
    I.see('Invalid datetime'); // text mong đợi
});
