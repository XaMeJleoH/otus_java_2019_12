package ru.otus.hw.configuration;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.client.MSClientImpl;
import ru.otus.hw.messagesystem.MessageSystem;
import ru.otus.hw.messagesystem.MessageSystemImpl;
import ru.otus.hw.messagesystem.MessageType;
import ru.otus.hw.messagesystem.MsClient;
import ru.otus.hw.socket.MSSocketClient;

@Configuration
public class MSConfig {
    private final MSSocketClient clientFront;
    private final MSSocketClient clientDB;

    @Value("${db.name}")
    private String dbName;
    @Value("${db.host}")
    private String dbHost;
    @Value("${db.port}")
    private int dbPort;

    @Value("${frontend.name}")
    private String frontName;
    @Value("${frontend.host}")
    private String frontHost;
    @Value("${frontend.port}")
    private int frontPort;

    @Autowired
    public MSConfig(MSSocketClient clientForFront, MSSocketClient clientForDB) {
        this.clientFront = clientForFront;
        this.clientDB = clientForDB;
    }


    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }


    @Bean
    public MsClient frontendService(MessageSystem messageSystem){
        clientFront.setHost(frontHost);
        clientFront.setPort(frontPort);

        val frontendMsClient = new MSClientImpl(frontName, clientFront);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem){
        clientDB.setHost(dbHost);
        clientDB.setPort(dbPort);
        val databaseMsClient = new MSClientImpl(dbName, clientDB);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
