package com.example.catalog.dto;

import com.example.catalog.domain.Book;

public record BookResponse(Long id, String isbn, String title, String author, int year) {

    public static BookResponse fromDomain(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear()
        );
    }
}