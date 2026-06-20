package com.mrakshay.BookStoreSystem.service.ServiceImpl;

import com.mrakshay.BookStoreSystem.dto.BookDto;
import com.mrakshay.BookStoreSystem.dto.BookPostDto;
import com.mrakshay.BookStoreSystem.entity.Book;
import com.mrakshay.BookStoreSystem.exception.BookNotFoundException;
import com.mrakshay.BookStoreSystem.exception.CsvFileException;
import com.mrakshay.BookStoreSystem.exception.InvalidBookDataException;
import com.mrakshay.BookStoreSystem.service.BookService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;

    @Value("${csv.file.path}")
    private String csvFilePath;

    @Override
    public List<BookDto> getBooks() {

        log.info("Fetching all books");

        List<BookDto> bookDtos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(
                new FileReader(csvFilePath))) {

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

            log.info("Successfully fetched {} books", bookDtos.size());

            return bookDtos;

        } catch (Exception e) {

            log.error("Error while reading CSV file", e);

            throw new CsvFileException(
                    "Error reading CSV file");
        }
    }

    @Override
    public BookDto getBookById(Long id) {

        log.info("Fetching book with id {}", id);

        return getBooks()
                .stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Book not found with id {}", id);
                    return new BookNotFoundException(
                            "Book not found with id: " + id);
                });
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

            log.info("Book successfully written to CSV with id {}",
                    book.getId());

        } catch (Exception e) {

            log.error("Error while writing CSV file", e);

            throw new CsvFileException(
                    "Error writing CSV file");
        }
    }

    @Override
    public BookDto addBook(BookPostDto bookPostDto) {

        log.info("Adding new book {}",
                bookPostDto.getBookName());

        if (bookPostDto.getPrice() <= 0) {
            throw new InvalidBookDataException(
                    "Price must be greater than zero");
        }

        if (bookPostDto.getQuantity() < 0) {
            throw new InvalidBookDataException(
                    "Quantity cannot be negative");
        }

        if (bookPostDto.getBookName() == null
                || bookPostDto.getBookName().isBlank()) {

            throw new InvalidBookDataException(
                    "Book name cannot be empty");
        }

        Book book = modelMapper.map(
                bookPostDto,
                Book.class);

        Long nextId = getBooks()
                .stream()
                .map(BookDto::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;

        book.setId(nextId);

        writeBookToCsv(book);

        log.info("Book added successfully with id {}",
                nextId);

        return modelMapper.map(
                book,
                BookDto.class);
    }
}