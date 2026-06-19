package com.mrakshay.BookStoreSystem.service.ServiceImpl;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.BookReportDto;
import com.mrakshay.BookStoreSystem.entity.Book;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;
    @Value("${csv.file.path}")
    private String csvFilePath;


  @Override
    public List<BookDto> getBooks() {

        List<BookDto> bookDtos = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(
                    new java.io.FileReader(csvFilePath));

            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) {

                String[] row = rows.get(i);

                Book book = new Book();

                book.setId(Long.parseLong(row[0]));
                book.setBookName(row[1]);
                book.setAuthorName(row[2]);
                book.setCategory(row[3]);
                book.setPublisher(row[4]);
                book.setPrice(Double.parseDouble(row[5]));
                book.setQuantity(Integer.parseInt(row[6]));
                book.setPublishedYear(Integer.parseInt(row[7]));
                book.setIsbn(row[8]);
                book.setLanguage(row[9]);

                bookDtos.add(modelMapper.map(book, BookDto.class));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file", e);
        }

        return bookDtos;
    }

    @Override
    public BookDto getBookById(Long id) {
        return getBooks().stream()
                .filter(bookDto -> bookDto.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    private void writeBookToCsv(Book book) {

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(csvFilePath, true))) {

            String[] row = {
                    String.valueOf(book.getId()),
                    book.getBookName(),
                    book.getAuthorName(),
                    book.getCategory(),
                    book.getPublisher(),
                    String.valueOf(book.getPrice()),
                    String.valueOf(book.getQuantity()),
                    String.valueOf(book.getPublishedYear()),
                    book.getIsbn(),
                    book.getLanguage()
            };

            writer.writeNext(row);

        } catch (Exception e) {
            throw new RuntimeException("Error writing to CSV", e);
        }
    }
    @Override
    public BookDto addBook(BookPostDto bookPostDto) {

        Book book = modelMapper.map(bookPostDto, Book.class);

        Long nextId = getBooks()
                .stream()
                .map(BookDto::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;

        book.setId(nextId);

        writeBookToCsv(book);

        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public BookReportDto generateReport() {

        List<BookDto> books = getBooks();

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
        return report;
    }

    private void generateTxtReport(BookReportDto report) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("book-report.txt"))) {

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

            // Category Report
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

            // Average Price By Category
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

            // Author Report
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

            // Publisher Report
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

        } catch (Exception e) {
            throw new RuntimeException("Error generating TXT report", e);
        }
    }


}