package ru.otus.hw.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.db.handlers.GetUserDataRequestHandler;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.MsClient;

@Component
public class DBSocketServer extends AbstractSocketServer {

    @Autowired
    public DBSocketServer(MsClient msClient, @Value("${db.port}") int port,
                          GetUserDataRequestHandler requestHandler) {
        super(msClient, port);
        msClient.addHandler(MessageType.USER_DATA, requestHandler);
    }

}