package com.experiment.library.model;

import jakarta.validation.constraints.*;

public class Book {

    private int id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
    private String title;

    @NotBlank(message = "Author name is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @Min(value = 1000, message = "Year must be a valid 4-digit year")
    @Max(value = 2100, message = "Year must be a valid 4-digit year")
    private int year;

    private boolean available;

    public Book() {}

    public Book(int id, String title, String author, String genre,
                Double price, int year, boolean available) {
        this.id        = id;
        this.title     = title;
        this.author    = author;
        this.genre     = genre;
        this.price     = price;
        this.year      = year;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author +
               "', genre='" + genre + "', price=" + price + ", year=" + year + "}";
    }
}
