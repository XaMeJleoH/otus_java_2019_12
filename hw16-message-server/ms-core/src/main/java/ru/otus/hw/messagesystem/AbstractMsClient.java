package ru.otus.hw.messagesystem;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.messagesystem.common.Serializers;
import ru.otus.hw.socket.SocketClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractMsClient implements MsClient {

    private final String name;
    protected SocketClient socketClient;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();


    public AbstractMsClient(String name, SocketClient socketClient) {
        this.name = name;
        this.socketClient = socketClient;
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = socketClient.newMessage(msg);
        if (!result) {
            log.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                log.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMsClient msClient = (AbstractMsClient) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
