package ru.otus.hw;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Subject {
    private Long subjectId;
    private String name;
    private String patronymic;
    private String surname;
    private LocalDate birthday;
    private List<Document> documents;
    private int[] accessTypes;
}
