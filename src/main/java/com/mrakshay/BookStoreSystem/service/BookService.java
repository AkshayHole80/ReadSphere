package com.mrakshay.BookStoreSystem.service;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.BookReportDto;

import java.util.List;

public interface BookService {
    List<BookDto> getBooks();
    BookDto getBookById(Long id);
    BookDto addBook(BookPostDto bookPostDto);
    BookDto updateBook(Long id, BookPostDto bookPostDto);

    void deleteBook(Long id);
    List<BookDto> getBooksByCategory(String category);

    List<BookDto> getBooksByAuthor(String author);
  }
