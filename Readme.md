# Book Store Management System

## Overview

Book Store Management System is a Spring Boot application that manages book inventory using a CSV file as the data source.

The application provides REST APIs to perform book operations, generate analytical reports, and export reports to a text file.

---

## Features

### Book Management

- Get all books
- Get book by ID
- Add new books
- CSV-based data persistence

### Reporting

- Generate consolidated report
- Category-wise book count
- Author-wise books report
- Publisher-wise books report
- Average price by category
- Inventory statistics
- TXT report generation

---

## Tech Stack

- Java 21
- Spring Boot
- Maven
- OpenCSV
- ModelMapper
- Lombok

---

## Project Structure

```text
src/main/java
│
├── config
│   └── ModelMapperConfig
│
├── controller
│   └── BookController
│
├── dto
│   ├── BookDto
│   ├── BookPostDto
│   └── BookReportDto
│
├── entity
│   └── Book
│
├── service
│   ├── BookService
│   └── ServiceImpl
│       └── BookServiceImpl
│
└── BookStoreSystemApplication
```

---

## API Endpoints

### Get All Books

```http
GET /api/books
```

### Get Book By Id

```http
GET /api/books/{id}
```

Example:

```http
GET /api/books/1
```

### Add Book

```http
POST /api/books
```

Request Body

```json
{
  "bookName": "Spring Boot Guide",
  "authorName": "Craig Walls",
  "category": "Programming",
  "publisher": "Manning",
  "price": 799,
  "quantity": 10,
  "publishedYear": 2024,
  "isbn": "10021",
  "language": "English"
}
```

### Generate Report

```http
GET /api/books/report
```

This API:
- Returns report data as JSON
- Generates a TXT report file

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

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
</dependency>

<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

---

## Future Enhancements

- Update Book API
- Delete Book API
- Excel Report Generation
- PDF Report Generation
- MySQL Integration
- Swagger Documentation
- Unit Testing

---


Spring Boot Domain Training Project - P99Soft