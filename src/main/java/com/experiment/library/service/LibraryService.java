package com.experiment.library.service;

import com.experiment.library.model.Book;
import com.experiment.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LibraryService {

    private final BookRepository bookRepository;

    @Autowired
    public LibraryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public int getTotalCount() {
        return bookRepository.count();
    }

    public List<Book> searchByTitle(String keyword) {
        return bookRepository.searchByTitle(keyword);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> getBooksByPriceRange(double min, double max) {
        return bookRepository.findByPriceRange(min, max);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailable();
    }

    public double getAveragePrice() {
        return bookRepository.findAll().stream()
                .mapToDouble(Book::getPrice)
                .average()
                .orElse(0.0);
    }

    public double getHighestPrice() {
        return bookRepository.findAll().stream()
                .mapToDouble(Book::getPrice)
                .max()
                .orElse(0.0);
    }

    public double getLowestPrice() {
        return bookRepository.findAll().stream()
                .mapToDouble(Book::getPrice)
                .min()
                .orElse(0.0);
    }

    public List<String> getAllGenres() {
        return bookRepository.allGenres();
    }

    public Map<String, Object> getLibraryStats() {
        List<Book> all = bookRepository.findAll();
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalBooks",     all.size());
        stats.put("availableBooks", all.stream().filter(Book::isAvailable).count());
        stats.put("genres",         bookRepository.allGenres().size());
        stats.put("averagePrice",   String.format("$%.2f", getAveragePrice()));
        stats.put("highestPrice",   String.format("$%.2f", getHighestPrice()));
        stats.put("lowestPrice",    String.format("$%.2f", getLowestPrice()));
        return stats;
    }
}
