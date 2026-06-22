package com.mrakshay.BookStoreSystem.service;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Test
    void testGetBookById() {

        BookDto book = bookService.getBookById(1L);

        assertNotNull(book);
        assertEquals(1L, book.getId());
    }

    @Test
    void testGetAllBooks() {

        assertFalse(
                bookService.getBooks().isEmpty()
        );
    }

    @Test
    void testBookNotFound() {

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookById(999L)
        );
    }
}