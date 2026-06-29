package com.mrakshay.BookStoreSystem.service.ServiceImpl;

import com.mrakshay.BookStoreSystem.dto.request.BookDto;
import com.mrakshay.BookStoreSystem.dto.request.BookPostDto;
import com.mrakshay.BookStoreSystem.dto.response.PageResponse;
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
import java.util.stream.Collectors;

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
    public PageResponse<BookDto> getBooks(int page, int size) {

        log.info("Fetching books with page {} and size {}", page, size);

        if (page < 0) {
            throw new InvalidBookDataException(
                    "Page number cannot be negative");
        }

        if (size <= 0) {
            throw new InvalidBookDataException(
                    "Page size must be greater than zero");
        }

        List<BookDto> books = getBooks();

        int totalElements = books.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int start = page * size;

        if (start >= totalElements) {
            return new PageResponse<>(
                    List.of(),
                    page,
                    size,
                    totalElements,
                    totalPages
            );
        }

        int end = Math.min(start + size, totalElements);

        return new PageResponse<>(
                books.subList(start, end),
                page,
                size,
                totalElements,
                totalPages
        );
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

    private void rewriteCsv(List<Book> books) {

        try (CSVWriter writer =
                     new CSVWriter(new FileWriter(csvFilePath))) {

            writer.writeNext(new String[]{
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
            });

            for (Book book : books) {

                writer.writeNext(new String[]{
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
                });
            }

        } catch (Exception e) {

            throw new CsvFileException(
                    "Error updating CSV file");
        }
    }

    @Override
    public BookDto updateBook(
            Long id,
            BookPostDto bookPostDto) {

        log.info("Updating book with id {}", id);

        List<Book> books = getBooks()
                .stream()
                .map(bookDto ->
                        modelMapper.map(bookDto, Book.class))
                .collect(Collectors.toList());

        Book existingBook = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new BookNotFoundException("Book Not Fount With Id :"+id));

        existingBook.setBookName(bookPostDto.getBookName());
        existingBook.setAuthorName(bookPostDto.getAuthorName());
        existingBook.setCategory(bookPostDto.getCategory());
        existingBook.setPublisher(bookPostDto.getPublisher());
        existingBook.setPrice(bookPostDto.getPrice());
        existingBook.setQuantity(bookPostDto.getQuantity());
        existingBook.setPublishedYear(bookPostDto.getPublishedYear());
        existingBook.setIsbn(bookPostDto.getIsbn());
        existingBook.setLanguage(bookPostDto.getLanguage());

        rewriteCsv(books);

        return modelMapper.map(
                existingBook,
                BookDto.class);
    }

    @Override
    public void deleteBook(Long id) {

        log.info("Deleting book with id {}", id);

        List<Book> books = getBooks()
                .stream()
                .map(bookDto ->
                        modelMapper.map(bookDto, Book.class))
                .collect(Collectors.toList());

        boolean removed =
                books.removeIf(book ->
                        book.getId().equals(id));

        if (!removed) {

            throw new BookNotFoundException(
                    "Book not found with id: " + id);
        }

        rewriteCsv(books);

        log.info("Book deleted successfully with id: {}",id);
    }

    @Override
    public List<BookDto> getBooksByCategory(String category) {

        log.info("Fetching books by category {}", category);

        return getBooks()
                .stream()
                .filter(book ->
                        book.getCategory()
                                .equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public List<BookDto> getBooksByAuthor(String author) {

        log.info("Fetching books by author {}", author);

        return getBooks()
                .stream()
                .filter(book ->
                        book.getAuthorName()
                                .equalsIgnoreCase(author))
                .toList();
    }

}