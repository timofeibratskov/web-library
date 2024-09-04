package com.example.demolibrary.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "book_status")
public class BookStatus {
    @Id
    @Column(name = "book_id") // Указываем имя столбца
    private Long bookId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "book_id") // Указываем имя столбца для связи
    private Book book;

    @Column(name = "borrowed_at") // Указываем имя столбца
    private LocalDateTime borrowedAt;

    @Column(name = "return_by") // Указываем имя столбца
    private LocalDateTime returnBy;
}
