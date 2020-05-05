package ru.otus.hw.messagesystem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.otus.hw.db.model.User;

@Data
public class MessageStr {
    @JsonProperty("messageStr")
    private User user;
}
