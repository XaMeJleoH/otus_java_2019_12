package ru.otus.hw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Subject {
    private Long subjectId;
    private String name;
    private String patronymic;
    private String surname;
    private LocalDate birthday;
    private List<Document> documents;
    private int[] likeNumber;
    private Map<AccessRooms, Boolean> accessRoomsBooleanMap;
}
