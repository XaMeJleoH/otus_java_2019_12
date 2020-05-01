package ru.otus.hw.db.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.RequestHandler;
import ru.otus.hw.messagesystem.Serializers;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private final DBServiceWebUser dbServiceWebUser;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetUserDataRequestHandler(DBServiceWebUser dbServiceWebUser) {
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @SneakyThrows
    @Override
    public Optional<Message> handle(Message msg) {
        String userData = Serializers.deserialize(msg.getPayload(), String.class);
        long userId =  dbServiceWebUser.saveUser(objectMapper.readValue(userData, User.class));
        String returnUserData = objectMapper.writeValueAsString(dbServiceWebUser.getUser(userId).orElse(null));

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(returnUserData)));
    }
}
