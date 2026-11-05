package com.example.catalog.service;

import com.example.catalog.domain.Book;
import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public List<BookResponseDTO> list() {
        return storage.values().stream()
                .map(BookResponseDTO::fromDomain)
                .toList();
    }

    public BookResponseDTO get(Long id) {
        Book book = storage.get(id);
        return book != null ? BookResponseDTO.fromDomain(book) : null;
    }

    public BookResponseDTO create(BookRequestDTO request) {
        Long id = idCounter.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponseDTO.fromDomain(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponseDTO.fromDomain(book);
    }

    public void delete(Long id) {
        storage.remove(id);
    }
}