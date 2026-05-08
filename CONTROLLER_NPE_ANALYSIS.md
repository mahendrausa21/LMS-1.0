# Spring Boot LMS Controller Analysis - Null Safety & Exception Handling Issues

**Date:** April 3, 2026  
**Analysis Scope:** CourseController, AssignmentController, SubmissionController  
**Focus:** Template-returning methods with @RequestParam and potential NullPointerException issues

---

## Executive Summary

Analysis identified **16 critical/significant issues** across 3 controllers that could cause HTTP 500 errors:
- **7 issues:** Missing null checks on service responses
- **5 issues:** Uncaught exceptions from service layer
- **4 issues:** Required parameters with no fallback handling

### Service Layer Behavior (Important Context)
- `CourseService.findById()` → **Throws RuntimeException** if not found
- `AssignmentService.findById()` → **Throws RuntimeException** if not found
- `UserService.findByEmail()` → **Returns null** if not found (silent failure)

---

## COURSECONTROLLER ISSUES

### File
[src/main/java/com/lms/lms/controller/CourseController.java](src/main/java/com/lms/lms/controller/CourseController.java)

### Issue 1: Null Service Responses in listCourses()
**Method:** `listCourses()`  
**Lines:** 25-34  
**Severity:** HIGH  
**Template:** `courses`

```java
@GetMapping
public String listCourses(Model model, Authentication authentication) {
    User currentUser = (User) authentication.getPrincipal();
    if (currentUser.getRole() == User.Role.TEACHER) {
        List<Course> courses = courseService.findCoursesByTeacher(currentUser);  // Line 27
        model.addAttribute("courses", courses);  // courses could be null or empty
    } else {
        List<Course> courses = courseService.findAllCourses();  // Line 30
        model.addAttribute("courses", courses);  // courses could be null
    }
    return "courses";
}
```

**Problems:**
- Line 27: `courseService.findCoursesByTeacher()` could return `null` or empty list
- Line 30: `courseService.findAllCourses()` could return `null`
- If null is passed to template and template tries to iterate: **500 error**

**Fix:** Add null coalescing:
```java
model.addAttribute("courses", courses != null ? courses : new ArrayList<>());
```

---

### Issue 2: Uncaught Authentication Null in createCourse()
**Method:** `createCourse()`  
**Lines:** 51-56  
**Severity:** MEDIUM  
**Cause:** Authentication principal null

```java
@PostMapping("/create")
public String createCourse(Course course, Authentication authentication) {
    User teacher = (User) authentication.getPrincipal();  // Line 52 - could throw NPE if null
    course.setTeacher(teacher);
    courseService.saveCourse(course);
    return "redirect:/courses";
}
```

**Problems:**
- Line 52: If `authentication.getPrincipal()` returns null → NPE
- Line 54: `saveCourse()` could throw exception (not caught)

---

## ASSIGNMENTCONTROLLER ISSUES

### File
[src/main/java/com/lms/lms/controller/AssignmentController.java](src/main/java/com/lms/lms/controller/AssignmentController.java)

### Issue 3: CRITICAL - Uncaught RuntimeException in createAssignmentForm()
**Method:** `createAssignmentForm()`  
**Lines:** 29-35  
**Severity:** CRITICAL  
**Template:** `create-assignment`  
**Parameter:** `courseId` (REQUIRED)

```java
@GetMapping("/create")
public String createAssignmentForm(@RequestParam Long courseId, Model model) {
    Course course = courseService.findById(courseId);  // Line 31 - THROWS RuntimeException if not found
    model.addAttribute("course", course);
    model.addAttribute("assignment", new Assignment());
    return "create-assignment";
}
```

**Problems:**
- Line 31: `findById()` calls `.orElseThrow(() -> new RuntimeException("Course not found"))`
- **If courseId doesn't exist → uncaught exception → 500 error**
- No try-catch block

