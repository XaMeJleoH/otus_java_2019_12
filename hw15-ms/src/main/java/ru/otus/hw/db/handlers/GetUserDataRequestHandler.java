package ru.otus.hw.db.handlers;

import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.RequestHandler;
import ru.otus.hw.messagesystem.Serializers;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private final DBServiceWebUser dbServiceWebUser;

    public GetUserDataRequestHandler(DBServiceWebUser dbServiceWebUser) {
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        long id = Serializers.deserialize(msg.getPayload(), Long.class);
        // TODO: 22.04.2020
        Optional<User> data = dbServiceWebUser.getUser(id);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(data.toString())));
    }
}
