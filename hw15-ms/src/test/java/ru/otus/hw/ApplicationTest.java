package ru.otus.hw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.otus.hw.db.handlers.GetUserDataRequestHandler;
import ru.otus.hw.db.model.Address;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.front.FrontendService;
import ru.otus.hw.front.FrontendServiceImpl;
import ru.otus.hw.front.handlers.GetUserDataResponseHandler;
import ru.otus.hw.messagesystem.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

@Slf4j
class ApplicationTest {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private MessageSystem messageSystem;
    private FrontendService frontendService;
    private MsClient databaseMsClient;
    private MsClient frontendMsClient;
    private Gson gson = new Gson();
    private ObjectMapper objectMapper = new ObjectMapper();

    private ArrayList<User> users = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        val user0 = createUser(0);
        val user1 = createUser(1);
        val user2 = createUser(2);
        users.add(user0);
        users.add(user1);
        users.add(user2);
    }

    @DisplayName("Базовый сценарий сохранения пользователя")
    @RepeatedTest(200)
    public void getDataById() throws Exception {
        createMessageSystem(true);
        CountDownLatch waitLatch = new CountDownLatch(users.size());

        IntStream.range(0, users.size()).forEach(id ->
                frontendService.saveUserData(gson.toJson(users.get(id)), data -> {
                    User user = null;
                    try {
                        user = objectMapper.readValue(data, User.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertNotNull(user);
                    assertThat(user.getId()).isEqualTo(id);

                    waitLatch.countDown();
                }));

        waitLatch.await();
        messageSystem.dispose();
        log.info("done");
    }

    @Test
    @DisplayName("Выполнение запроса после остановки сервиса")
    @RepeatedTest(200)
    public void getDataAfterShutdown() throws Exception {
        createMessageSystem(true);
        messageSystem.dispose();

        CountDownLatch waitLatchShutdown = new CountDownLatch(1);

        when(frontendMsClient.sendMessage(any(Message.class))).
                thenAnswer(invocation -> {
                    waitLatchShutdown.countDown();
                    return null;
                });

        frontendService.saveUserData(gson.toJson(new User()), data -> log.info("data:{}", data));
        waitLatchShutdown.await();
        boolean result = verify(frontendMsClient).sendMessage(any(Message.class));
        assertThat(result).isFalse();

        log.info("done");
    }

    @Test
    @DisplayName("Тестируем остановку работы MessageSystem")
    @RepeatedTest(200)
    public void stopMessageSystem() throws Exception {
        createMessageSystem(false);
        int counter = 100;
        CountDownLatch messagesSentLatch = new CountDownLatch(counter);
        CountDownLatch messageSystemDisposed = new CountDownLatch(1);

        IntStream.range(0, counter).forEach(id -> {
                    frontendService.saveUserData(gson.toJson(new User("Sheldon - " + id, id, new Address())), data -> {
                    });
                    messagesSentLatch.countDown();
                }
        );
        messagesSentLatch.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(counter);

        messageSystem.start();
        disposeMessageSystem(messageSystemDisposed::countDown);

        messageSystemDisposed.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(0);

        log.info("done");
    }


    private void createMessageSystem(boolean startProcessing) {
        log.info("setup");
        messageSystem = new MessageSystemImpl(startProcessing);

        databaseMsClient = spy(new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem));
        DBServiceWebUser dbService = mock(DBServiceWebUser.class);

        when(dbService.saveUser(users.get(0))).thenReturn(0L);
        when(dbService.saveUser(users.get(1))).thenReturn(1L);
        when(dbService.saveUser(users.get(2))).thenReturn(2L);

        when(dbService.getUser(0)).thenReturn(Optional.ofNullable(users.get(0)));
        when(dbService.getUser(1)).thenReturn(Optional.ofNullable(users.get(1)));
        when(dbService.getUser(2)).thenReturn(Optional.ofNullable(users.get(2)));

        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbService));
        messageSystem.addClient(databaseMsClient);

        frontendMsClient = spy(new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem));
        frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        log.info("setup done");
    }


    private User createUser(int id){
        User user = new User();
        user.setId(id);
        return user;
    }

    private void disposeMessageSystem(Runnable callback) {
        try {
            messageSystem.dispose(callback);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}