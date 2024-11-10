package com.example.library.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDateTime;

    @OneToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
