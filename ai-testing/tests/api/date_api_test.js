const assert = require('assert').strict;

Feature('DateTime API');

// API base: POST /check with body { day, month, year } -> returns JSON with message

Scenario('valid date should be accepted', async ({ I }) => {
  const res = await I.sendPostRequest('/check', {
    day: '15',
    month: '10',
    year: '2025'
  });

  assert.equal(res.status, 200);
  assert.ok(res.data && typeof res.data.message === 'string');
  assert.ok(res.data.message.includes('is correct date time'));
});

Scenario('invalid date should be rejected (31/02/2025)', async ({ I }) => {
  const res = await I.sendPostRequest('/check', {
    day: '31',
    month: '2',
    year: '2025'
  });

  assert.equal(res.status, 200);
  assert.ok(res.data && typeof res.data.message === 'string');
  assert.ok(res.data.message.includes('NOT correct date time'));
});

Scenario('day out of valid range should be rejected (35/05/2025)', async ({ I }) => {
  const res = await I.sendPostRequest('/check', {
    day: '35',
    month: '5',
    year: '2025'
  });

  assert.equal(res.status, 200);
  // Backend treats this as invalid date, not explicit "out of range"
  assert.ok(res.data && typeof res.data.message === 'string');
  assert.ok(res.data.message.includes('NOT correct date time'));
});

Scenario('non-numeric input should return validation error', async ({ I }) => {
  const res = await I.sendPostRequest('/check', {
    day: 'aa',
    month: 'bb',
    year: 'cc'
  });

  assert.equal(res.status, 200);
  assert.ok(res.data && typeof res.data.message === 'string');
  assert.ok(res.data.message.includes('Lỗi'));
});


