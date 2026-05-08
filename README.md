# Learning Management System (LMS)

> A full-featured, production-ready Learning Management System built with **Spring Boot 3.x**, **Spring MVC**, and **Thymeleaf**. Demonstrates enterprise-grade architecture with role-based access control, file management, and comprehensive course administration.

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)

## 📋 Overview

LMS is a comprehensive learning platform that enables educators to create, manage, and deliver courses while students engage with course materials, submit assignments, and track their progress. Built with a focus on **clean architecture**, **security**, and **scalability**, this project demonstrates professional-grade backend development practices.

### Key Capabilities

- **Multi-role Access Control**: Admin, Instructor, and Student dashboards with role-based permissions
- **Course Management**: Create, publish, and manage courses with rich content support
- **Assignment & Assessment System**: Assign work, collect submissions, and provide feedback
- **File Upload/Download**: Secure file handling with validation and virus scanning readiness
- **User Authentication**: BCrypt password encoding and session-based security
- **Responsive UI**: Modern Thymeleaf templates with Bootstrap integration
- **Production-Ready Configuration**: Environment-specific configurations and error handling

---

## 🏗️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.x (latest stable)
- **Web**: Spring MVC, Thymeleaf
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security with BCrypt
- **Build**: Maven 3.9+

### Frontend
- **Templates**: Thymeleaf
- **Styling**: Bootstrap 5, Custom CSS
- **Scripts**: Vanilla JavaScript, AJAX

### Database
- **Primary**: MySQL 8.0+
- **Driver**: MySQL Connector/J

### Additional Libraries
- **File Handling**: Apache Commons FileUpload
- **Validation**: Hibernate Validator
- **Logging**: SLF4J with Logback

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)** 17 or higher
  ```bash
  java -version  # Verify installation
  ```
- **Maven** 3.9+
  ```bash
  mvn -version   # Verify installation
  ```
- **MySQL Server** 8.0+
  ```bash
  mysql --version  # Verify installation
  ```

### Installation

#### 1. Clone the Repository
```bash
git clone https://github.com/mahendrausa21/LMS-1.0.git
cd LMS-1.0
```

#### 2. Database Setup

Create a new MySQL database and user:

```sql
CREATE DATABASE lms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON lms_db.* TO 'lms_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. Configure Application Properties

Update `src/main/resources/application.properties` with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=lms_user
spring.datasource.password=secure_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Application Configuration
server.port=8080
spring.application.name=LMS
```

#### 4. Build the Project

```bash
mvn clean package
```

This will compile the code and package it as a JAR file. The output will be in `target/`.

#### 5. Run the Application

**Option A: Using Spring Boot Maven Plugin**
```bash
mvn spring-boot:run
```

**Option B: Running the JAR File**
```bash
java -jar target/LMS-1.0.jar
```

#### 6. Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

---

## 📂 Project Structure

```
LMS-1.0/
├── src/
│   ├── main/
│   │   ├── java/com/lms/
│   │   │   ├── controller/          # Request handlers for all endpoints
│   │   │   ├── service/             # Business logic layer
│   │   │   ├── repository/          # Data access objects (Spring Data JPA)
│   │   │   ├── entity/              # JPA entity classes
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── config/              # Spring configuration classes
│   │   │   ├── security/            # Authentication and authorization
│   │   │   ├── util/                # Utility classes
│   │   │   └── exception/           # Custom exception classes
│   │   ├── resources/
│   │   │   ├── application.properties  # Configuration file
│   │   │   ├── templates/           # Thymeleaf HTML templates
│   │   │   │   ├── admin/
│   │   │   │   ├── instructor/
│   │   │   │   ├── student/
│   │   │   │   └── common/
│   │   │   ├── static/              # CSS, JavaScript, images
│   │   │   │   ├── css/
│   │   │   │   ├── js/
│   │   │   │   └── images/
│   │   │   └── i18n/                # Internationalization properties
│   │   └── webapp/
│   └── test/                        # Unit and integration tests
├── pom.xml                          # Maven project configuration
├── mvnw & mvnw.cmd                  # Maven wrapper scripts
└── README.md                        # This file
```

---

## 🎯 Core Features

### 1. User Authentication & Authorization
- Secure login with BCrypt password hashing
- Role-based access control (RBAC): Admin, Instructor, Student
- Session management with Spring Security

### 2. Admin Dashboard
- User management (create, edit, delete users)
- Course oversight and monitoring
- System configuration and analytics
- User role assignment

### 3. Instructor Features
- **Course Management**: Create and publish courses
- **Content Management**: Upload course materials (PDF, images, videos)
- **Assessment Tools**: Create assignments and quizzes
- **Grading System**: View submissions and provide feedback
- **Student Progress**: Track student performance and engagement

