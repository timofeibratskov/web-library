package com.example.demolibrary.models;

import jakarta.persistence.OneToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
@Slf4j
@Data
@Entity
@Table(name = "book_status")
public class BookStatus {
    @Id
    private Long bookId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "book_id")
    private Book book;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnBy;
}
