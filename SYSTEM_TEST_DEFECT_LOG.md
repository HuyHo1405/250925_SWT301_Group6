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
| ST-006 | Critical | Open | Backend API | Server crashes with NullPointerException on null fields |
| ST-007 | High | Open | Backend API | API lacks independent server-side validation, vulnerable to bypass |
| ST-008 | Medium | Open | Backend API | Generic error messages don't specify which field has error |
| ST-009 | Medium | Open | Backend API | Generic exception messages are in Vietnamese, inconsistent |
| ST-010 | Medium | Open | Frontend Input | Input fields allow paste of invalid characters |

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

### **Defect ID: ST-006**
**Title:** Server crashes with NullPointerException when API receives null field values  
**Severity:** Critical  
**Priority:** Critical  
**Status:** Open  
**Reported By:** System Test Analysis  
**Date Reported:** 2025-10-23  

**Module/Component:** Backend API - Input Validation  
**Environment:**
- API Endpoint: POST /check
- Request Body: `{"day": null, "month": null, "year": null}`
- Spring Boot version: 3.5.6

**Description:**  
When API endpoint receives a request with null values in day, month, or year fields, the server throws `NullPointerException` because the code calls `.trim()` on null without checking. This causes server crash and returns HTTP 500 error to client.

**Steps to Reproduce:**
1. Use API client (Postman, curl, or direct JavaScript fetch) to send POST request:
   ```json
   POST http://localhost:8080/check
   Content-Type: application/json
   {
     "day": null,
     "month": null,
     "year": null
   }
   ```
2. Or send request with one field as null:
   ```json
   {
     "day": null,
     "month": "10",
     "year": "2025"
   }
   ```
3. Observe server error

**Expected Result:**  
- Server should handle null values gracefully
- Return appropriate error message: "Error: Please enter all required fields (day, month, year)"
- HTTP status should be 200 with error message in response body

**Actual Result:**  
- Server throws `NullPointerException`
- HTTP 500 Internal Server Error returned
- Stack trace exposed: `Cannot invoke "String.trim()" because the return value of "com.DateTimeChecker.demo.model.DateTimeModel.getDay()" is null`

**Root Cause:**
```java
// DateTimeCheckerService.java, lines 23-25
int year = Integer.parseInt(model.getYear().trim());  // NPE if getYear() is null
int month = Integer.parseInt(model.getMonth().trim());  // NPE if getMonth() is null
int day = Integer.parseInt(model.getDay().trim());  // NPE if getDay() is null
```

**Suggested Fix:**
```java
@Override
public void process(DateTimeModel model) {
    try {
        if (model == null) {
            throw new IllegalArgumentException("Input data is empty");
        }
        
        // Validate null fields BEFORE calling trim()
        if (model.getDay() == null || model.getMonth() == null || model.getYear() == null) {
            model.setMessage("Error: Please enter all required fields (day, month, year)");
            return;
        }
        
        // Check for empty strings after trim
        if (model.getDay().trim().isEmpty() || 
            model.getMonth().trim().isEmpty() || 
            model.getYear().trim().isEmpty()) {
            model.setMessage("Error: Day, month, and year cannot be empty");
            return;
        }

        // Now safe to call trim()
        int year = Integer.parseInt(model.getYear().trim());
        int month = Integer.parseInt(model.getMonth().trim());
        int day = Integer.parseInt(model.getDay().trim());
        // ... rest of logic
    } catch (NumberFormatException e) {
        model.setMessage("Error: Please enter valid numbers for day, month, and year!");
    }
}
```

**Impact:** Critical - Server crash vulnerability, can be exploited by malicious users or integration failures

**Related Test Cases:** TC-API-007, TC-SECURITY-001

---

### **Defect ID: ST-007**
**Title:** API lacks independent server-side validation, vulnerable to frontend bypass  
**Severity:** High  
**Priority:** High  
**Status:** Open  
**Reported By:** System Test Analysis  
**Date Reported:** 2025-10-23  

**Module/Component:** Backend API - Security & Validation  
**Environment:**
- API Endpoint: POST /check
- Attack Vector: Direct API calls bypassing frontend validation

**Description:**  
The backend API relies heavily on frontend validation. Users can bypass client-side checks by directly calling the API endpoint with invalid data, potentially causing server errors or unexpected behavior.

