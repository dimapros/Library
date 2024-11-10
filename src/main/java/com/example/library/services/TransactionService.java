package com.example.library.services;

import com.example.library.entities.Author;
import com.example.library.entities.Reader;
import com.example.library.entities.Transaction;
import com.example.library.entities.TransactionType;
import com.example.library.repositories.AuthorRepository;
import com.example.library.repositories.BookRepository;
import com.example.library.repositories.ReaderRepository;
import com.example.library.repositories.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final AuthorRepository authorRepository;

    public TransactionService(TransactionRepository transactionRepository, BookRepository bookRepository,
                              ReaderRepository readerRepository, AuthorRepository authorRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.authorRepository = authorRepository;
    }

    public void createTransaction(Long bookId, Long readerId, TransactionType transactionType) {
        Transaction transaction = new Transaction();

        transaction.setBook(bookRepository.findById(bookId).
                orElseThrow(() -> new EntityNotFoundException("Book was not found")));
        transaction.setReader(readerRepository.findById(readerId).
                orElseThrow(() -> new EntityNotFoundException("Reader was not found")));

        transaction.setTransactionType(transactionType);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.TAKE);

        transactionRepository.save(transaction);
    }

    public Author getMostPopularAuthor(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactionList = transactionRepository.findAllByTransactionDateTimeBetween(startDate, endDate);

        Map<Long, Long> authorTransactionCount = transactionList.stream().
                flatMap(transaction -> transaction.getBook().getAuthors().stream())
                .collect(Collectors.groupingBy(Author::getId, Collectors.counting()));

        Long mostPopularAuthorId = authorTransactionCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return authorRepository.findById(mostPopularAuthorId).orElse(null);
    }

    public Reader getMostRead() {
        List<Transaction> transactionList = transactionRepository.findAll();
        Map<Long, Long> readers = transactionList.stream()
                .map(Transaction::getReader).collect(Collectors.groupingBy(Reader::getId, Collectors.counting()));

        Long mostRead = readers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        return readerRepository.getById(mostRead);
    }

    public List<Reader> getReadersUsingBooks() {
        List<Transaction> transactionList = transactionRepository.findAll();
        Map<Reader, Long> readerMap = transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getReader, Collectors.counting()));

        return readerMap.entrySet().stream()
                .sorted(Map.Entry.<Reader, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
