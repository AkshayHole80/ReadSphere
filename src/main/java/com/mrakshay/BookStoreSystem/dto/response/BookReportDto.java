package com.mrakshay.BookStoreSystem.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BookReportDto {

    private Long totalBooks;
    private Integer totalInventory;
    private Double totalInventoryValue;
    private Double averageBookPrice;

    private String highestPricedBook;
    private String lowestPricedBook;

    private Map<String, Long> booksCategories;
    private Map<String, List<String>> authorAndTheirBooks;
    private Map<String, Double> averagePriceByCategory;
    private Map<String, List<String>> publisherAndTheirBooks;
}
