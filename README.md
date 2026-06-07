# AI-Assisted Learning Companion

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![OpenRouter](https://img.shields.io/badge/AI-OpenRouter-6467F2)](https://openrouter.ai/)

AI-Assisted Learning Companion is a web-based learning management platform that
combines structured course delivery with AI-powered academic support. Students
can enroll in courses, complete lessons and quizzes, monitor their progress,
earn achievements, and download verifiable course certificates. Administrators
can manage the learning catalog, students, assessments, reports, and AI usage
from a dedicated dashboard.

## Key Features

### Student Experience

- Account registration, verification, login, and session-based access
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
- AI-assisted quiz generation and regeneration
- Student verification, activation, and role management
- AI usage monitoring
- Learning and performance reports

## Technology Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 4 |
| Web | Spring Web MVC, Thymeleaf |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| AI integration | OpenRouter Chat Completions API |
| PDF generation | Apache PDFBox |
| Security | Spring Security and HTTP sessions |
| Build | Maven |
| Frontend | HTML, CSS, Thymeleaf templates |

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

Open [http://localhost:8080](http://localhost:8080) in your browser.

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
- The default AI model is configured in `application.properties`.
- Mail settings contain placeholders and must be configured before email
  delivery can be used.

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

Developed by [EOndoa](https://github.com/EOndoa).

