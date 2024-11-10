package com.example.library.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
