# Oauth Registrator Application

## Introduction
A simple application for user registration and authentication to be consumed by other APIs. It supports two environments: development (H2 database) and production (PostgreSQL), with environment variables for sensitive data in production.

---

## Features
- **User Registration and Authentication**
- **Token-Based Security**
- **Environment-Specific Configurations**:
    - Development: H2 (in-memory database)
    - Production: PostgreSQL (via environment variables)
- **Database Migrations**: Flyway for versioned schema migrations
- **API Documentation**: Integrated Swagger UI
- **Docker Support**: Scripts for Linux and Windows to build and run Docker containers
- **Planned Features**:

---

## Requirements
- [Docker](https://docs.docker.com/engine/install/)
- PostgreSQL (for production environment)

---

## Configuration

### Development Environment (`dev`)
- **Database**:
    - H2 (in-memory database)
    - URL: `jdbc:h2:mem:testdb`
    - Driver: `org.h2.Driver`
    - Username: `sa`
    - Password: `password`
    - Hibernate dialect: `org.hibernate.dialect.H2Dialect`
    - Flyway migration scripts location: `classpath:memory`
- **JPA Settings**:
    - Hibernate DDL auto: `update`
    - Show SQL: `true`
    - Generate DDL: `true`
- **Security**:
    - Public and private keys: `classpath:/public_key.pem` and `classpath:/private_key.pem`
- **Server**:
    - Port: `8080`
- **Flyway**:
    - `baseline-on-migrate`: `true`

### Production Environment (`prod`)
- **Database**:
    - PostgreSQL
    - URL: `${URL_DB}:${PORT_DB}/${NAME_DB}`
    - Username: `${USER_DB}`
    - Password: `${PASS_DB}`
    - Hibernate dialect: `org.hibernate.dialect.PostgreSQLDialect`
    - Flyway migration scripts location: `classpath:db/migration`
- **JPA Settings**:
    - Hibernate DDL auto: `update`
    - Show SQL: `true`
    - Generate DDL: `true`
- **Security**:
    - Public and private keys: `${PUB_KEY}` and `${PRIV_KEY}`
- **Server**:
    - Port: `8080`
- **Flyway**:
    - `baseline-on-migrate`: `true`

---

## Running the Application

### Prerequisites
- Ensure Docker is installed on your system.
- Set up environment variables for the production database:
    - `URL_DB`: Database URL
    - `PORT_DB`: Database port
    - `NAME_DB`: Database name
    - `USER_DB`: Database username
    - `PASS_DB`: Database password
    - `PUB_KEY`: Path to the public key
    - `PRIV_KEY`: Path to the private key

### Steps to run
1. Clone the repository:
    ```bash
    git clone https://github.com/kswelder/Authenticator-Server
    cd Authenticator-Server
    ```

2. Run Maven:
    ```bash
    mvn spring-boot:run
    ```

3. Access the application:
    - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Steps to run with Docker
1. Clone the repository:
   ```bash
   git clone https://github.com/kswelder/Authenticator-Server
   cd Authenticator-Server
   ```
2. Build project
   ```bash
   mvn clean install
   ```

3. Make Docker image:
    - **Linux**:
      ```bash
      ./environments/build_and_dockerize.sh
      ```
    - **Windows**:
      ```cmd
      environments\build_and_dockerize.bat
      ```

4. Up Docker container:
    ```bash
    docker run -p 8080:8080 authentication-server-api:latest -d
     ```

5. Access the application:
    - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Documentation
- **Swagger**: [https://springdoc.org](https://springdoc.org)
- **Validation**: [Spring Boot Validation Starter](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/3.0.2)

---

## Planned Improvements
- Implement a dedicated token validation endpoint to delegate validation to another service.
- Enhance security with advanced OAuth2 features.

---

## License
This project is licensed under the [MIT License](LICENSE).

