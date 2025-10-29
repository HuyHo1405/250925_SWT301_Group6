# SYSTEM TEST DEFECT LOG
## DateTimeChecker Application

**Project:** DateTimeChecker  
**Test Level:** System Testing  
**Test Phase:** Integration & E2E Testing  
**Document Version:** 1.0  
**Date:** October 23, 2025  

---

## Defect Summary

| Defect ID | Severity | Status | Module | Summary |
|-----------|----------|--------|---------|---------|
| ST-001 | High | Open | UI Form Validation | Missing client-side validation for empty fields |
| ST-002 | Medium | Open | API Response | Inconsistent error message format |
| ST-003 | High | Open | Date Validation | Accepts invalid characters in input fields |
| ST-004 | Low | Open | UI/UX | No loading indicator during API call |
| ST-005 | Medium | Open | Error Handling | Unclear error message for special characters |

---

## Detailed Defect Reports

### **Defect ID: ST-001**
**Title:** Missing client-side validation for empty fields  
**Severity:** High  
**Priority:** High  
**Status:** Open  
**Reported By:** Tester Name  
**Date Reported:** 2025-10-23  

**Module/Component:** Frontend - Form Validation  
**Environment:** 
- Browser: Chrome 119, Firefox 120
- OS: Windows 11
- Application URL: http://localhost:8080

**Description:**  
When user clicks "Check" button without entering any values in Day, Month, or Year fields, the form submits to backend without client-side validation. This causes unnecessary server requests.

**Steps to Reproduce:**
1. Navigate to http://localhost:8080
2. Leave all fields (Day, Month, Year) empty
3. Click "Check" button
4. Observe the behavior

**Expected Result:**  
- Client-side validation should trigger immediately
- Display error message: "Please fill in all fields"
- Prevent form submission to server

**Actual Result:**  
- Form submits to backend
- Server returns error after delay
- Poor user experience with network latency

**Attachments:**
- Screenshot: `screenshots/ST-001-empty-fields.png`
- Network trace showing unnecessary API call

**Suggested Fix:**
Add JavaScript validation in `datetime-checker.js`:
```javascript
function checkDateTime() {
    const day = document.getElementById('day').value.trim();
    const month = document.getElementById('month').value.trim();
    const year = document.getElementById('year').value.trim();
    
    if (!day || !month || !year) {
        showModal('Please fill in all fields!');
        return;
    }
    // ... continue with API call
}
```

**Related Test Cases:** TC-UI-001, TC-UI-002

---

### **Defect ID: ST-002**
**Title:** Inconsistent error message format between client and server  
**Severity:** Medium  
**Priority:** Medium  
**Status:** Open  
**Reported By:** Tester Name  
**Date Reported:** 2025-10-23  

**Module/Component:** API Response Formatting  
**Environment:**
- Spring Boot version: 3.5.6
- API Endpoint: POST /check

**Description:**  
Error messages returned from the server are inconsistent. Some messages are in Vietnamese ("Lỗi: Vui lòng nhập số hợp lệ...") while success messages are in English. This creates confusion for international users.

**Steps to Reproduce:**
1. Enter non-numeric values in any field (e.g., "abc" for day)
2. Click "Check" button
3. Observe the error message language

**Expected Result:**  
- All messages should be in consistent language (English for international app)
- Use proper i18n (internationalization) approach
- Example: "Error: Please enter valid numbers for day, month, and year!"

**Actual Result:**  
```json
{
  "day": "abc",
  "month": "1",
  "year": "2024",
  "message": "Lỗi: Vui lòng nhập số hợp lệ cho ngày, tháng, năm!"
}
```

**Suggested Fix:**
Update `DateTimeCheckerService.process()` method:
```java
catch (NumberFormatException e) {
    model.setMessage("Error: Please enter valid numbers for day, month, and year!");
}
```

**Impact:** Medium - Affects user experience, especially for non-Vietnamese speakers

**Related Test Cases:** TC-API-005, TC-I18N-001

---

### **Defect ID: ST-003**
**Title:** Input fields accept invalid characters (letters, special chars)  
**Severity:** High  
**Priority:** High  
**Status:** Open  
**Reported By:** Tester Name  
**Date Reported:** 2025-10-23  

**Module/Component:** Frontend Input Validation  
**Environment:**
- Browser: Chrome 119
- Input Fields: Day, Month, Year

**Description:**  
HTML input fields allow users to type any characters including letters and special characters. This should be prevented at the input level for better UX.

**Steps to Reproduce:**
1. Navigate to http://localhost:8080
2. Try typing letters (e.g., "abc") in Day field
3. Try typing special characters (e.g., "!@#$%") in Month field
4. Observe that all characters are accepted

**Expected Result:**  
- Input fields should only accept numeric characters (0-9)
- Show visual feedback when invalid character is typed
- Or use `type="number"` with proper min/max constraints

**Actual Result:**  
- All characters are accepted
- Validation only happens after submission
- Poor user experience

