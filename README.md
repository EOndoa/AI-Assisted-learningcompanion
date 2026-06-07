# AI-Assisted Learning Companion Web Application

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![OpenRouter](https://img.shields.io/badge/AI-OpenRouter-6467F2)](https://openrouter.ai/)
[![Deployment](https://img.shields.io/badge/Deployment-Hostinger_VPS-673DE6)](http://187.77.183.24:8080)
[![Status](https://img.shields.io/badge/Status-Live-success)](http://187.77.183.24:8080)

AI-Assisted Learning Companion is a full-stack educational web application that
combines structured online course delivery with AI-powered academic support.
It provides responsive browser interfaces for students and administrators,
server-rendered with Thymeleaf and backed by a Spring Boot application.

Students can enroll in courses, complete lessons and quizzes, monitor their
progress, earn achievements, and download verifiable course certificates.
Administrators can manage the learning catalog, students, assessments, reports,
and AI usage from a dedicated web dashboard.

## Live Demo

The application is deployed on a Hostinger VPS and is accessible at:

**[http://187.77.183.24:8080](http://187.77.183.24:8080)**

The deployment currently uses a public IP address and HTTP. A production domain,
HTTPS certificate, and reverse proxy should be configured before broader public
use.
The test User: 
test@learning.com
password:TestUser1

## Project Objectives

The main objective is to develop an AI-powered educational web platform that
enhances learning through intelligent tutoring, structured course management,
automated assessment, and personalized academic support.

- Provide a centralized online learning environment.
- Support student self-learning through structured academic content.
- Automate quiz generation from lesson material.
- Use artificial intelligence to explain, summarize, and reinforce concepts.
- Track lesson completion, quiz performance, achievements, and course progress.
- Help administrators manage students and academic content efficiently.
- Promote personalized, engaging, and intelligent learning experiences.

## Key Features

### Student Experience

- Account registration, administrator approval, login, and session-based access
- Course discovery and enrollment
- Lesson-based learning paths with completion tracking
- Lesson quizzes, scoring, results, and quiz history
- Personal dashboard and progress overview
- Achievement badges and student leaderboard
- AI learning assistant with lesson-aware responses
- AI-generated lesson summaries
- Downloadable PDF certificates for completed courses
- Public certificate verification

### Administration

- Dashboard with platform activity summaries
- Major, course, lesson, and question management
- PDF upload with automatic lesson text extraction
- AI-assisted quiz generation and regeneration
- Student verification, activation, and role management
- AI usage monitoring
- Learning and performance reports

## Academic Model

Learning content is organized into a simple academic hierarchy:

```text
Major
`-- Course
    `-- Lesson
        `-- Quiz questions
```

For example, a Software Engineering major can contain courses such as
Object-Oriented Programming, Database Systems, Web Development, and
Cybersecurity. Each course contains lessons, and each lesson can have its own
assessment.

## Core Learning Workflows

### Course and lesson management

Administrators can create, edit, categorize, and remove majors, courses, and
lessons. Students can browse the course catalog, enroll in a course, access its
lessons, and continue through their enrolled learning paths.

### PDF learning content

Administrators can upload text-based PDF documents when creating or editing a
lesson. Apache PDFBox extracts the document text and stores it as lesson
content, making existing educational material available inside the web
application. Scanned image-only PDFs require OCR before upload.

### AI learning support

The AI assistant uses the student's question and relevant lesson context to:

- Explain academic concepts in accessible language.
- Summarize lesson material.
- Provide short examples and learning tips.
- Support revision, exam preparation, and independent study.

The administration interface can also generate or regenerate multiple-choice
quiz questions from lesson content through OpenRouter.

### Assessment and progress

Lesson quizzes are graded automatically. The application records correct
answers, scores, percentages, and attempt history. Lesson completion and quiz
performance contribute to course progress, leaderboard rankings, and
achievements.

### Achievements and certificates

Students can earn non-duplicated achievement badges such as `Quiz Master` and
`Lesson Master`. Completing a course unlocks a downloadable PDF certificate
with a public certificate verification workflow.

### Account approval

New accounts follow an administrator-controlled approval process:

```text
Student registration
        |
        v
Account created as pending
        |
        v
Administrator review
        |
        v
Account enabled
        |
        v
Student access granted
```

## Technology Stack

| Layer | Technology |
| --- | --- |
| Web interface | HTML5, CSS3, Bootstrap 5, JavaScript, Thymeleaf |
| Presentation | Server-rendered pages and Spring MVC controllers |
| Backend | Java 21, Spring Boot 4 |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| AI integration | OpenRouter Chat Completions API |
| PDF generation | Apache PDFBox |
| Authentication | HTTP sessions with Spring Security integration |
| Build | Maven |
| Version control | Git and GitHub |
| Deployment | Hostinger VPS, Ubuntu Linux |

## Web Application Architecture

The application follows a layered Model-View-Controller architecture:

```text
Web browser
    |
    | HTTP requests and form submissions
    v
Spring MVC controllers
    |
    | Business operations
    v
Service layer --------> OpenRouter API
    |
    | Data access
    v
Spring Data repositories
    |
    v
MySQL database

Controllers -> Thymeleaf templates -> HTML responses -> Web browser
```

- **Views** provide the student and administrator web interfaces.
- **Controllers** handle navigation, forms, sessions, and API requests.
- **Services** contain learning, assessment, certificate, and AI logic.
- **Repositories** persist application data through Spring Data JPA.
- **External services** provide AI responses and email delivery.

## Data Model

The MySQL database includes the following main entities:

- `User` for student and administrator accounts
- `Major` for academic subject groupings
- `Course` for learning programs within a major
- `Lesson` for course content
- `Question` for lesson assessments
- `QuizResult` for scores and attempt history
- `Enrollment` for student course participation and completion
- `LessonProgress` for lesson completion status
- `Achievement` for earned badges
- `AIUsage` for AI activity monitoring

## Project Structure

```text
src/
|-- main/
|   |-- java/com/etienne/AI_Assisted_learningcompanion/
|   |   |-- config/       # Application and security configuration
|   |   |-- controller/   # MVC and API endpoints
|   |   |-- dto/          # Data transfer objects
|   |   |-- model/        # JPA entities
|   |   |-- repository/   # Spring Data repositories
|   |   `-- service/      # Business logic and external integrations
|   `-- resources/
|       |-- static/       # CSS and image assets
|       |-- templates/    # Thymeleaf views
|       `-- application.properties
`-- test/                 # Automated tests
```

## Prerequisites

Install the following before running the application:

- Java Development Kit 21
- MySQL 8 or later
- Maven 3.9 or later, or use the included Maven Wrapper
- An [OpenRouter](https://openrouter.ai/) API key for AI features

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/EOndoa/AI-Assisted-learningcompanion.git
cd AI-Assisted-learningcompanion
```

### 2. Create the database

Open MySQL and create the application database:

```sql
CREATE DATABASE learning_companion_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Hibernate creates and updates the required tables when the application starts.

### 3. Configure environment variables

The application reads sensitive configuration from environment variables.

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `DB_URL` | No | `jdbc:mysql://localhost:3306/learning_companion_db` | MySQL JDBC URL |
| `DB_USERNAME` | No | `root` | Database username |
| `DB_PASSWORD` | Yes | Empty | Database password |
| `OPENROUTER_API_KEY` | For AI features | Empty | OpenRouter API key |

PowerShell:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your_database_password"
$env:OPENROUTER_API_KEY = "your_openrouter_api_key"
```

Linux or macOS:

```bash
export DB_USERNAME="root"
export DB_PASSWORD="your_database_password"
export OPENROUTER_API_KEY="your_openrouter_api_key"
```

Never commit real credentials or API keys to the repository.

### 4. Run the application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux or macOS:

```bash
./mvnw spring-boot:run
```

Alternatively, with Maven installed:

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in a modern web browser.

The root URL redirects to the login page. Register a student account or create
the first administrator using the instructions below.

## Creating the First Administrator

New registrations are created as disabled `STUDENT` accounts. After registering
an account, promote and enable it directly in MySQL for the initial setup:

```sql
UPDATE users
SET role = 'ADMIN',
    enabled = TRUE,
    verification_token = NULL
WHERE email = 'admin@example.com';
```

Log in with that account and use the administration dashboard to manage later
users and their roles.

## Main Routes

| Route | Purpose |
| --- | --- |
| `/register` | Create a student account |
| `/login` | Sign in |
| `/dashboard` | Student dashboard |
| `/courses` | Browse available courses |
| `/my-learning` | View enrolled courses |
| `/progress` | Review learning progress |
| `/quiz-history` | Review previous quiz attempts |
| `/achievements` | View earned badges |
| `/leaderboard` | View student rankings |
| `/ai-assistant` | Ask the AI learning assistant |
| `/verify-certificate` | Validate a certificate number |
| `/admin` | Open the administration dashboard |
| `POST /api/ask-ai` | Submit a question to the AI API |
| `GET /api/lesson-summary/{lessonId}` | Generate a lesson summary |

## Build and Test

Run the test suite:

```bash
./mvnw test
```

Create an executable application package:

```bash
./mvnw clean package
```

The generated JAR will be available in the `target/` directory.

## Configuration Notes

- The application runs on port `8080` by default.
- JPA schema updates are enabled through `spring.jpa.hibernate.ddl-auto=update`.
- File uploads are limited to 50 MB.
- Uploaded text-based PDFs are converted into lesson content with PDFBox.
- The default AI model is configured in `application.properties`.
- Mail settings contain placeholders and must be configured before email
  delivery can be used.

## Web Deployment

The application is currently deployed as an executable JAR on a Hostinger VPS
running Ubuntu Linux, Java 21, and MySQL 8. The same deployment approach can be
used on another Java-compatible server or cloud platform:

```bash
./mvnw clean package
java -jar target/AI-Assisted-learningcompanion-0.0.1-SNAPSHOT.jar
```

For a hosted environment:

- Provision a MySQL database and set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
- Set `OPENROUTER_API_KEY` securely in the hosting platform.
- Configure mail credentials through environment variables or a production
  configuration profile.
- Place the application behind HTTPS and a reverse proxy.
- Use a managed secret store rather than checked-in configuration values.

### Current deployment

| Component | Environment |
| --- | --- |
| Hosting | Hostinger VPS |
| Operating system | Ubuntu Linux |
| Runtime | Java 21 |
| Database | MySQL 8 |
| Application | Spring Boot executable JAR |
| AI provider | OpenRouter |
| Public endpoint | `http://187.77.183.24:8080` |

The public login page was verified as reachable on June 7, 2026. This confirms
availability at that time, but does not replace uptime monitoring or a
production security review.

## Security Status

This repository is currently suitable for development and academic
demonstration. Before deploying it publicly:

- Replace plaintext password storage with BCrypt or Argon2 hashing.
- Configure Spring Security authorization rules for student and admin routes.
- Enable CSRF protection for state-changing browser requests.
- Replace state-changing `GET` endpoints with `POST`, `PUT`, or `DELETE`.
- Move all mail credentials and deployment settings to environment variables.
- Add input validation, centralized error handling, and rate limiting.
- Use database migrations such as Flyway or Liquibase in production.
- Set `spring.jpa.hibernate.ddl-auto` to `validate` or `none` in production.

## Agile Scrum Methodology

The project was managed using an Agile Scrum approach, organizing development
around prioritized requirements, iterative implementation, review, and
deployment.

### Scrum roles

**Scrum Master and Lead Developer: Etienne Ondoa**

- Sprint planning and task coordination
- Development oversight
- Risk and issue management
- Integration and deployment management

The Product Owner role covers requirements definition and prioritization, while
the development role covers implementation, testing, integration, and
deployment.

### Iterative workflow

```text
Product requirements
        |
        v
Prioritized backlog
        |
        v
Sprint planning
        |
        v
Design and implementation
        |
        v
Testing and review
        |
        v
Deployment and feedback
```

## Future Improvements

- AI-generated personalized study plans
- Course and lesson recommendation engine
- Discussion forums and instructor feedback
- Assignment submission and peer collaboration
- Video-based learning content
- Android and iOS applications
- Progressive Web Application support
- Real-time notifications and in-app messaging
- Expanded analytics dashboards and reports
- Predictive learning insights
- OCR support for scanned PDF documents
- Automated email verification and password recovery
- Broader automated test coverage

## Project Outcomes

This project demonstrates:

- Full-stack web application development with Spring Boot and Thymeleaf
- Layered architecture and object-oriented design
- Relational data modeling and MySQL integration
- AI-assisted learning and automated assessment generation
- PDF content extraction and certificate generation
- Learning management, progress tracking, and gamification
- Agile Scrum project management
- Cloud deployment on an Ubuntu VPS
- Practical Git and GitHub-based software development

## Contributing

Contributions are welcome. Fork the repository, create a focused feature
branch, add or update tests, and submit a pull request with a clear description
of the change.

```bash
git checkout -b feature/your-feature
git commit -m "Add your feature"
git push origin feature/your-feature
```

## Author

Developed by **Etienne Ondoa** ([EOndoa](https://github.com/EOndoa)).
Matricule: ICTU20233886
- Scrum Master and Lead Developer
- Software Engineering student, Final year Level 4 and 2 ICT University

## License

This project was developed for academic, educational, and research purposes.
No open-source license has been added yet.

Copyright (c) 2026 Etienne Ondoa. All rights reserved.
