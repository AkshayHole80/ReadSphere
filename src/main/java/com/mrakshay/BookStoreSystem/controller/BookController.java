package com.mrakshay.BookStoreSystem.controller;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.BookReportDto;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.mrakshay.BookStoreSystem.service.ServiceImpl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book APIs", description = "Operations related to books")
public class BookController {

    private final BookService bookService;

   @Operation(summary = "Get all books")
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getBooks();
    }

    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Add a new book")
    @PostMapping
    public BookDto addBook(@RequestBody BookPostDto bookPostDto) {
        return bookService.addBook(bookPostDto);
    }


    @Operation(summary = "Get books by category")
    @GetMapping("/category/{category}")
    public List<BookDto> getBooksByCategory(
            @PathVariable String category) {

        return bookService
                .getBooksByCategory(category);
    }

    @Operation(summary = "Get books by author")
    @GetMapping("/author/{author}")
    public List<BookDto> getBooksByAuthor(
            @PathVariable String author) {

        return bookService
                .getBooksByAuthor(author);
    }

    @Operation(summary = "Update a book by id")
    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody BookPostDto bookPostDto) {

        return bookService.updateBook(id, bookPostDto);
    }

    @Operation(summary = "Delete a book by id")
    @DeleteMapping("/{id}")
    public String deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return "Book deleted successfully";
    }


}