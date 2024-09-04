package com.example.demolibrary.controllers;

import com.example.demolibrary.models.Book;
import com.example.demolibrary.models.BookStatus;
import com.example.demolibrary.services.BookService;
import com.example.demolibrary.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final LibraryService libraryService;

    @Autowired
    public BookController(BookService bookService, LibraryService libraryService) {
        this.bookService = bookService;
        this.libraryService = libraryService;
    }

    // 1. Получение списка всех книг
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    // 2. Получение определённой книги по её Id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    // 3. Получение книги по её ISBN
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    // 4. Добавление новой книги
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.addNewBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // 5. Изменение информации о существующей книге
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // 6. Удаление книги
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Выдача книги
    @PostMapping("/{id}/borrow")
    public ResponseEntity<BookStatus> borrowBook(@PathVariable Long id) {
        // Получаем книгу из базы
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Создаем новый статус книги
        BookStatus status = new BookStatus();
        status.setBook(book); // Устанавливаем книгу в статус
        status.setBorrowedAt(LocalDateTime.now());
        status.setReturnBy(LocalDateTime.now().plusDays(14));

        // Сохраняем статус книги
        BookStatus savedStatus = libraryService.addBookStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus);
    }

    // 8. Получение статуса книги
    @GetMapping("/{id}/status")
    public ResponseEntity<BookStatus> getBookStatus(@PathVariable Long id) {
        return libraryService.getBookStatus(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}