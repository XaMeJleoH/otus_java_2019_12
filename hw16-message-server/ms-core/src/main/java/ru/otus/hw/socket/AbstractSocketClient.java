package ru.otus.hw.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.hw.messagesystem.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public abstract class AbstractSocketClient implements SocketClient {

    private String host;
    private int port;

    public AbstractSocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SneakyThrows
    @Override
    public boolean newMessage(Message message) {
        try (val socket = new Socket(host, port);
             val outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(message);
            log.info("Message was send {}", message);

            return true;
        }
    }
}
