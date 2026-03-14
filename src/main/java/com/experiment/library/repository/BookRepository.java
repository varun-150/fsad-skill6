package com.experiment.library.repository;

import com.experiment.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    public BookRepository() {
        save(new Book(nextId++, "Clean Code",                   "Robert C. Martin", "Technology", 39.99, 2008, true));
        save(new Book(nextId++, "The Pragmatic Programmer",     "Andrew Hunt",       "Technology", 44.99, 2019, true));
        save(new Book(nextId++, "Design Patterns",              "Gang of Four",      "Technology", 54.99, 1994, false));
        save(new Book(nextId++, "To Kill a Mockingbird",        "Harper Lee",        "Fiction",    12.99, 1960, true));
        save(new Book(nextId++, "1984",                         "George Orwell",     "Fiction",    10.99, 1949, true));
        save(new Book(nextId++, "Brave New World",              "Aldous Huxley",     "Fiction",    11.99, 1932, true));
        save(new Book(nextId++, "A Brief History of Time",      "Stephen Hawking",   "Science",    18.99, 1988, true));
        save(new Book(nextId++, "Sapiens",                      "Yuval Noah Harari", "History",    16.99, 2011, true));
        save(new Book(nextId++, "The Great Gatsby",             "F. Scott Fitzgerald","Fiction",    9.99, 1925, false));
        save(new Book(nextId++, "Refactoring",                  "Robert C. Martin",  "Technology", 49.99, 2018, true));
    }

    public Book save(Book book) {
        if (book.getId() == 0) {
            book.setId(nextId++);
        }
        books.add(book);
        return book;
    }

    public List<Book> findAll() {
        return Collections.unmodifiableList(books);
    }

    public Optional<Book> findById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst();
    }

    public List<Book> searchByTitle(String keyword) {
        String kw = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String name) {
        String n = name.toLowerCase();
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(n))
                .collect(Collectors.toList());
    }

    public List<Book> findByGenre(String genre) {
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public int count() {
        return books.size();
    }

    public List<String> allGenres() {
        return books.stream()
                .map(Book::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Book> findByPriceRange(double min, double max) {
        return books.stream()
                .filter(b -> b.getPrice() >= min && b.getPrice() <= max)
                .sorted(Comparator.comparingDouble(Book::getPrice))
                .collect(Collectors.toList());
    }

    public List<Book> findAvailable() {
        return books.stream().filter(Book::isAvailable).collect(Collectors.toList());
    }
}