**Suggested Fix:**
Update `index.html`:
```html
<input type="number" id="day" name="day" min="1" max="31" 
       placeholder="Enter day (1-31)" required>
<input type="number" id="month" name="month" min="1" max="12" 
       placeholder="Enter month (1-12)" required>
<input type="number" id="year" name="year" min="1000" max="3000" 
       placeholder="Enter year (1000-3000)" required>
```

Or add JavaScript input filter:
```javascript
document.getElementById('day').addEventListener('input', function(e) {
    this.value = this.value.replace(/[^0-9]/g, '');
});
```

**Related Test Cases:** TC-UI-003, TC-INPUT-001

---

### **Defect ID: ST-004**
**Title:** No loading indicator during API call  
**Severity:** Low  
**Priority:** Low  
**Status:** Open  
**Reported By:** Tester Name  
**Date Reported:** 2025-10-23  

**Module/Component:** UI - User Feedback  
**Environment:**
- Network: Slow 3G simulation
- Browser: All browsers

**Description:**  
When user clicks "Check" button, there's no visual feedback that the application is processing the request. On slow networks, users may think the application is frozen.

**Steps to Reproduce:**
1. Use browser DevTools to simulate slow 3G network
2. Enter valid date: 15/10/2024
3. Click "Check" button
4. Observe lack of loading indicator during 2-3 second delay

**Expected Result:**  
- Show loading spinner or disable button with "Processing..." text
- Provide visual feedback that system is working

**Actual Result:**  
- No visual feedback
- Button remains clickable (users may click multiple times)
- Poor user experience on slow connections

**Suggested Fix:**
Add to `datetime-checker.js`:
```javascript
function checkDateTime() {
    // Show loading state
    const checkBtn = document.querySelector('.btn-check');
    checkBtn.disabled = true;
    checkBtn.textContent = 'Checking...';
    
    fetch('/check', {...})
        .finally(() => {
            // Reset button state
            checkBtn.disabled = false;
            checkBtn.textContent = 'Check';
        });
}
```

**Related Test Cases:** TC-UX-001

---

### **Defect ID: ST-005**
**Title:** Unclear error message when entering special characters  
**Severity:** Medium  
**Priority:** Medium  
**Status:** Open  
**Reported By:** Tester Name  
**Date Reported:** 2025-10-23  

**Module/Component:** Error Handling & User Messages  
**Environment:**
- Application URL: http://localhost:8080
- Test Data: Day=@#$, Month=12, Year=2024

**Description:**  
When user enters special characters in date fields, the error message is generic and doesn't explain what went wrong clearly.

**Steps to Reproduce:**
1. Enter "@#$" in Day field
2. Enter "12" in Month field
3. Enter "2024" in Year field
4. Click "Check"

**Expected Result:**  
Clear error message: "Day field contains invalid characters. Please enter numbers only (1-31)."

**Actual Result:**  
Generic message: "Lỗi: Vui lòng nhập số hợp lệ cho ngày, tháng, năm!"

**Suggested Fix:**
Add input-specific validation and error messages:
```java
try {
    if (!model.getDay().matches("\\d+")) {
        model.setMessage("Error: Day must contain only numbers (1-31)");
        return;
    }
    // ... similar for month and year
} catch (NumberFormatException e) {
    model.setMessage("Error: Please enter valid numbers");
}
```

**Related Test Cases:** TC-ERROR-001, TC-VALIDATION-003

---

## Defect Statistics

### By Severity
- **Critical:** 0
- **High:** 2 (ST-001, ST-003)
- **Medium:** 2 (ST-002, ST-005)
- **Low:** 1 (ST-004)

### By Status
- **Open:** 5
- **In Progress:** 0
- **Fixed:** 0
- **Closed:** 0
- **Deferred:** 0

### By Module
- **Frontend/UI:** 3 defects
- **Backend/API:** 1 defect
- **UX/Feedback:** 1 defect

---

## Test Environment Details

**Application Stack:**
- Framework: Spring Boot 3.5.6
- Java Version: 21
- Frontend: Thymeleaf + Vanilla JavaScript
- Build Tool: Maven

**Test Environment:**
- OS: Windows 11
- Browsers: Chrome 119, Firefox 120, Edge 118
- Test Server: http://localhost:8080
- Database: N/A (stateless application)

**Test Tools:**
- Manual Testing
- Browser DevTools
- Network Throttling
- CodeceptJS (for E2E automation)

---

## Notes

1. **Priority for Fix:** ST-001 and ST-003 should be fixed first as they significantly impact user experience
2. **Testing Approach:** Recommend implementing client-side validation before server-side processing
3. **Code Coverage:** Backend validation is solid; focus on frontend improvements
4. **Regression Testing:** After fixes, retest all input validation scenarios

---

## Approval

**Prepared By:** ________________  
**Reviewed By:** ________________  
**Approved By:** ________________  
**Date:** ________________