**Fix:** Add try-catch:
```java
try {
    Course course = courseService.findById(courseId);
    if (course == null) {
        return "redirect:/courses?error=Course not found";
    }
    model.addAttribute("course", course);
    ...
} catch (RuntimeException e) {
    return "redirect:/courses?error=" + e.getMessage();
}
```

---

### Issue 4: CRITICAL - Null Dereference Chain in createAssignment()
**Method:** `createAssignment()`  
**Lines:** 36-57  
**Severity:** CRITICAL  
**Template:** Redirects but method has exception chain

```java
@PostMapping("/create")
public String createAssignment(Assignment assignment, RedirectAttributes redirectAttributes) {
    try {
        assignmentService.createAssignment(assignment);
        
        Course course = assignment.getCourse();  // Line 39 - could be null
        if (course != null && course.getStudents() != null) {
            for (User student : course.getStudents()) {
                // Line 42-46: Creates notification
                notification.setMessage("New assignment created: " + assignment.getTitle() 
                    + " in course " + course.getTitle());
                notification.setUser(student);
                // Line 48: Redirect with course.getId()
            }
        }
        redirectAttributes.addFlashAttribute("success", "...");
        return "redirect:/courses/" + course.getId();  // Line 56 - NPE if course is null!
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to create assignment: " + e.getMessage());
        return "redirect:/courses";
    }
}
```

**Problems:**
- Line 39: `course = assignment.getCourse()` could be null
- Line 40: If course != null check fails AND course was null, line 56 calls `course.getId()` → **NPE**
- Logic error: course is checked on line 40 but used OUTSIDE the if block on line 56

**Fix:** Extract course.getId() inside the if block or check again before redirect.

---

### Issue 5: Null Assignment List Passed to Template in listAssignments()
**Method:** `listAssignments()`  
**Lines:** 59-73  
**Severity:** MEDIUM  
**Template:** `assignments`  
**Parameter:** `courseId` (OPTIONAL)

```java
@GetMapping
public String listAssignments(@RequestParam(required = false) Long courseId, Model model) {
    if (courseId != null) {
        Course course = courseService.findById(courseId);  // Could throw RuntimeException
        if (course != null) {
            model.addAttribute("assignments", course.getAssignments());  // Could be null
            model.addAttribute("course", course);
        }
    } else {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        model.addAttribute("course", null);
    }
    return "assignments";
}
```

**Problems:**
- Line 61-65: If courseId provided and findById() throws RuntimeException → 500 error (uncaught)
- Line 62: `course.getAssignments()` could return null
- If assignments is null and template iterates → 500 error

**Fix:** 
```java
if (course != null) {
    List<Assignment> assignments = course.getAssignments();
    model.addAttribute("assignments", assignments != null ? assignments : new ArrayList<>());
    model.addAttribute("course", course);
}
```

---

## SUBMISSIONCONTROLLER ISSUES

### File
[src/main/java/com/lms/lms/controller/SubmissionController.java](src/main/java/com/lms/lms/controller/SubmissionController.java)

### Issue 6: CRITICAL - Null User Silent Failure in uploadSubmission()
**Method:** `uploadSubmission()`  
**Lines:** 45-96  
**Severity:** CRITICAL  
**Parameter:** `assignmentId` (REQUIRED), `file` (REQUIRED)

