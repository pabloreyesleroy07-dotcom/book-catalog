package com.example.catalog.dto;

public record BookRequest(String isbn, String title, String author, int year) {
}