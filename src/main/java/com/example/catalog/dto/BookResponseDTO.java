package com.example.catalog.dto;

import com.example.catalog.domain.Book;

public record BookResponseDTO(Long id, String isbn, String title, String author, int year) {

    public static BookResponseDTO fromDomain(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear()
        );
    }
}