**Steps to Reproduce:**
1. Bypass frontend by sending direct API request with invalid data:
   ```json
   POST /check
   {
     "day": "999",
     "month": "99",
     "year": "99999"
   }
   ```
2. Or send extremely long strings:
   ```json
   {
     "day": "111111111111111111111111111111",
     "month": "222222222222222222222222222222",
     "year": "333333333333333333333333333333"
   }
   ```
3. Observe server response

**Expected Result:**  
- Server should validate all inputs independently
- Return clear error messages for out-of-range values
- Prevent integer overflow (Integer.parseInt may throw NumberFormatException for very large numbers)
- Handle edge cases gracefully

**Actual Result:**  
- Server processes invalid range values (day=999) and returns "NOT correct date time" but doesn't explicitly reject out-of-range inputs
- Large numbers may cause NumberFormatException but error message is generic

**Suggested Fix:**
Add server-side validation in `process()` method:
```java
// Validate ranges BEFORE date validation
if (year < 1000 || year > 3000) {
    model.setMessage("Error: Year must be between 1000 and 3000");
    return;
}
if (month < 1 || month > 12) {
    model.setMessage("Error: Month must be between 1 and 12");
    return;
}
if (day < 1 || day > 31) {
    model.setMessage("Error: Day must be between 1 and 31");
    return;
}
```

**Impact:** High - Security vulnerability, can cause DoS or unexpected behavior

**Related Test Cases:** TC-API-008, TC-SECURITY-002

---

### **Defect ID: ST-008**
**Title:** Generic error messages don't specify which field has the error  
**Severity:** Medium  
**Priority:** Medium  
**Status:** Open  
**Reported By:** System Test Analysis  
**Date Reported:** 2025-10-23  

**Module/Component:** Backend API - Error Messages  
**Environment:**
- API Endpoint: POST /check
- Test Data: Multiple invalid fields simultaneously

**Description:**  
When multiple fields have errors or when user submits invalid data, the error message is generic and doesn't indicate which specific field(s) contain errors. This makes debugging and user correction difficult.

**Steps to Reproduce:**
1. Send API request with multiple invalid fields:
   ```json
   POST /check
   {
     "day": "abc",
     "month": "xyz",
     "year": "invalid"
   }
   ```
2. Observe error message returned

**Expected Result:**  
- Clear indication of which field(s) have errors
- Example: "Error: Day must contain only numbers (1-31). Month must contain only numbers (1-12). Year must contain only numbers (1000-3000)."
- Or field-by-field validation: "Error: Day field contains invalid characters. Please enter numbers only."

**Actual Result:**  
Generic message: "Lỗi: Vui lòng nhập số hợp lệ cho ngày, tháng, năm!"  
This doesn't tell user which field(s) need correction.

**Suggested Fix:**
```java
// Validate each field separately and collect errors
List<String> errors = new ArrayList<>();

if (model.getDay() == null || model.getDay().trim().isEmpty()) {
    errors.add("Day field is required");
} else if (!model.getDay().trim().matches("\\d+")) {
    errors.add("Day must contain only numbers (1-31)");
} else {
    try {
        int day = Integer.parseInt(model.getDay().trim());
        if (day < 1 || day > 31) {
            errors.add("Day must be between 1 and 31");
        }
    } catch (NumberFormatException e) {
        errors.add("Day must be a valid number");
    }
}

// Similar for month and year...

if (!errors.isEmpty()) {
    model.setMessage("Error: " + String.join(". ", errors));
    return;
}
```

**Impact:** Medium - Poor user experience, especially for non-technical users

**Related Test Cases:** TC-ERROR-002, TC-UX-002

---

### **Defect ID: ST-009**
**Title:** Generic exception messages are in Vietnamese, inconsistent with application  
**Severity:** Medium  
**Priority:** Medium  
**Status:** Open  
**Reported By:** System Test Analysis  
**Date Reported:** 2025-10-23  

**Module/Component:** Backend API - Exception Handling  
**Environment:**
- Service: DateTimeCheckerService.process()
- Exception: Generic Exception catch block

**Description:**  
When unexpected exceptions occur (non-NumberFormatException, non-IllegalArgumentException), the catch-all exception handler returns a Vietnamese error message, inconsistent with the rest of the application which should be in English for international users.

**Steps to Reproduce:**
1. Cause an unexpected exception (e.g., mock service failure, memory issues, or unexpected data format)
2. Observe error message in response

