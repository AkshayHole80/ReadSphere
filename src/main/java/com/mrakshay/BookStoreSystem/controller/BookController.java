package com.mrakshay.BookStoreSystem.controller;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.BookReportDto;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.mrakshay.BookStoreSystem.service.ServiceImpl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public  ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @Operation(summary = "Get book by id")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return  ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Add a new book")
    @PostMapping
    public ResponseEntity<BookDto> addBook(
           @Valid @RequestBody BookPostDto bookPostDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.addBook(bookPostDto));
    }


    @Operation(summary = "Get books by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookDto>> getBooksByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(bookService
                .getBooksByCategory(category));
    }

    @Operation(summary = "Get books by author")
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(
            @PathVariable String author) {

        return ResponseEntity.ok(
                bookService.getBooksByAuthor(author));
    }

    @Operation(summary = "Update a book by id")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
          @Valid  @RequestBody BookPostDto bookPostDto) {

        return ResponseEntity.ok(
                bookService.updateBook(id, bookPostDto));
    }

    @Operation(summary = "Delete a book by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }


}