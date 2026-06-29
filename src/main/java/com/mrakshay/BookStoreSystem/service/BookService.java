package com.mrakshay.BookStoreSystem.service;

import com.mrakshay.BookStoreSystem.dto.request.BookDto;
import com.mrakshay.BookStoreSystem.dto.request.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.response.PageResponse;

import java.util.List;

public interface BookService {
    List<BookDto> getBooks();
    PageResponse<BookDto> getBooks(int page, int size);
    BookDto getBookById(Long id);
    BookDto addBook(BookPostDto bookPostDto);
    BookDto updateBook(Long id, BookPostDto bookPostDto);

    void deleteBook(Long id);
    List<BookDto> getBooksByCategory(String category);

    List<BookDto> getBooksByAuthor(String author);
  }
