package com.example.demolibrary.controllers;

import com.example.demolibrary.dto.BookDto;
import com.example.demolibrary.dto.BookStatusDto;
import com.example.demolibrary.services.BookService;
import com.example.demolibrary.services.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }
    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга найдена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@Parameter(description = "ID книги") @PathVariable Long id) {
        BookDto book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по указанному ISBN")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@Parameter(description = "ISBN книги") @PathVariable String isbn) {
        BookDto book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Добавить новую книгу", description = "Создает новую книгу")
    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.addNewBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Обновить книгу", description = "Обновляет информацию о книге по указанному ID")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@Parameter(description = "ID книги") @PathVariable Long id,
                                              @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@Parameter(description = "ID книги") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "забрать книгу", description = "Позволяет пользователю взять книгу")
    @PostMapping("/{id}/borrow")
    public ResponseEntity<BookStatusDto> borrowBook(@Parameter(description = "ID книги") @PathVariable Long id) {
        BookStatusDto statusDto = libraryService.borrowBook(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(statusDto);
    }

    @Operation(summary = "Вернуть книгу", description = "Позволяет пользователю вернуть книгу")
    @PostMapping("/{id}/return")
    public ResponseEntity<BookStatusDto> returnBook(@Parameter(description = "ID книги") @PathVariable Long id) {
        BookStatusDto statusDto = libraryService.returnBook(id);
        return ResponseEntity.ok(statusDto);
    }

    @Operation(summary = "Получить статус книги", description = "Возвращает статус книги по указанному ID")
    @GetMapping("/{id}/status")
    public ResponseEntity<BookStatusDto> getBookStatus(@Parameter(description = "ID книги") @PathVariable Long id) {
        BookStatusDto statusDto = libraryService.getBookStatus(id);
        return ResponseEntity.ok(statusDto);
    }
}