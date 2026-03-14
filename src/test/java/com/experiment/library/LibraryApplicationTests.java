package com.experiment.library;

import com.experiment.library.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibraryApplicationTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test @Order(1)
    void welcome_returns200() throws Exception {
        mockMvc.perform(get("/library/welcome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Welcome")));
    }

    @Test @Order(2)
    void count_returns200WithTotal() throws Exception {
        mockMvc.perform(get("/library/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBooks").value(greaterThan(0)));
    }

    @Test @Order(3)
    void price_returnsStats() throws Exception {
        mockMvc.perform(get("/library/price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePrice").exists())
                .andExpect(jsonPath("$.highestPrice").exists())
                .andExpect(jsonPath("$.lowestPrice").exists());
    }

    @Test @Order(4)
    void books_returnsAllBooks() throws Exception {
        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(greaterThan(0))));
    }

    @Test @Order(5)
    void books_filterByGenre() throws Exception {
        mockMvc.perform(get("/library/books?genre=Fiction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].genre").value("Fiction"));
    }

    @Test @Order(6)
    void bookById_found_returns200() throws Exception {
        mockMvc.perform(get("/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test @Order(7)
    void bookById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/library/books/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book not found"));
    }

    @Test @Order(8)
    void search_found_returns200() throws Exception {
        mockMvc.perform(get("/library/search?keyword=code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keyword").value("code"))
                .andExpect(jsonPath("$.results", hasSize(greaterThan(0))));
    }

    @Test @Order(9)
    void search_noKeyword_returns400() throws Exception {
        mockMvc.perform(get("/library/search"))
                .andExpect(status().isBadRequest());
    }

    @Test @Order(10)
    void authorSearch_found() throws Exception {
        mockMvc.perform(get("/library/author/Robert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(greaterThan(0)));
    }

    @Test @Order(11)
    void authorSearch_notFound_returns404() throws Exception {
        mockMvc.perform(get("/library/author/XYZUnknownAuthor"))
                .andExpect(status().isNotFound());
    }

    @Test @Order(12)
    void addBook_valid_returns201() throws Exception {
        Book book = new Book(0, "Effective Java", "Joshua Bloch",
                "Technology", 45.99, 2018, true);
        mockMvc.perform(post("/library/addbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book added successfully!"))
                .andExpect(jsonPath("$.book.title").value("Effective Java"));
    }

    @Test @Order(13)
    void addBook_missingTitle_returns400() throws Exception {
        Book book = new Book();
        book.setAuthor("Someone"); book.setGenre("Tech"); book.setPrice(9.99); book.setYear(2020);
        mockMvc.perform(post("/library/addbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test @Order(14)
    void viewBooks_returnsCatalogue() throws Exception {
        mockMvc.perform(get("/library/viewbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libraryName").value("Spring MVC Library"))
                .andExpect(jsonPath("$.stats").exists())
                .andExpect(jsonPath("$.catalogue").isArray());
    }

    @Test @Order(15)
    void filterByPrice_returns200() throws Exception {
        mockMvc.perform(get("/library/books/filter?minPrice=10&maxPrice=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isArray());
    }
}
