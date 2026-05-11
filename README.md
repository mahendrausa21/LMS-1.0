# 📚 Learning Management System (LMS)

## What is this project?

This is a **Learning Management System (LMS)** - a complete platform for managing courses, assignments, grades, and student/teacher interactions online. Think of it like a digital classroom where students can enroll in courses, submit assignments, check grades, and interact with teachers.

---

## 🚀 Key Features

### **For Students:**
- ✅ Register and login to the system
- ✅ Browse and enroll in available courses
- ✅ Download course materials and resources
- ✅ Submit assignments with file uploads
- ✅ Check grades and feedback from teachers
- ✅ Participate in discussion forums
- ✅ Receive notifications about deadlines
- ✅ View personal dashboard with progress

### **For Teachers:**
- ✅ Create and manage courses
- ✅ Upload course materials (PDFs, documents, etc.)
- ✅ Create and publish assignments with due dates
- ✅ View and grade student submissions
- ✅ Provide feedback and comments
- ✅ Manage enrollments
- ✅ Track student progress
- ✅ Participate in forum discussions

### **General Features:**
- 🔐 Secure login with password hashing
- 📧 Email notifications for events
- 📊 Dashboard with statistics
- 💾 File upload and download support (up to 10MB per file)
- 🎨 Modern, clean, and professional user interface

---

## 📋 Project Structure

```
lms-1.0/
├── src/
│   ├── main/
│   │   ├── java/com/lms/lms/
│   │   │   ├── controller/          → Handles user requests (15+ controllers)
│   │   │   ├── service/             → Business logic layer (10+ services)
│   │   │   ├── model/               → Database models/entities (User, Course, etc.)
│   │   │   ├── repository/          → Database access layer
│   │   │   ├── config/              → Application configuration
│   │   │   └── LmsApplication.java  → Main entry point
│   │   └── resources/
│   │       ├── application.properties → Configuration file
│   │       ├── templates/           → HTML pages (Thymeleaf templates)
│   │       └── static/              → CSS, JavaScript, images
│   └── test/java/                   → Test files
├── pom.xml                          → Maven dependencies
├── mvnw / mvnw.cmd                  → Maven wrapper for easy builds
└── uploads/                         → Student file submissions
```

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| **Backend Language** | Java 21 |
| **Framework** | Spring Boot 3.5.6 |
| **Database** | MySQL |
| **Template Engine** | Thymeleaf (HTML rendering) |
| **Security** | Spring Security (password encryption, authentication) |
| **Build Tool** | Maven |
| **Frontend** | HTML5, CSS3, JavaScript |

---

## 📁 What Each Folder Does

### **Controller Layer** (`controller/` folder)
Handles all user requests and returns responses:
- `AuthController` - Login & Registration
- `CourseController` - View and manage courses
- `AssignmentController` - Create and manage assignments
- `SubmissionController` - Handle student submissions
- `GradeController` - Grade submissions and view grades
- `UserController` - User profile management
- `EnrollmentController` - Course enrollment
- `ForumController` - Discussion forums
- `NotificationController` - Notifications
- `DashboardController` - Dashboard display
- `HomeController` - Homepage
- `GlobalExceptionHandler` - Error handling

### **Service Layer** (`service/` folder)
Contains business logic and rules:
- `UserService` - User management
- `CourseService` - Course operations
- `AssignmentService` - Assignment logic
- `SubmissionService` - Submission handling
- `GradeService` - Grading system
- `EnrollmentService` - Course enrollment
- `ForumPostService` - Forum management
- `NotificationService` - Notifications
- `CourseMaterialService` - Material management
- `CustomUserDetailsService` - User authentication

### **Model Layer** (`model/` folder)
Database tables represented as Java classes:
- `User` - Student and Teacher accounts
- `Course` - Course information
- `Assignment` - Assignment details
- `Submission` - Student submissions
- `Grade` - Grades and feedback
- `Enrollment` - Student course enrollments
- `ForumPost` - Forum discussions
- `Notification` - System notifications
- `CourseMaterial` - Course resources

### **Repository Layer** (`repository/` folder)
Database access and queries:
- Automatically connects models to MySQL database
- Uses Spring Data JPA (Java Persistence API)

### **Static Files** (`resources/static/`)
Frontend assets:
- **CSS Files**: Style sheets for modern UI
  - `style.css` - Core design system
  - `layout.css` - Page layouts
  - `components.css` - UI components
  - `main.css`, `modern.css`, `sidebar.css` - Additional styles
- **JavaScript**: Interactive functionality
  - `app.js` - Main application logic
  - `modern.js` - Modern features

### **Templates** (`resources/templates/`)
HTML pages displayed to users:
- `login.html` - Login page
- `register.html` - Registration page
- `home.html` - Homepage
- `courses.html` - Courses listing
- `course-form.html` - Create/edit course
- `assignments.html` - Assignments view
- `create-assignment.html` - Create assignment
- `submissions.html` - View submissions
- `grades.html` - View grades
- `student-dashboard.html` - Student dashboard
- `teacher-dashboard.html` - Teacher dashboard
- `forum.html` - Discussion forum
- `notifications.html` - Notifications
- `enrollment.html` - Course enrollment
- `layout.html` - Base layout template

---

## 🔧 How to Set Up and Run

### **Prerequisites:**
1. **Java 21** installed on your computer
2. **MySQL Server** running
3. **Maven** (optional - comes with the project as `mvnw`)

### **Step 1: Configure Database**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### **Step 2: Create MySQL Database**
```sql
CREATE DATABASE lms_db;
```

