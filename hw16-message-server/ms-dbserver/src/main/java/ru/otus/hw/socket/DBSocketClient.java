package ru.otus.hw.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBSocketClient extends AbstractSocketClient {

    public DBSocketClient(@Value("${ms.host}") String host,
                          @Value("${ms.port}") int port) {
        super(host, port);
    }

}
