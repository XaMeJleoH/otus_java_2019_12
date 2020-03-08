package ru.otus.hw.custom.gson;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.AccessRooms;
import ru.otus.hw.Document;
import ru.otus.hw.Subject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomGsonImplTest {

    private CustomGson customGson = new CustomGsonImpl();
    private Gson gson = new Gson();
    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = Subject.builder()
                .subjectId(221L)
                .name("Sheldon")
                .patronymic("Li")
                .surname("Cooper")
                .birthday(LocalDate.of(1970, 2, 26))
                .documents(createDocuments())
                .likeNumber(new int[]{73, 37})
                .accessRoomsBooleanMap(createAccess())
                .build();
    }

    @Test
    void toJson() throws IllegalAccessException {
        System.out.println(subject);
        String jsonFromGson = gson.toJson(subject);
        String jsonFromCustomGson = customGson.toJson(subject);
        System.out.println("Json from GSON: " + jsonFromGson);
        System.out.println("Json from Custom GSON: " + jsonFromCustomGson);
        assertEquals(jsonFromGson, jsonFromCustomGson);
    }

    private List<Document> createDocuments() {
        List<Document> documents = new ArrayList<>();
        Document document = Document.builder()
                .documentId(12L)
                .type(2)
                .serial(323)
                .number("329FC3")
                .dateOut(LocalDate.of(2000, 7, 21))
                .build();

        Document document1 = Document.builder()
                .documentId(712L)
                .type(6)
                .serial(17)
                .number("FICIA19")
                .dateOut(LocalDate.of(2019, 10, 12))
                .build();

        documents.add(document);
        documents.add(document1);
        return documents;
    }

    private Map<AccessRooms, Boolean> createAccess() {
        Map<AccessRooms, Boolean> accessRoomsBooleanMap = new HashMap<>();
        accessRoomsBooleanMap.put(AccessRooms.LABORATORY, Boolean.TRUE);
        accessRoomsBooleanMap.put(AccessRooms.SHELDON_ROOM, Boolean.TRUE);
        accessRoomsBooleanMap.put(AccessRooms.PENNY_FLAT, Boolean.FALSE);
        accessRoomsBooleanMap.put(AccessRooms.TOILET, Boolean.TRUE);
        return accessRoomsBooleanMap;
    }
}