### 4. Student Features
- **Course Enrollment**: Browse and enroll in available courses
- **Learning Dashboard**: Track progress across courses
- **Assignment Submission**: Submit work with file upload capability
- **Grade Tracking**: View grades and feedback from instructors
- **Course Materials**: Access downloadable course resources

### 5. File Management
- Secure file upload/download with validation
- Organized file storage by course and user
- File type and size restrictions
- Error handling for storage operations

### 6. Database Design
- Normalized schema with proper relationships
- Indexes on frequently queried columns
- Audit columns for created/modified timestamps
- Foreign key constraints for data integrity

---

## 🔧 Configuration

### Environment Variables (Optional)

Create a `.env` file in the root directory for sensitive configuration:

```bash
DB_URL=jdbc:mysql://localhost:3306/lms_db
DB_USERNAME=lms_user
DB_PASSWORD=your_secure_password
SERVER_PORT=8080
```

### Logging Configuration

Logging is configured via `logback-spring.xml`. Adjust log levels in application properties:

```properties
logging.level.root=INFO
logging.level.com.lms=DEBUG
logging.level.org.springframework=WARN
```

---

## 🧪 Testing

Run unit and integration tests with Maven:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Generate coverage report
mvn clean test jacoco:report
```

---

## 📊 API Endpoints (Key Examples)

### Authentication
- `POST /login` - User login
- `POST /logout` - User logout
- `POST /register` - User registration (Admin only)

### Courses
- `GET /courses` - List all courses
- `POST /courses/create` - Create new course (Instructor)
- `GET /courses/{id}` - View course details
- `POST /courses/{id}/enroll` - Enroll in course (Student)

### Assignments
- `GET /assignments` - List assignments
- `POST /assignments/{id}/submit` - Submit assignment
- `GET /submissions/{id}` - View submission details

### Admin
- `GET /admin/users` - Manage users
- `POST /admin/users/create` - Create new user
- `GET /admin/dashboard` - System analytics

---

## 🔐 Security Considerations

- **Password Security**: All passwords are hashed using BCrypt with strong salt rounds
- **SQL Injection Prevention**: Uses parameterized queries via Spring Data JPA
- **CSRF Protection**: Enabled by default in Spring Security
- **Session Security**: HttpOnly and Secure flags on session cookies
- **File Upload Security**: File type validation and size restrictions
- **Input Validation**: Bean validation with Hibernate Validator

### Recommended Production Practices

- Use HTTPS instead of HTTP
- Configure CORS appropriately
- Implement rate limiting on login attempts
- Enable HTTPS-only session cookies
- Regularly update dependencies for security patches

---

## 🚀 Deployment

### Docker (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/LMS-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t lms:1.0 .
docker run -p 8080:8080 lms:1.0
```

### Traditional Server Deployment

1. Build the JAR: `mvn clean package`
2. Transfer JAR to server
3. Set environment variables for database connection
4. Run: `java -jar LMS-1.0.jar`

---

## 📈 Performance Optimizations

- **Database Indexing**: Indexed commonly queried fields
- **Lazy Loading**: Configured on JPA relationships for optimal query performance
- **Caching**: Spring Cache integration for frequently accessed data
- **Connection Pooling**: HikariCP for efficient database connections

---

## 🐛 Troubleshooting

### Common Issues

#### 1. "Cannot get a connection, pool error"
**Solution**: Verify MySQL is running and database credentials are correct in `application.properties`.

#### 2. "File upload exceeds maximum allowed size"
**Solution**: Increase `spring.servlet.multipart.max-file-size` in application properties.

#### 3. "BCryptPasswordEncoder not found"
**Solution**: Ensure Spring Security dependency is included in `pom.xml`.

#### 4. "Thymeleaf template not found"
**Solution**: Verify template file exists in `src/main/resources/templates/` with correct naming.

### Debug Mode

Run with debug logging:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

Or set in application properties:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate=DEBUG
```

---

## 📚 Learning Resources

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Tutorial](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
- [Spring Security Reference](https://spring.io/projects/spring-security)

---

## 🔄 Contributing

This is a personal portfolio project. If you'd like to suggest improvements or report issues:

1. Open an issue with a clear description
2. Fork the repository and create a feature branch
3. Commit your changes and push to the branch
4. Submit a pull request with detailed description

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Mahendra**

- GitHub: [@mahendrausa21]( https://github.com/mahendrausa21)
- portfolio: https://mahendrausa21.github.io/my-portfolio/
- Email: mahendrausirikayala@gmail.com

---

## 🙏 Acknowledgments

- Spring Boot and Spring Framework communities
- MySQL and database design best practices
- Bootstrap for responsive UI components
- Open source community for educational resources

---

## 📞 Support

For questions or support, please:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Review existing GitHub issues
3. Open a new issue with detailed information

---

**Last Updated**: May 2026 | **Version**: 1.0

