package com.example.demolibrary.repositories;

import com.example.demolibrary.models.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    Optional<BookStatus> findByBookId(Long bookId); // Метод для поиска статуса по bookId
}