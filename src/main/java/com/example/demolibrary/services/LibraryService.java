package com.example.demolibrary.services;

import com.example.demolibrary.models.BookStatus;
import com.example.demolibrary.repositories.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {

    private final BookStatusRepository bookStatusRepository;

    @Autowired
    public LibraryService(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    public BookStatus addBookStatus(BookStatus 
        return bookStatusRepository.save(status);
    }

    public Optional<BookStatus> getBookStatus(Long bookId) {
        return bookStatusRepository.findByBookId(bookId); 
    }
}
