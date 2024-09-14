package com.example.demolibrary.services;

import com.example.demolibrary.dto.BookStatusDto;
import com.example.demolibrary.exceptions.ResourceNotFoundException;
import com.example.demolibrary.models.Book;
import com.example.demolibrary.models.BookStatus;
import com.example.demolibrary.repositories.BookRepository;
import com.example.demolibrary.repositories.BookStatusRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LibraryService {
    private final BookStatusRepository bookStatusRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LibraryService(BookStatusRepository bookStatusRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookStatusRepository = bookStatusRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public BookStatusDto borrowBook(Long bookId) {
        log.info("Attempting to borrow book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ID: " + bookId));

        if (bookStatusRepository.findByBookId(bookId).isPresent()) {
            throw new RuntimeException("Книга уже выдана.");
        }

        BookStatusDto statusDto = new BookStatusDto();
        statusDto.setBookId(book.getId());
        statusDto.setBorrowedAt(LocalDateTime.now());
        statusDto.setReturnBy(LocalDateTime.now().plusDays(14));

        BookStatus status = modelMapper.map(statusDto, BookStatus.class);
        bookStatusRepository.save(status);
        log.info("Book borrowed successfully with ID: {}", bookId);
        return modelMapper.map(status, BookStatusDto.class);
    }

    @Transactional
    public BookStatusDto returnBook(Long bookId) {
        log.info("Attempting to return book with ID: {}", bookId);
        BookStatus status = bookStatusRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Статус книги не найден с ID: " + bookId));

        bookStatusRepository.delete(status);
        log.info("Book returned successfully with ID: {}", bookId);
        return modelMapper.map(status, BookStatusDto.class);
    }

    @Transactional
    public BookStatusDto getBookStatus(Long bookId) {
        log.info("Fetching status for book ID: {}", bookId);
        BookStatus status = bookStatusRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Статус книги не найден с ID: " + bookId));
        log.info("Status found for book ID: {}", bookId);
        return modelMapper.map(status, BookStatusDto.class);
    }
}