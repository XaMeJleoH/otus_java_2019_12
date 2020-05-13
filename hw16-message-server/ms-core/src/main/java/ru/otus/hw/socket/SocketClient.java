package ru.otus.hw.socket;

import ru.otus.hw.messagesystem.Message;

public interface SocketClient {
    boolean newMessage(Message message);
}
