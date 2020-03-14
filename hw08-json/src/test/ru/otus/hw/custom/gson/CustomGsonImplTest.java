package ru.otus.hw.custom.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.AccessRooms;
import ru.otus.hw.Document;
import ru.otus.hw.Subject;

import java.time.LocalDate;
import java.util.*;

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

    @Test
    void toJsonTests() throws IllegalAccessException {
        Gson gson = new GsonBuilder().serializeNulls().create();
        assertEquals(gson.toJson((byte) 1), customGson.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), customGson.toJson((short) 1f));
        assertEquals(gson.toJson(1), customGson.toJson(1));
        assertEquals(gson.toJson(1L), customGson.toJson(1L));
        assertEquals(gson.toJson(1f), customGson.toJson(1f));
        assertEquals(gson.toJson(1d), customGson.toJson(1d));
        assertEquals(gson.toJson("aaa"), customGson.toJson("aaa"));
        assertEquals(gson.toJson('a'), customGson.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), customGson.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), customGson.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), customGson.toJson(Collections.singletonList(1)));
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