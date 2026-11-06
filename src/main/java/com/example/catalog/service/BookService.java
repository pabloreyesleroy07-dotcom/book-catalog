package com.example.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.catalog.domain.Book;
import com.example.catalog.dto.BookRequestDTO;
import com.example.catalog.dto.BookResponseDTO;

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
        if (book == null) {
            throw new BookNotFoundException("Book not found");
        }
        return BookResponseDTO.fromDomain(book);
    }

    public BookResponseDTO create(BookRequestDTO request) {
        Long id = idCounter.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponseDTO.fromDomain(book);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        if (!storage.containsKey(id)) {
            throw new BookNotFoundException("Book not found");
        }
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponseDTO.fromDomain(book);
    }

    public void delete(Long id) {
        if (storage.remove(id) == null) {
            throw new BookNotFoundException("Book not found");
        }
    }
}