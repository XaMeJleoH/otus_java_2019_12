package ru.otus.hw.front;

import ru.otus.hw.db.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    void getUserData(long userId, Consumer<String> dataConsumer);

    List<User> getAllUsers(Consumer<String> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

