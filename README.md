# Experiment 6 – Spring MVC Web Request Handling Demo

## Overview
A Spring Boot **Library System** demonstrating all major Spring MVC request-handling annotations across 11 REST endpoints.

---

## Project Structure
```
src/main/java/com/experiment/library/
├── LibraryApplication.java          # Entry point
├── model/
│   └── Book.java                    # Book model with Bean Validation
├── repository/
│   └── BookRepository.java          # In-memory store (10 sample books)
├── service/
│   └── LibraryService.java          # Business logic
└── controller/
    └── LibraryController.java       # All REST endpoints
```

---

## Run
```bash
mvn spring-boot:run
# Server: http://localhost:8080
```

---

## Annotation Reference

| Annotation | Purpose | Used In |
|---|---|---|
| `@RestController` | Marks class as REST controller (returns JSON) | LibraryController |
| `@RequestMapping` | Base URL prefix `/library` | LibraryController |
| `@GetMapping` | Handles HTTP GET | All GET endpoints |
| `@PostMapping` | Handles HTTP POST | `/addbook` |
| `@PathVariable` | Binds `{segment}` from URL path | `/books/{id}`, `/author/{name}` |
| `@RequestParam` | Binds `?key=value` query parameters | `/search`, `/books`, `/filter` |
| `@RequestBody` | Deserialises JSON body to Java object | `/addbook` |
| `@Valid` | Triggers Bean Validation on request body | `/addbook` |
| `ResponseEntity<T>` | Wraps response with HTTP status code | All endpoints |

---

## Postman Test Guide

Base URL: `http://localhost:8080/library`

---

### Task 2 – GET /welcome
```
GET http://localhost:8080/library/welcome
```
**200 OK**
```json
{
  "message": "Welcome to the Spring MVC Library System!",
  "description": "Use the endpoints below to explore the library.",
  "endpoints": "/welcome | /count | /price | /books | ..."
}
```

---

### Task 3 – GET /count
```
GET http://localhost:8080/library/count
```
**200 OK**
```json
{ "totalBooks": 10, "availableBooks": 8, "genres": 4 }
```

---

### Task 4 – GET /price
```
GET http://localhost:8080/library/price
```
**200 OK**
```json
{ "averagePrice": "$32.98", "highestPrice": "$54.99", "lowestPrice": "$9.99", "currency": "USD" }
```

---

### Task 5 – GET /books
```
GET http://localhost:8080/library/books
GET http://localhost:8080/library/books?genre=Fiction
GET http://localhost:8080/library/books?available=true
```
**200 OK** – list of books

---

### Task 6 – GET /books/{id}  → @PathVariable
```
GET http://localhost:8080/library/books/1     → 200 OK
GET http://localhost:8080/library/books/99    → 404 Not Found
```

---

### Task 7 – GET /search  → @RequestParam
```
GET http://localhost:8080/library/search?keyword=code
GET http://localhost:8080/library/search?keyword=the
GET http://localhost:8080/library/search            → 400 Bad Request
```
**200 OK**
```json
{ "keyword": "code", "count": 2, "results": [ ... ] }
```

---

### Task 8 – GET /author/{name}  → @PathVariable
```
GET http://localhost:8080/library/author/Robert
GET http://localhost:8080/library/author/Harper Lee
GET http://localhost:8080/library/author/XYZ    → 404 Not Found
```

---

### Task 9 – POST /addbook  → @PostMapping + @RequestBody
```
POST http://localhost:8080/library/addbook
Content-Type: application/json

{
  "title":     "Effective Java",
  "author":    "Joshua Bloch",
  "genre":     "Technology",
  "price":     45.99,
  "year":      2018,
  "available": true
}
```
**201 Created**
```json
{ "message": "Book added successfully!", "book": { ... } }
```

**Validation errors (400 Bad Request):**
- Missing title → `"Title is required"`
- Price ≤ 0    → `"Price must be greater than 0"`
- Year < 1000  → `"Year must be a valid 4-digit year"`

---

### Task 10 – GET /viewbooks
```
GET http://localhost:8080/library/viewbooks
```
**200 OK**
```json
{
  "libraryName": "Spring MVC Library",
  "stats": { "totalBooks": 10, "availableBooks": 8, ... },
  "genres": ["Fiction", "History", "Science", "Technology"],
  "catalogue": [ ... all books ... ]
}
```

---

### Extra – GET /books/filter (multiple @RequestParam)
```
GET http://localhost:8080/library/books/filter?minPrice=10&maxPrice=30
```

### Extra – GET /stats
```
GET http://localhost:8080/library/stats
```

---

## GitHub
```bash
git init
git add .
git commit -m "Experiment 6: Spring MVC Web Request Handling Demo"
git remote add origin <your-repo-url>
git push -u origin main
```
