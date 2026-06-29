package com.mrakshay.BookStoreSystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class BookPostDto {

    @NotBlank(message = "Book name is required")
    @UniqueElements(message = "Book name must be unique")
    private String bookName;

    @NotBlank(message = "Author name is required")
    private String authorName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Published year is required")
    private Integer publishedYear;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Language is required")
    private String language;
}