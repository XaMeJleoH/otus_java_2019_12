package ru.otus.hw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Document {
    private Long documentId;
    private Integer type;
    private Integer serial;
    private String number;
    private LocalDate dateOut;
}
