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




}