package com.mrakshay.BookStoreSystem.dto;

import lombok.Data;

@Data
public class BookPostDto {

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