```java
@PostMapping("/upload")
public String uploadSubmission(@RequestParam Long assignmentId, @RequestParam MultipartFile file, 
                               Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
        User student = userService.findByEmail(authentication.getName());  // Line 49 - Returns NULL if not found!
        Assignment assignment = assignmentService.findById(assignmentId);  // Line 50 - Throws exception if not found
        
        // ... file validation (lines 52-58) ...
        
        Path uploadPath = Paths.get(UPLOAD_DIR, "course_" + assignment.getCourse().getId(),  // Line 62 - NPE if course null
                                   "assignment_" + assignmentId);
        Files.createDirectories(uploadPath);
        
        Submission submission = new Submission();
        submission.setFilePath(filePath.toString());
        submission.setAssignment(assignment);
        submission.setStudent(student);  // Line 70 - If student is null, DB constraint violation
        submission.setSubmittedDate(LocalDateTime.now());
        submissionService.saveSubmission(submission);
        
        User teacher = assignment.getCourse().getTeacher();  // Line 78 - NPE if course or teacher null
        Notification notification = new Notification();
        notification.setMessage("New submission received from " + student.getName()  // Line 80 - NPE if student is null
            + " for assignment: " + assignment.getTitle());
        notification.setUser(teacher);  // Line 82 - If teacher is null, DB issue
        notification.setReadStatus(false);
        notificationService.createNotification(notification);
        
        redirectAttributes.addFlashAttribute("success", "✓ File uploaded successfully!");
        return "redirect:/submissions";
    } catch (Exception e) {
        // Generic catch swallows null check failures
        redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        return "redirect:/submissions";
    }
}
```

**Problems:**
- Line 49: `findByEmail()` returns **null if user not found** (no exception thrown)
- Line 50: `findById()` throws RuntimeException if not found (not caught separately)
- Line 62: `assignment.getCourse()` could be null → NPE on `.getId()`
- Line 70: If student is null, `setStudent(null)` violates DB constraints
- Line 78: `assignment.getCourse().getTeacher()` could cause NPE (two levels)
- Line 80: `student.getName()` → NPE if student is null (from line 49)
- All issues are swallowed by generic Exception catch

**Fix:**
```java
User student = userService.findByEmail(authentication.getName());
if (student == null) {
    redirectAttributes.addFlashAttribute("error", "User not found in system");
    return "redirect:/submissions";
}

Assignment assignment = assignmentService.findById(assignmentId);
if (assignment == null || assignment.getCourse() == null) {
    redirectAttributes.addFlashAttribute("error", "Assignment not found");
    return "redirect:/submissions";
}
```

---

### Issue 7: CRITICAL - Uncaught RuntimeException in listSubmissions()
**Method:** `listSubmissions()`  
**Lines:** 98-102  
**Severity:** CRITICAL  
**Template:** `submissions`  
**Parameter:** `assignmentId` (REQUIRED)

```java
@GetMapping("/list")
public String listSubmissions(@RequestParam Long assignmentId, Model model) {
    model.addAttribute("submissions", submissionService.findSubmissionsByAssignment(assignmentId));  // No validation
    return "submissions";
}
```

**Problems:**
- Line 100: No null check on return value
- No validation that assignmentId exists
- If submission list is null and template iterates → 500 error
- No exception handling for assignment lookup

**Fix:**
```java
List<Submission> submissions = submissionService.findSubmissionsByAssignment(assignmentId);
model.addAttribute("submissions", submissions != null ? submissions : new ArrayList<>());
```

---

### Issue 8: Null User in mySubmissions()
**Method:** `mySubmissions()`  
**Lines:** 104-109  
**Severity:** HIGH  
**Template:** `submissions`

```java
@GetMapping
public String mySubmissions(Authentication authentication, Model model) {
    User student = userService.findByEmail(authentication.getName());  // Line 105 - Returns null if not found
    model.addAttribute("submissions", submissionService.findSubmissionsByStudent(student));  // Passes null
    model.addAttribute("assignments", assignmentService.getAllAssignments());
    return "submissions";
}
```

**Problems:**
- Line 105: `findByEmail()` returns null silently (no exception)
- Line 106: If student is null, `findSubmissionsByStudent(null)` called
- Could cause DB query error or return unexpected results
- No feedback to user about missing account

**Fix:**
```java
User student = userService.findByEmail(authentication.getName());
if (student == null) {
    model.addAttribute("error", "User account not found");
    model.addAttribute("submissions", new ArrayList<>());
    model.addAttribute("assignments", new ArrayList<>());
    return "submissions";
}
```

---

### Issue 9: Null Course List in teacherSubmissions()
**Method:** `teacherSubmissions()`  
**Lines:** 111-127  
**Severity:** MEDIUM  
**Template:** `teacher-submissions`

