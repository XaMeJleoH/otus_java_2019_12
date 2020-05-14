package ru.otus.hw.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.messagesystem.AbstractMsClient;
import ru.otus.hw.socket.SocketClient;

public class DBClient extends AbstractMsClient {
    public DBClient(String name, SocketClient databaseSocketClient) {
        super(name, databaseSocketClient);
    }
}
