package com.mrakshay.BookStoreSystem.controller;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.BookReportDto;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.mrakshay.BookStoreSystem.service.ServiceImpl.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
    @PostMapping
    public BookDto addBook(@RequestBody BookPostDto bookPostDto) {
        return bookService.addBook(bookPostDto);
    }


    @GetMapping("/category/{category}")
    public List<BookDto> getBooksByCategory(
            @PathVariable String category) {

        return bookService
                .getBooksByCategory(category);
    }

    @GetMapping("/author/{author}")
    public List<BookDto> getBooksByAuthor(
            @PathVariable String author) {

        return bookService
                .getBooksByAuthor(author);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody BookPostDto bookPostDto) {

        return bookService.updateBook(id, bookPostDto);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return "Book deleted successfully";
    }


}