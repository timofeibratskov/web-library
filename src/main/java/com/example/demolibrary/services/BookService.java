package com.example.demolibrary.services;

import com.example.demolibrary.dto.BookDto;
import com.example.demolibrary.exceptions.ResourceNotFoundException;
import com.example.demolibrary.models.Book;
import com.example.demolibrary.repositories.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<BookDto> findAll() {
        log.info("Fetching all books");
        List<Book> books = bookRepository.findAll();
        log.info("Retrieved {} books", books.size());
        return books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public BookDto findById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ID: " + id));
        return modelMapper.map(book, BookDto.class);
    }

    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        log.info("Adding new book: {}", bookDto);

        // Проверка на уникальность ISBN
        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            throw new EntityExistsException("Книга с таким ISBN уже существует: " + bookDto.getIsbn());
        }

        Book book = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(book);
        log.info("Book added with ID: {}", savedBook.getId());
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        log.info("Updating book with ID: {}", id);

        // Проверка на существование книги
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга не найдена с ID: " + id);
        }

        // Проверка на уникальность ISBN
        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent() &&
                !bookRepository.findByIsbn(bookDto.getIsbn()).get().getId().equals(id)) {
            throw new EntityExistsException("Книга с таким ISBN уже существует: " + bookDto.getIsbn());
        }

        bookDto.setId(id);
        Book updatedBook = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(updatedBook);
        log.info("Book updated with ID: {}", savedBook.getId());
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга не найдена с ID: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book deleted with ID: {}", id);
    }

    @Transactional
    public BookDto findByIsbn(String isbn) {
        log.info("Fetching book with ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ISBN: " + isbn));
        return modelMapper.map(book, BookDto.class);
    }
}