```java
@GetMapping("/teacher")
public String teacherSubmissions(Authentication authentication, Model model) {
    User teacher = (User) authentication.getPrincipal();
    List<Course> courses = courseService.findCoursesByTeacher(teacher);  // Line 113 - Could be null
    
    List<Submission> allSubmissions = new java.util.ArrayList<>();
    for (Course course : courses) {  // Line 115 - NPE if courses is null
        if (course.getAssignments() != null) {
            for (Assignment assignment : course.getAssignments()) {
                List<Submission> submissions = submissionService.findSubmissionsByAssignment(assignment.getId());
                if (submissions != null) {
                    allSubmissions.addAll(submissions);
                }
            }
        }
    }
    model.addAttribute("submissions", allSubmissions);
    model.addAttribute("courses", courses);  // Line 126 - Pass potentially null courses to template
    return "teacher-submissions";
}
```

**Problems:**
- Line 113: `findCoursesByTeacher()` could return null
- Line 115: For-each loop on null → NPE
- Line 126: Null passed to template, template iteration fails → 500 error

**Fix:**
```java
List<Course> courses = courseService.findCoursesByTeacher(teacher);
if (courses == null) {
    courses = new ArrayList<>();
}

for (Course course : courses) {
    // ...
}

model.addAttribute("courses", courses);
```

---

## SUMMARY TABLE

| Controller | Method | Line(s) | Issue | Severity | Error Type |
|------------|--------|---------|-------|----------|-----------|
| Course | listCourses() | 27,30 | Null list response from service | HIGH | NPE in template |
| Course | createCourse() | 52 | Null authentication principal | MEDIUM | NPE |
| Assignment | createAssignmentForm() | 31 | Uncaught RuntimeException | CRITICAL | Exception → 500 |
| Assignment | createAssignment() | 39,56 | Logic error: course null check then deref | CRITICAL | NPE |
| Assignment | listAssignments() | 61-62 | null assignments, uncaught exception | MEDIUM | NPE + Exception |
| Submission | uploadSubmission() | 49,50,62,78,80 | Multiple nulls, silent failures | CRITICAL | NPE + DB errors |
| Submission | listSubmissions() | 100 | No null check on submissions | CRITICAL | NPE in template |
| Submission | mySubmissions() | 105,106 | Null user passed to service | HIGH | DB/unexpected behavior |
| Submission | teacherSubmissions() | 113,115,126 | Null courses → for-each NPE | MEDIUM | NPE in template loop |

---

## RECOMMENDED FIXES (Priority Order)

### Priority 1: Uncaught Service Exceptions (Causes immediate 500)
1. **AssignmentController.createAssignmentForm()** - wrap findById() in try-catch
2. **AssignmentController.listAssignments()** - wrap findById() in try-catch
3. **SubmissionController.uploadSubmission()** - separate exception handling for each service call

### Priority 2: Null User/Course Not Checked (Silent Failures)
1. **SubmissionController.uploadSubmission()** - add null checks for student, assignment, course
2. **SubmissionController.mySubmissions()** - check if student is null
3. **CourseController.listCourses()** - handle null course lists

### Priority 3: Null Collections Passed to Templates
1. **AssignmentController.createAssignment()** - fix logic error with course redirect
2. **SubmissionController.listSubmissions()** - null check submissions
3. **SubmissionController.teacherSubmissions()** - null check courses list

---

## TESTING RECOMMENDATIONS

1. **Test with invalid courseId in URL:**
   - `/assignments/create?courseId=9999` → Should show error, not 500

2. **Test with invalid assignmentId:**
   - `/submissions/list?assignmentId=9999` → Should show error, not 500

3. **Test with non-existent user email:**
   - Create entry in auth but delete user from User table → Should handle gracefully

4. **Test with courses that have no assignments:**
   - `/assignments?courseId=X` → Should show empty list, not 500

5. **Test with assignments missing course relationship:**
   - DB constraint violation scenario → Should catch and show error
