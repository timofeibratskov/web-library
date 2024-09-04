package com.example.demolibrary.services;

import com.example.demolibrary.exceptions.ResourceNotFoundException;
import com.example.demolibrary.models.Book;
import com.example.demolibrary.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Получение списка всех книг
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // Получение книги по ID
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ID: " + id));
    }

    // Добавление новой книги
    public Book addNewBook(Book book) {
        return bookRepository.save(book);
    }

    // Обновление информации о книге
    public Book updateBook(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга не найдена с ID: " + id);
        }
        book.setId(id); // Устанавливаем ID у обновляемой книги
        return bookRepository.save(book);
    }

    // Удаление книги
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга не найдена с ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    // Получение книги по ISBN
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с ISBN: " + isbn));
    }
}