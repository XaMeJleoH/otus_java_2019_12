package ru.otus.hw.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.messagesystem.AbstractMsClient;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.socket.MSSocketClient;

@Slf4j
public class MSClientImpl extends AbstractMsClient {

    public MSClientImpl(String name, MSSocketClient msSocketClient) {
        super(name, msSocketClient);
    }

    @Override
    public void handle(Message msg) {
        try {


            log.info("new message:{}", msg);
            socketClient.newMessage(msg);
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
    }
}
