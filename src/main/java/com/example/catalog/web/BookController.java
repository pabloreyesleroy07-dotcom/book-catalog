package com.example.catalog.web;

import com.example.catalog.domain.Book;
import com.example.catalog.dto.BookRequest;
import com.example.catalog.dto.BookResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookController {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @GetMapping
    public List<BookResponse> list() {
        return storage.values().stream()
                .map(BookResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public BookResponse getOne(@PathVariable Long id) {
        Book book = storage.get(id);
        return book != null ? BookResponse.fromDomain(book) : null;
    }

    @PostMapping
    public BookResponse create(@RequestBody BookRequest request) {
        Long id = idCounter.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponse.fromDomain(book);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @RequestBody BookRequest request) {
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return BookResponse.fromDomain(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        storage.remove(id);
    }
}