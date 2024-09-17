package com.example.demolibrary.services;

import com.example.demolibrary.dto.BookStatusDto;
import com.example.demolibrary.exceptions.BookNotReturnedException;
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
import org.springframework.web.context.request.NativeWebRequest;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LibraryService {
    private final BookStatusRepository bookStatusRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final NativeWebRequest nativeWebRequest;

    @Autowired
    public LibraryService(BookStatusRepository bookStatusRepository, BookRepository bookRepository, ModelMapper modelMapper, NativeWebRequest nativeWebRequest) {
        this.bookStatusRepository = bookStatusRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.nativeWebRequest = nativeWebRequest;
    }

    @Transactional
    public BookStatusDto borrowBook(Long bookId) {
        log.info("Attempting to borrow book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ID: " + bookId));



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

        status.setBorrowedAt(null);
        status.setReturnBy(null);

        log.info("Book returned successfully with ID: {}", bookId);
        return modelMapper.map(status, BookStatusDto.class);
    }

    @Transactional
    public BookStatusDto getBookStatus(Long bookId) {
        log.info("Fetching status for book ID: {}", bookId);
        BookStatus status = bookStatusRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Статус книги не найден с ID: " + bookId));
        log.info("Status found for book ID: {}", bookId);

        BookStatusDto bookStatusDto = modelMapper.map(status, BookStatusDto.class);

        // Установка сообщения о возможности взять книгу
        if (status.getBorrowedAt() == null) {
            log.info("you can take this book with id:{}", bookId);
            return bookStatusDto; // Сообщение о том, что книга доступна
        }else {
            throw new BookNotReturnedException("Книги нет в наличии");
        }

    }
}