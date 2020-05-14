package ru.otus.hw.socket;

import org.springframework.beans.factory.annotation.Value;
import ru.otus.hw.messagesystem.MsClient;

public class DBSocketServer extends AbstractSocketServer {

    public DBSocketServer(MsClient msClient, int port) {
        super(msClient, port);
    }

}