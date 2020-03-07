package ru.otus.hw;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Document {
    private Long documentId;
    private Integer serial;
    private String number;
    private LocalDate dateOut;
}
