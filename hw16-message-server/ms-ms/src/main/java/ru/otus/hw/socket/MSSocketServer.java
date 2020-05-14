package ru.otus.hw.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.front.handlers.GetUserDataResponseHandler;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.MsClient;

@Component
public class MSSocketServer extends AbstractSocketServer {

    @Autowired
    public MSSocketServer(MsClient msClient, @Value("${frontend.port}") int port,
                          GetUserDataResponseHandler responseHandler) {
        super(msClient, port);
        msClient.addHandler(MessageType.USER_DATA, responseHandler);
    }

}