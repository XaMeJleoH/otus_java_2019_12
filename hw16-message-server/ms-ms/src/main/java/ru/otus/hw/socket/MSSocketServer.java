package ru.otus.hw.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.messagesystem.MessageSystem;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
public class MSSocketServer implements SocketServer {

    @Value("${ms.host}")
    private String host;
    @Value("${ms.port}")
    private int port;
    private MessageSystem messageSystem;

    @Autowired
    public MSSocketServer(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }


    @SneakyThrows
    @Override
    public void initializeServer() {
        try (val serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    log.info("client connected");
                    clientHandler(clientSocket);
                }
            }
        }
    }

    @SneakyThrows
    private void clientHandler(Socket socketClient) {
        try (val objectInputStream = new ObjectInputStream(socketClient.getInputStream())) {
            val message = (Message) objectInputStream.readObject();
            messageSystem.newMessage(message);
            log.info("Message was received {}", message);
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

}