### **Step 3: Build the Project**
```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### **Step 4: Run the Application**
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### **Step 5: Access the Application**
Open your browser and go to:
```
http://localhost:8080
```

### **Step 6: Login**
- Use default test credentials or register a new account
- Choose between Student or Teacher role

---

## 📊 Database Schema Overview

### **Main Tables:**

| Table | Purpose | Key Fields |
|-------|---------|-----------|
| `user` | User accounts | id, username, email, password, role, name |
| `course` | Course information | id, title, description, teacher_id, created_date |
| `assignment` | Assignments | id, title, description, course_id, due_date |
| `submission` | Student submissions | id, assignment_id, student_id, file_path, submitted_date |
| `grade` | Grades & feedback | id, submission_id, score, feedback, graded_date |
| `enrollment` | Course enrollments | id, student_id, course_id, enrolled_date |
| `forum_post` | Forum discussions | id, course_id, user_id, title, content, created_date |
| `notification` | System notifications | id, user_id, message, type, created_date |
| `course_material` | Course resources | id, course_id, file_path, uploaded_date |

---

## 🔒 Security Features

- ✅ **Password Encryption** - Passwords are hashed using bcrypt
- ✅ **Authentication** - Login required for all features
- ✅ **Authorization** - Different permissions for Students and Teachers
- ✅ **Role-Based Access** - Students see only their courses; Teachers manage their courses
- ✅ **Session Management** - Secure session handling
- ✅ **File Upload Validation** - Maximum 10MB file size limit

---

## 🎨 User Interface Features

- **Modern Design** - Clean, professional appearance with smooth animations
- **Responsive Layout** - Works on desktop, tablet, and mobile devices
- **Sidebar Navigation** - Easy access to all features
- **Dashboard** - Quick overview of courses, assignments, and grades
- **Real-time Notifications** - Updates on important events
- **Dark/Light Mode Support** - Comfortable viewing

---

## 📱 Main User Flows

### **Student Flow:**
1. Register account as Student
2. Browse available courses
3. Enroll in courses
4. View course materials
5. Submit assignments
6. Check grades
7. Participate in forums

### **Teacher Flow:**
1. Register account as Teacher
2. Create new courses
3. Upload course materials
4. Create assignments
5. Review student submissions
6. Grade assignments and add feedback
7. Track student progress

---

## 🐛 Error Handling

The application includes:
- **Global Exception Handler** - Catches and handles errors gracefully
- **Custom Error Pages** - 400, 403, 404, 500 error pages
- **Validation** - Input validation on forms
- **Logging** - Tracks issues for debugging

---

## 📝 Configuration File

`application.properties` contains:
- Database connection settings
- File upload limits (10MB)
- Thymeleaf template configuration
- JPA/Hibernate settings
- Error handling configuration

---

## 🚀 Development Tips

### **Adding a New Feature:**
1. Create entity in `model/`
2. Create repository in `repository/`
3. Create service in `service/`
4. Create controller in `controller/`
5. Create HTML templates in `templates/`

### **Common Tasks:**
- **Add new course field**: Edit `Course.java` model + database migration
- **Create new page**: Add HTML in `templates/` + CSS in `static/css/`
- **Add validation**: Use Spring Validation annotations
- **Query database**: Use repository methods or write custom queries

---

## 📚 File Upload Feature

- Students can upload assignment submissions
- Maximum file size: **10MB**
- Files stored in: `uploads/course_X/assignment_Y/`
- Supported formats: All file types accepted

---

## 🧪 Testing

Test files are located in `src/test/java/`
- Unit tests for services
- Controller tests
- Integration tests

Run tests:
```bash
mvnw.cmd test
```

---

## 📄 Additional Documentation Files

- `DESIGN_SYSTEM.md` - UI/UX design documentation
- `TESTING_GUIDE.md` - Testing procedures
- `ERROR_RESOLUTION_REPORT.md` - Common errors and fixes
- `HELP.md` - Additional help resources

---

## 🤝 Contributing

To contribute to this project:
1. Create a feature branch
2. Make changes
3. Test thoroughly
4. Submit for review

---



### **Common Issues:**

| Issue | Solution |
|-------|----------|
| Can't connect to database | Check MySQL is running; verify credentials in `application.properties` |
| Port 8080 in use | Change port in `application.properties`: `server.port=8081` |
| File upload fails | Check `uploads/` folder exists; verify permissions |
| Login doesn't work | Ensure user is registered; check password spelling |

---

## 📦 Version Information

- **Spring Boot Version**: 3.5.6
- **Java Version**: 21
- **MySQL Version**: 8.0+
- **Maven Version**: 3.6+

---

## 📋 Checklist for New Users

- [ ] Install Java 21
- [ ] Install MySQL and create database
- [ ] Update `application.properties` with database credentials
- [ ] Run `mvnw.cmd clean install`
- [ ] Run `mvnw.cmd spring-boot:run`
- [ ] Open `http://localhost:8080` in browser
- [ ] Register a new account
- [ ] Explore the dashboard

---

## 🎓 Learning Objectives

This project demonstrates:
- Spring Boot web application development
- MVC (Model-View-Controller) architecture
- Database design and SQL
- User authentication and authorization
- File upload/download handling
- RESTful API concepts
- Thymeleaf templating
- Spring Security
- Modern web UI/UX design

---

## 📄 License

This is a demo/learning project for educational purposes.

---

## 🎉 Congratulations!

You now have a complete Learning Management System ready to use! Start creating courses and engaging with students today.

For more detailed information, refer to the other documentation files in the project root.

## 📞 Support 
mahendrausirikayala@gmail.com

