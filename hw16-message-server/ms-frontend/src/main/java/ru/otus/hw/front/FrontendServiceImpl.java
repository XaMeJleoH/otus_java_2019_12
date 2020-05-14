package ru.otus.hw.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.messagesystem.Message;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.MsClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FrontendServiceImpl(MsClient msClient, @Value("${db.name}") String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getUserData(long userId, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, userId, MessageType.USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveUserData(String userDataMessage, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, userDataMessage, MessageType.USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}
