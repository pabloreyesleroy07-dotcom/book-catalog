package com.example.catalog.web;

import com.example.catalog.domain.Book;
import com.example.catalog.dto.BookRequest;
import com.example.catalog.dto.BookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<List<BookResponse>> list() {
        List<BookResponse> books = storage.values().stream()
                .map(BookResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getOne(@PathVariable Long id) {
        Book book = storage.get(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookResponse.fromDomain(book));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody BookRequest request) {
        Long id = idCounter.incrementAndGet();
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);

        BookResponse response = BookResponse.fromDomain(book);
        URI location = URI.create("/books/" + id);
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody BookRequest request) {
        if (!storage.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book(id, request.isbn(), request.title(), request.author(), request.year());
        storage.put(id, book);
        return ResponseEntity.ok(BookResponse.fromDomain(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Book removed = storage.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}