package ru.otus.hw.socket;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.hw.messagesystem.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@Setter
@Component
@Scope("prototype")
public class MSSocketClient implements SocketClient {

    private String host;
    private int port;

    @SneakyThrows
    public boolean newMessage(Message message) {
        try (val socket = new Socket(host, port)) {
            val outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
            log.info("Message was send {}, to={}:{}", message, host, port);

            return true;
        }
    }
}
