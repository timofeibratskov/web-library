package com.example.demolibrary.controllers;

import com.example.demolibrary.dto.BookDto;
import com.example.demolibrary.dto.BookStatusDto;
import com.example.demolibrary.services.BookService;
import com.example.demolibrary.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        BookDto book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.addNewBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<BookStatusDto> borrowBook(@PathVariable Long id) {
        BookStatusDto statusDto = libraryService.borrowBook(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(statusDto);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BookStatusDto> returnBook(@PathVariable Long id) {
        BookStatusDto statusDto = libraryService.returnBook(id);
        return ResponseEntity.ok(statusDto);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<BookStatusDto> getBookStatus(@PathVariable Long id) {
        BookStatusDto statusDto = libraryService.getBookStatus(id);
        return ResponseEntity.ok(statusDto);
    }
}