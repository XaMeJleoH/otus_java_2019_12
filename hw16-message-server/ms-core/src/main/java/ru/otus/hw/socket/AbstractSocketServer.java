package ru.otus.hw.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.messagesystem.MsClient;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public abstract class AbstractSocketServer implements SocketServer {

    private MsClient msClient;
    private int port;

    public AbstractSocketServer(MsClient msClient, int port) {
        this.msClient = msClient;
        this.port = port;
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
            msClient.handle(message);
            log.info("Message was received {}", message);
        }
    }
}
