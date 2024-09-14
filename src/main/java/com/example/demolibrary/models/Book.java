package com.example.demolibrary.models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name="books",uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String description;

}
