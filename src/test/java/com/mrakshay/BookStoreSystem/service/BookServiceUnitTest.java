package com.mrakshay.BookStoreSystem.service;

import com.mrakshay.BookStoreSystem.exception.BookNotFoundException;
import com.mrakshay.BookStoreSystem.exception.InvalidBookDataException;
import com.mrakshay.BookStoreSystem.service.ServiceImpl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldThrowBookNotFoundException() {

        assertThrows(
                BookNotFoundException.class,
                () -> {
                    throw new BookNotFoundException(
                            "Book not found with id: 999");
                }
        );
    }

    @Test
    void shouldThrowInvalidBookDataException() {

        assertThrows(
                InvalidBookDataException.class,
                () -> {
                    throw new InvalidBookDataException(
                            "Price must be greater than zero");
                }
        );
    }
}
