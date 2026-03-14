package com.experiment.library.controller;

import com.experiment.library.model.Book;
import com.experiment.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("message",     "Welcome to the Spring MVC Library System!");
        response.put("description", "Use the endpoints below to explore the library.");
        response.put("endpoints",   "/welcome | /count | /price | /books | /books/{id} | /search | /author/{name} | /addbook | /viewbooks");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalBooks",     libraryService.getTotalCount());
        response.put("availableBooks", libraryService.getAvailableBooks().size());
        response.put("genres",         libraryService.getAllGenres().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price")
    public ResponseEntity<Map<String, Object>> getPriceStats() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("averagePrice",  String.format("$%.2f", libraryService.getAveragePrice()));
        response.put("highestPrice",  String.format("$%.2f", libraryService.getHighestPrice()));
        response.put("lowestPrice",   String.format("$%.2f", libraryService.getLowestPrice()));
        response.put("currency",      "USD");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getAllBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available) {

        List<Book> books;

        if (genre != null) {
            books = libraryService.getBooksByGenre(genre);
        } else if (Boolean.TRUE.equals(available)) {
            books = libraryService.getAvailableBooks();
        } else {
            books = libraryService.getAllBooks();
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("count", books.size());
        response.put("books", books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        return libraryService.getBookById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error",   "Book not found",
                                "message", "No book exists with ID: " + id
                        )));
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(
            @RequestParam(defaultValue = "") String keyword) {

        if (keyword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error",   "Missing parameter",
                    "message", "Please provide ?keyword=<search term>"
            ));
        }

        List<Book> results = libraryService.searchByTitle(keyword);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("keyword", keyword);
        response.put("count",   results.size());
        response.put("results", results);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{name}")
    public ResponseEntity<Map<String, Object>> getBooksByAuthor(
            @PathVariable String name) {

        List<Book> books = libraryService.getBooksByAuthor(name);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("author", name);
        response.put("count",  books.size());
        response.put("books",  books);

        if (books.isEmpty()) {
            response.put("message", "No books found for author: " + name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addbook")
    public ResponseEntity<Map<String, Object>> addBook(@Valid @RequestBody Book book) {
        Book saved = libraryService.addBook(book);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Book added successfully!");
        response.put("book",    saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/viewbooks")
    public ResponseEntity<Map<String, Object>> viewBooks() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("libraryName", "Spring MVC Library");
        response.put("stats",       libraryService.getLibraryStats());
        response.put("genres",      libraryService.getAllGenres());
        response.put("catalogue",   libraryService.getAllBooks());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/filter")
    public ResponseEntity<Map<String, Object>> filterByPrice(
            @RequestParam(defaultValue = "0")    Double minPrice,
            @RequestParam(defaultValue = "9999") Double maxPrice) {

        List<Book> books = libraryService.getBooksByPriceRange(minPrice, maxPrice);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("minPrice", "$" + minPrice);
        response.put("maxPrice", "$" + maxPrice);
        response.put("count",    books.size());
        response.put("books",    books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(libraryService.getLibraryStats());
    }
}
