package com.example.demolibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStatusDto {
    private Long bookId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnBy;
}