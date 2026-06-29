# Book Store Management System

## Overview

Book Store Management System is a Spring Boot application that manages book inventory using a CSV file as the data source. The application provides REST APIs to perform CRUD operations on books, generate analytical reports, and includes role-based authentication.

---

## Features

### Book Management

- Get all books with pagination
- Get book by ID
- Filter books by category
- Filter books by author
- Add new books (ADMIN only)
- Update books (ADMIN only)
- Delete books (ADMIN only)
- CSV-based data persistence

### Security

- HTTP Basic Authentication
- Role-based access control (ADMIN, USER)
- In-memory user management
- Secured endpoints for write operations

### Reporting

- Generate consolidated report
- Category-wise book count
- Author-wise books report
- Publisher-wise books report
- Average price by category
- Inventory statistics
- TXT report generation (ADMIN only)

### API Documentation

- Swagger UI for interactive API testing
- OpenAPI 3.0 specification
- Detailed endpoint documentation

---

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Security
- Maven
- OpenCSV
- ModelMapper
- Lombok
- Springdoc OpenAPI
- Docker support

---

## Project Structure

```text
src/main/java
│
├── config
│   └── ModelMapperConfig
│
├── controller
│   ├── BookController
│   └── ReportController
│
├── dto
│   ├── request
│   │   ├── BookDto
│   │   └── BookPostDto
│   └── response
│       ├── BookReportDto
│       └── PageResponse
│
├── entity
│   └── Book
│
├── exception
│   ├── BookNotFoundException
│   ├── CsvFileException
│   ├── ErrorResponse
│   ├── GlobalExceptionHandler
│   └── InvalidBookDataException
│
├── security
│   └── SecurityConfig
│
├── service
│   ├── BookService
│   ├── ReportService
│   └── ServiceImpl
│       ├── BookServiceImpl
│       └── ReportServiceImpl
│
└── BookStoreSystemApplication
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.6+
- Docker (optional, for containerized deployment)

### Running Locally

```bash
# Clone the repository
git clone <repository-url>
cd BookStoreSystem

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on http://localhost:8000

### Running with Docker

**Build Docker image:**
```bash
docker build -t bookstore-system:latest .
```

**Run container:**
```bash
docker run -d \
  --name bookstore-app \
  -p 8000:8000 \
  -v $(pwd)/reports:/app/reports \
  bookstore-system:latest
```

**Using Docker Compose:**
```bash
docker-compose up -d
```

For detailed Docker instructions, see [DOCKER-README.md](DOCKER-README.md)

---

## Authentication

The application uses HTTP Basic Authentication with in-memory users:

**Admin User:**
- Username: `admin`
- Password: `admin123`
- Permissions: Full access (read, create, update, delete, generate reports)

**Regular User:**
- Username: `user`
- Password: `user123`
- Permissions: Read-only access to books

---

## API Endpoints

### Authentication
All write operations (POST, PUT, DELETE) require ADMIN authentication.
Read operations (GET) are accessible to both USER and ADMIN roles.

### Get All Books (Paginated)

```http
GET /api/books?page=0&size=5
```

### Get Book By Id

```http
GET /api/books/{id}
```

Example:

```http
GET /api/books/1
```

### Get Books By Category

```http
GET /api/books/category/{category}
```

Example:
```http
GET /api/books/category/Programming
```

### Get Books By Author

```http
GET /api/books/author/{author}
```

Example:
```http
GET /api/books/author/Craig%20Walls
```

### Add Book (ADMIN only)

```http
POST /api/books
Authorization: Basic admin:admin123
Content-Type: application/json
```

Request Body:

```json
{
  "bookName": "Spring Boot Guide",
  "authorName": "Craig Walls",
  "category": "Programming",
  "publisher": "Manning",
  "price": 799.99,
  "quantity": 10,
  "publishedYear": 2024,
  "isbn": "10021",
  "language": "English"
}
```

### Update Book (ADMIN only)

```http
PUT /api/books/{id}
Authorization: Basic admin:admin123
Content-Type: application/json
```

Request Body:
```json
{
  "bookName": "Updated Book Name",
  "authorName": "Author Name",
  "category": "Programming",
  "publisher": "Publisher",
  "price": 999.99,
  "quantity": 20,
  "publishedYear": 2024,
  "isbn": "123456789",
  "language": "English"
}
```

### Delete Book (ADMIN only)

```http
DELETE /api/books/{id}
Authorization: Basic admin:admin123
```

### Generate Report (ADMIN only)

```http
GET /api/reports
Authorization: Basic admin:admin123
```

This API:
- Returns report data as JSON
- Generates a TXT report file in the reports directory

---

## API Documentation

Access the interactive Swagger UI documentation:

```
http://localhost:8000/swagger-ui.html
```

OpenAPI JSON specification:

```
http://localhost:8000/v3/api-docs
```

---

## Health Check

```
http://localhost:8000/actuator/health
```

---

## Report Contents

### Summary

- Total Books
- Total Inventory
- Total Inventory Value
- Average Book Price
- Highest Priced Book
- Lowest Priced Book

### Analytics

- Category Wise Books
- Author and Their Books
- Average Price By Category
- Publisher and Their Books

---

## Sample Report Output

```text
=================================================
BOOK STORE REPORT
=================================================

Total Books : 21
Total Inventory : 296
Total Inventory Value : 245125.0
Average Book Price : 894.95

Highest Priced Book : Introduction to Algorithms
Lowest Priced Book : Head First Java

=================================================
BOOK CATEGORIES
=================================================

Programming : 7
Architecture : 3
DevOps : 3
Algorithms : 2

=================================================
AUTHOR AND THEIR BOOKS
=================================================

Craig Walls
   - Spring in Action
   - Spring Boot Guide

Joshua Bloch
   - Effective Java
```

---

## Dependencies

Main dependencies used in the project:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>

<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.2.5</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## Configuration

Application configuration in `application.properties`:

```properties
# Server Configuration
server.port=8000

# CSV File Configuration
csv.file.path=src/main/resources/books_catalog.csv
report.file.path=book-report.txt

# Actuator Configuration
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=always
```

---

## Testing

Run tests using Maven:

```bash
mvn test
```

---

## Future Enhancements

- JWT authentication implementation
- PostgreSQL database integration
- Excel report generation
- PDF report generation
- Advanced search and filtering
- Book cover image support
- REST API versioning
- Comprehensive integration tests
- Performance optimization with caching

---


