# Delta App - Backend API

![Delta APP](https://img.shields.io/badge/Delta%20APP-0.1.0-blue)

[Portuguese version](README.md) • [English version](README.en.md)

**Delta App** is an educational platform designed especially for Computer Engineering students, with an initial focus on the UFC - Sobral campus. This repository contains the application's backend API, built in Java with Spring Boot, providing secure and scalable resources to the frontend and other consumers.

---

## Table of Contents

- [Overview](#overview)
- [Main Features](#main-features)
- [Technologies and Dependencies](#technologies-and-dependencies)
- [How to Run Locally](#how-to-run-locally)
- [Project Structure](#project-structure)
- [Maven Scripts](#maven-scripts)
- [Testing](#testing)
- [Contributing](#contributing)
- [Contact](#contact)
- [Release Notes](CHANGELOG.md)

---

## Overview

The Delta App backend API provides RESTful endpoints for authentication, user management, educational resources, database integration, and features specific to the academic domain.

---

## Main Features

- **Authentication and Authorization:** Uses Spring Security with JWT support.
- **User Management:** CRUD operations for users, profiles, and permissions.
- **Data Persistence:** Integration with PostgreSQL via Spring Data JPA.
- **Validation:** Robust validation layer for data security and integrity.
- **Database Migration:** Version control for the database using Flyway.
- **OpenAPI:** Interactive API documentation with Swagger UI.
- **File Upload:** Integration with Google Cloud Storage.
- **Automated Testing:** Coverage for unit and integration tests.
- **HATEOAS:** Dynamic resource navigation via links.

---

## Technologies and Dependencies

- **Java 21**
- **Spring Boot 3.4.5**
    - spring-boot-starter-data-jpa
    - spring-boot-starter-security
    - spring-boot-starter-web
    - spring-boot-starter-validation
    - spring-boot-starter-hateoas
    - spring-boot-devtools
    - springdoc-openapi-starter-webmvc-ui
- **PostgreSQL** (driver: `org.postgresql:postgresql`)
- **Flyway** (database migration)
- **Google Cloud Storage** (`google-cloud-storage`, `google-cloud-storage-control`)
- **Lombok** (boilerplate-free)
- **MapStruct** (DTO/entity mapping)
- **Mockito** (testing)
- **Junit, Spring Security Test** (testing)
- **Docker** (optional, for environment containerization)

---

## How to Run Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/VictorEdu-Dev/delta-app.git
   cd delta-app
   ```

2. **Set environment variables:**  
   Create a `.env` file in the project root and set the necessary variables. Also, configure `application.properties` in `src/main/resources/`:
    ```ini
    # .env (configuration example)
    
    # === External API ===
    API_KEY_YT_V3=your_youtube_api_key
    
    # === Authentication ===
    JWT_SECRET=your_jwt_secret_key
    
    # === Database (PostgreSQL) ===
    PG_URL=your_db_host:5432
    PG_USERNAME=your_db_username
    PG_PASSWORD=your_db_password
    PG_DATABASE=your_db_name
    
    # === Storage Service (Google Cloud Storage) ===
    GCP_PROJECT_ID=your_gcp_project_id
    GCP_BUCKET_NAME=your_gcp_bucket_name
    GCP_FOLDER_PATH=prod/activities/
    GCP_FOLDER_PATH_DOC=doc/
    GCP_FOLDER_PATH_IMG=img/
    ```
    ```
    # application.properties (configuration example)
   
    spring.application.name=delta-app
    
    # Database
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=jdbc:postgresql://${PG_URL}/${PG_DATABASE}?sslmode=require
    spring.datasource.username=${PG_USERNAME}
    spring.datasource.password=${PG_PASSWORD}
    
    spring.jpa.open-in-view=false
    spring.jpa.properties.hibernate.show_sql=true
    spring.jpa.properties.hibernate.hbm2ddl.auto=none
    spring.jpa.properties.hibernate.transaction.jta.platform=org.hibernate.service.jta.platform.internal.NoJtaPlatform
    
    spring.datasource.hikari.max-lifetime=1800000
    spring.datasource.hikari.idle-timeout=600000
    spring.datasource.hikari.connection-timeout=30000
    spring.datasource.hikari.maximum-pool-size=30
    logging.level.com.zaxxer.hikari=warn
    
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:/db.migration
    spring.flyway.baseline-on-migrate=true
    
    # Swagger Config
    springdoc.swagger-ui.disable-swagger-default-url=true
    
    springdoc.swagger-ui.operationsSorter=alpha
    springdoc.swagger-ui.tagsSorter=alpha
    
    app.api.title=Delta Core API
    app.api.description=API for managing tutoring, tracks, and activities of the Delta platform.
    app.api.version=0.1.0
    app.api.contact.name=Victor Eduardo
    app.api.contact.email=suporte@delta.ufc.br
    app.api.license.name=UFC
    app.api.license.url=
    app.api.server.prod.url=https://api.delta.ufc.br
    app.api.server.prod.description=Production Server
    app.api.server.local.url=http://localhost:8080
    app.api.server.local.description=Local Server
    
    # Servlet Container
    spring.servlet.multipart.max-file-size=5MB
    spring.servlet.multipart.max-request-size=5MB
    
    # Storage Service
    gcp.project.id=${GCP_PROJECT_ID}
    gcp.bucket.name=${GCP_BUCKET_NAME}
    gcp.folder.path=${GCP_FOLDER_PATH}
    gcp.folder.path.doc=${GCP_FOLDER_PATH_DOC}
    gcp.folder.path.img=${GCP_FOLDER_PATH_IMG}
    
    # External API
    api.key.yt.v3=${API_KEY_YT_V3}
    
    # JWT Token
    jwt.secret=${JWT_SECRET}
    ```    

    - Set up Google Cloud Storage credentials if you intend to use file uploads.

3. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
    - By default, the API will be available at `http://localhost:8080/`.

5. **Access the interactive documentation:**
    - Swagger/OpenAPI: `http://localhost:8080/swagger-ui.html` or `/swagger-ui/index.html`

---

## Project Structure

```
delta-app/
├── src/
│   ├── main/
│   │   ├── java/org/deltacore/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   ├── repositories/
│   │   │   ├── services/
│   │   │   └── DeltaAppApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/
│       └── java/org/deltacore/
├── pom.xml
├── Dockerfile
├── CHANGELOG.md
├── README.en.md
└── README.md
```

---

## Maven Scripts

- `./mvnw clean install` — Compiles and tests the project.
- `./mvnw spring-boot:run` — Runs the server locally.
- `./mvnw test` — Runs all automated tests.

---

## Testing

Unit and integration tests are located in `src/test/java/org/deltacore/`.  
Run them with:

```bash
./mvnw test
```

---

## Contributing

1. Fork this repository.
2. Create a branch for your feature or fix (`git checkout -b my-feature`).
3. Commit your changes (`git commit -am 'My contribution'`).
4. Push to your branch (`git push origin my-feature`).
5. Open a Pull Request detailing your changes.

---

## Contact

- **Author:** Victor Eduardo Pita Campos
- **Email:** victoreduardodev@gmail.com
- **Institution:** UFC - Computer Engineering
- **GitHub:** [VictorEdu-Dev](https://github.com/VictorEdu-Dev)

---

> Educational platform for Computer Engineering - UFC Sobral campus.