package com.mrakshay.BookStoreSystem.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
@JsonPropertyOrder({
        "id",
        "bookName",
        "authorName",
        "category",
        "publisher",
        "price",
        "quantity",
        "publishedYear",
        "isbn",
        "language"
})
@Data
public class BookDto {

    private Long id;
    private String bookName;
    private String authorName;
    private String category;
    private String publisher;
    private Double price;
    private Integer quantity;
    private Integer publishedYear;
    private String isbn;
    private String language;
}