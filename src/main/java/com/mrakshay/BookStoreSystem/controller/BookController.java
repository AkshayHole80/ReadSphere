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

}