package com.example.library.controllers;

import com.example.library.entities.Author;
import com.example.library.entities.Reader;
import com.example.library.entities.TransactionType;
import com.example.library.services.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/createTransaction")
    public String createTransaction(@RequestParam Long bookId, @RequestParam Long readerId, @RequestParam TransactionType transactionType) {
        transactionService.createTransaction(bookId, readerId, transactionType);
        return "Ok";
    }

    @GetMapping("/getMostPopularAuthor")
    public Author getMostPopularAuthor(@RequestParam String startDate, @RequestParam String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);
        return transactionService.getMostPopularAuthor(startDateTime, endDateTime);
    }

    @GetMapping("/mostReader")
    public Reader getMostReader() {
        return transactionService.getMostRead();
    }

    @GetMapping("/readersUsingBooks")
    public List<Reader> getReadersUsingBooks() {
        return transactionService.getReadersUsingBooks();
    }
}
