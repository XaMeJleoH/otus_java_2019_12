package ru.otus.hw.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.messagesystem.AbstractMsClient;
import ru.otus.hw.socket.SocketClient;

@Component
public class FrontendClient extends AbstractMsClient {
    public FrontendClient(@Value("${frontend.name}") String name, SocketClient databaseSocketClient) {
        super(name, databaseSocketClient);
    }
}
