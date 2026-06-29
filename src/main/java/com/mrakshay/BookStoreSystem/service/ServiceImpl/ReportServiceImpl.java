package com.mrakshay.BookStoreSystem.service.ServiceImpl;

import com.mrakshay.BookStoreSystem.dto.request.BookDto;
import com.mrakshay.BookStoreSystem.dto.response.BookReportDto;
import com.mrakshay.BookStoreSystem.exception.CsvFileException;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.mrakshay.BookStoreSystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final BookService bookService;

    @Value("${report.file.path}")
    private String reportFilePath;

    @Cacheable("report")
    @Override
    public BookReportDto generateReport() {

        log.info("Generating book report");

        List<BookDto> books = bookService.getBooks();

        BookReportDto report = new BookReportDto();

        report.setTotalBooks((long) books.size());

        report.setTotalInventory(
                books.stream()
                        .mapToInt(BookDto::getQuantity)
                        .sum()
        );

        report.setTotalInventoryValue(
                books.stream()
                        .mapToDouble(book ->
                                book.getPrice() * book.getQuantity())
                        .sum()
        );

        report.setAverageBookPrice(
                books.stream()
                        .mapToDouble(BookDto::getPrice)
                        .average()
                        .orElse(0.0)
        );

        report.setHighestPricedBook(
                books.stream()
                        .max(Comparator.comparing(BookDto::getPrice))
                        .map(BookDto::getBookName)
                        .orElse(null)
        );

        report.setLowestPricedBook(
                books.stream()
                        .min(Comparator.comparing(BookDto::getPrice))
                        .map(BookDto::getBookName)
                        .orElse(null)
        );

        report.setBooksCategories(
                books.stream()
                        .collect(Collectors.groupingBy(
                                BookDto::getCategory,
                                Collectors.counting()))
        );

        report.setAuthorAndTheirBooks(
                books.stream()
                        .collect(Collectors.groupingBy(
                                BookDto::getAuthorName,
                                Collectors.mapping(
                                        BookDto::getBookName,
                                        Collectors.toList())))
        );

        report.setAveragePriceByCategory(
                books.stream()
                        .collect(Collectors.groupingBy(
                                BookDto::getCategory,
                                Collectors.averagingDouble(
                                        BookDto::getPrice)))
        );

        report.setPublisherAndTheirBooks(
                books.stream()
                        .collect(Collectors.groupingBy(
                                BookDto::getPublisher,
                                Collectors.mapping(
                                        BookDto::getBookName,
                                        Collectors.toList())))
        );

        generateTxtReport(report);

        log.info("Book report generated successfully");

        return report;
    }

    private void generateTxtReport(BookReportDto report) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(reportFilePath))) {

            writer.write("=================================================");
            writer.newLine();
            writer.write("BOOK STORE REPORT");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();

            writer.write("Total Books : " + report.getTotalBooks());
            writer.newLine();

            writer.write("Total Inventory : " + report.getTotalInventory());
            writer.newLine();

            writer.write("Total Inventory Value : " + report.getTotalInventoryValue());
            writer.newLine();

            writer.write("Average Book Price : " +
                    String.format("%.2f", report.getAverageBookPrice()));
            writer.newLine();

            writer.write("Highest Priced Book : " + report.getHighestPricedBook());
            writer.newLine();

            writer.write("Lowest Priced Book : " + report.getLowestPricedBook());
            writer.newLine();
            writer.newLine();

            writer.write("=================================================");
            writer.newLine();
            writer.write("BOOK CATEGORIES");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();

            for (var entry : report.getBooksCategories().entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.newLine();
            }

            writer.newLine();

            writer.write("=================================================");
            writer.newLine();
            writer.write("AVERAGE PRICE BY CATEGORY");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();

            for (var entry : report.getAveragePriceByCategory().entrySet()) {
                writer.write(entry.getKey() + " : ₹" +
                        String.format("%.2f", entry.getValue()));
                writer.newLine();
            }

            writer.newLine();

            writer.write("=================================================");
            writer.newLine();
            writer.write("AUTHOR AND THEIR BOOKS");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();

            for (var entry : report.getAuthorAndTheirBooks().entrySet()) {

                writer.write(entry.getKey());
                writer.newLine();

                for (String book : entry.getValue()) {
                    writer.write("   - " + book);
                    writer.newLine();
                }

                writer.newLine();
            }

            writer.write("=================================================");
            writer.newLine();
            writer.write("PUBLISHER AND THEIR BOOKS");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();

            for (var entry : report.getPublisherAndTheirBooks().entrySet()) {

                writer.write(entry.getKey());
                writer.newLine();

                for (String book : entry.getValue()) {
                    writer.write("   - " + book);
                    writer.newLine();
                }

                writer.newLine();
            }

            writer.write("=================================================");
            writer.newLine();
            writer.write("END OF REPORT");
            writer.newLine();
            writer.write("=================================================");

            log.info("TXT report generated successfully at {}", reportFilePath);

        } catch (Exception e) {

            log.error("Error generating TXT report", e);

            throw new CsvFileException(
                    "Error generating TXT report");
        }
    }
}