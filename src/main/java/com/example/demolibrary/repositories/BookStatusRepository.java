package com.example.demolibrary.repositories;

import com.example.demolibrary.models.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    Optional<BookStatus> findByBookId(Long bookId); // Метод для поиска статуса по bookId
}