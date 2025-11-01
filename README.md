# DiagnosisAI (Backend)

DiagnosisAI is a Spring Boot service that accepts clinical documents as file uploads, extracts and merges their text, and sends the merged content to an ML service for diagnostic assistance. It stores patients and reports in MongoDB and exposes simple REST APIs for running analysis and retrieving results.

## Key features
- Upload up to five clinical document parts as multipart form-data:
  - chief_complaint
  - history_of_present_illness
  - relevant_exam_findings
  - lab_investigation
  - ongoing_treatments
- Robust text extraction using Apache Tika (PDF, DOCX, images with embedded text, many others)
- Text merging with clear section headers before sending to ML
- Pluggable external ML API via `ml.api.url`
- MongoDB persistence for patients and reports
- Open CORS for development (allow all origins)
- Simple health check and test endpoints

## Tech stack
- Java 17, Spring Boot 3
- Spring Web, Spring Data MongoDB, Spring Mail
- Apache Tika (tika-core, tika-parsers-standard-package)

## Project layout (high level)
- `com.vrsabu.diagnosisai.web`
  - `AnalysisController` – `/api/analysis/diagnose/{id}` accepts multipart uploads and triggers ML analysis
  - `PatientController` – CRUD-like operations for patients and report retrieval
  - `ReportController` – persists ML report for a patient
  - `GlobalExceptionHandler` – centralized error responses
- `com.vrsabu.diagnosisai.service`
  - `AnalysisService`/`AnalysisServiceImpl` – orchestrates extraction → merge → ML call → persist
  - `TextExtractionService`/`TextExtractionServiceImpl` – Apache Tika text extraction
  - `PatientService`, `ReportService`, `EmailService`
- `com.vrsabu.diagnosisai.repository` – Mongo repositories (e.g., `PatientRepository`, `ReportRepository`)
- `com.vrsabu.diagnosisai.config`
  - `CorsConfig` – allows all origins (development)
  - `MlApiProperties` – ML API base URL (`ml.api.url`)

## Configuration
Set the following in `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# MongoDB (use either the full URI or host/port/db)
spring.data.mongodb.uri=mongodb://localhost:27017/diagnosisdb
# spring.data.mongodb.host=localhost
# spring.data.mongodb.port=27017
# spring.data.mongodb.database=diagnosisdb

# External ML service
ml.api.url=http://localhost:8000/api/analyze

# Email (optional, required if you use EmailService)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your_smtp_username
spring.mail.password=your_smtp_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Notes
- If you see `Database name must not be empty`, ensure `spring.data.mongodb.uri` (or `spring.data.mongodb.database`) is set.
- CORS is currently wide open for development in `CorsConfig`.
- EmailService currently uses a hard-coded recipient for demo. Adjust it to use the patient's email or a request parameter in production.

## Build and run
You can use the Maven Wrapper included in the repo.

```sh
./mvnw clean package -DskipTests
java -jar target/DiagnosisAI-0.0.1-SNAPSHOT.jar
```

Or run directly with Spring Boot:

```sh
./mvnw spring-boot:run
```

## REST API

Base URL: `http://localhost:8080`

- Health
  - GET `/api/analysis/health`
  - 200 OK: `Analysis Service is up and running.`

- Analyze documents
  - POST `/api/analysis/diagnose/{id}` (multipart/form-data)
  - Parts (all optional but useful):
    - `chief_complaint` (file)
    - `history_of_present_illness` (file)
    - `relevant_exam_findings` (file)
    - `lab_investigation` (file)
    - `ongoing_treatments` (file)
  - Response: `MlReportResponse` JSON

  Example cURL:
  ```sh
  curl -X POST \
    -F chief_complaint=@./samples/cc.pdf \
    -F history_of_present_illness=@./samples/hpi.docx \
    -F relevant_exam_findings=@./samples/ref.pdf \
    -F lab_investigation=@./samples/lab.txt \
    -F ongoing_treatments=@./samples/ot.pdf \
    http://localhost:8080/api/analysis/diagnose/USER_ID
  ```

- Patients
  - POST `/api/user/create`
    - Body: `CreateUserRequest` JSON (fields: `name`, `age`, `gender`, `email`)
    - Response: Created patient
  - GET `/api/user`
    - Response: `List<GetAllUserResponse>`
  - GET `/api/user/{id}`
    - Response: `{ "reports": List<GetUserReportResponse> }`
  - GET `/api/user/send-report`
    - Sends a demo email using `EmailService` (requires mail config; for demo/testing only)

- Reports
  - POST `/api/report/{id}`
    - Body: `MlReportResponse` JSON
    - Persists a report for patient `{id}`

## How it works (analysis flow)
1. Client uploads up to five files to `/api/analysis/diagnose/{id}`.
2. `TextExtractionService` uses Apache Tika to pull text from each file.
3. Text is merged with section headers (e.g., "Chief Complaint:") to preserve context.
4. The merged text is sent to the external ML endpoint configured by `ml.api.url`.
5. The ML response is returned to the client and can be stored via `ReportController`.

## CORS
`CorsConfig` enables global CORS with:
- `allowedOriginPatterns("*")`
- `allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")`
- `allowCredentials(true)`

This is convenient for development. For production, restrict origins to trusted frontends.

## Troubleshooting
- `package org.springframework.http does not exist` (or other Spring classes not found)
  - Ensure the project is imported as a Maven project and that dependencies are downloaded. The project relies on Spring Boot starters.
- `Database name must not be empty`
  - Set `spring.data.mongodb.uri` (or `spring.data.mongodb.database`).
- CORS errors in the browser
  - CORS is globally allowed. If you still see errors, ensure the browser is sending requests to the correct port and that credentials/cookies match your CORS needs.
- Email not sending
  - Verify `spring.mail.*` properties and that your SMTP provider credentials are valid.

## Development tips
- Java 17 is the target. Use JDK 17+.
- After changing `pom.xml`, refresh Maven in your IDE.
- Add integration tests for controllers under `src/test/java` (some tests already exist).

## License
Add your license here (e.g., MIT). If this is private code, mark it proprietary.