**Expected Result:**  
- All error messages should be in English for consistency
- Generic error: "Error: An unexpected error occurred. Please try again or contact support."

**Actual Result:**  
```java
catch (Exception e) {
    model.setMessage("Đã xảy ra lỗi không xác định!");  // Vietnamese
}
```

**Suggested Fix:**
```java
catch (Exception e) {
    // Log exception for debugging
    System.err.println("Unexpected error in DateTimeCheckerService: " + e.getMessage());
    e.printStackTrace();
    
    // Return user-friendly English message
    model.setMessage("Error: An unexpected error occurred. Please try again or contact support.");
}
```

**Impact:** Medium - Inconsistency in error messaging, affects internationalization

**Related Test Cases:** TC-ERROR-003, TC-I18N-002

---

### **Defect ID: ST-010**
**Title:** Input fields allow paste of invalid characters despite client-side validation  
**Severity:** Medium  
**Priority:** Low  
**Status:** Open  
**Reported By:** System Test Analysis  
**Date Reported:** 2025-10-23  

**Module/Component:** Frontend - Input Handling  
**Environment:**
- Browser: All browsers
- Input fields: Day, Month, Year (type="text")

**Description:**  
While the JavaScript validation prevents direct typing of invalid characters, users can paste text containing letters or special characters into the input fields. The validation only triggers on form submission, not on paste events.

**Steps to Reproduce:**
1. Navigate to http://localhost:8080
2. Copy text containing invalid characters: "abc123!@#"
3. Paste into Day field (Ctrl+V or right-click paste)
4. Observe that pasted text is accepted
5. Click "Check" button
6. Validation only triggers after submission

**Expected Result:**  
- Input fields should filter out invalid characters on paste
- Either strip non-numeric characters automatically, or show immediate error on paste
- Better UX: Only numeric characters should remain after paste

**Actual Result:**  
- Pasted invalid text is accepted into field
- Validation only happens on "Check" button click
- User must manually delete invalid characters

**Suggested Fix:**
Add paste event handler in `datetime-checker.js`:
```javascript
document.addEventListener('DOMContentLoaded', function() {
    const inputs = ['day', 'month', 'year'];
    
    inputs.forEach(id => {
        const input = document.getElementById(id);
        
        // Handle paste event
        input.addEventListener('paste', function(e) {
            e.preventDefault();
            const pastedText = (e.clipboardData || window.clipboardData).getData('text');
            // Strip non-numeric characters
            const numericOnly = pastedText.replace(/[^0-9]/g, '');
            this.value = numericOnly;
        });
        
        // Handle input event to filter as user types
        input.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    });
});
```

**Impact:** Low-Medium - Minor UX issue, but validation still works correctly on submission

**Related Test Cases:** TC-UI-004, TC-INPUT-002

---

## Defect Statistics

### By Severity
- **Critical:** 1 (ST-006)
- **High:** 3 (ST-001, ST-003, ST-007)
- **Medium:** 5 (ST-002, ST-005, ST-008, ST-009, ST-010)
- **Low:** 1 (ST-004)

### By Status
- **Open:** 10
- **In Progress:** 0
- **Fixed:** 0
- **Closed:** 0
- **Deferred:** 0

### By Module
- **Frontend/UI:** 4 defects (ST-001, ST-003, ST-004, ST-010)
- **Backend/API:** 6 defects (ST-002, ST-005, ST-006, ST-007, ST-008, ST-009)
- **UX/Feedback:** 1 defect (ST-004)

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

1. **Critical Priority for Fix:** ST-006 must be fixed immediately as it causes server crashes and is a critical security vulnerability
2. **High Priority Fixes:** ST-001, ST-003, and ST-007 should be addressed next as they significantly impact user experience and security
3. **Testing Approach:** 
   - Recommend implementing comprehensive server-side validation independent of frontend
   - Test API endpoints directly to ensure they handle all edge cases
   - Implement null checking and input validation at service layer
4. **Code Coverage:** Backend validation needs improvement for null handling and server-side input validation
5. **Security Considerations:** ST-006 and ST-007 represent security vulnerabilities that should be patched before production deployment
6. **Regression Testing:** After fixes, retest all input validation scenarios including null values, out-of-range values, and direct API calls

---

## Approval

**Prepared By:** ________________  
**Reviewed By:** ________________  
**Approved By:** ________________  
**Date:** ________________
