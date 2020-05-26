package ru.otus.hw.configuration;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.ConnectionProperties;
import ru.otus.hw.client.MSClientImpl;
import ru.otus.hw.messagesystem.MessageSystem;
import ru.otus.hw.messagesystem.MessageSystemImpl;
import ru.otus.hw.messagesystem.MsClient;
import ru.otus.hw.socket.MSSocketClient;

@Configuration
public class MSConfig {

    @Autowired
    private ConnectionProperties connectionProperties;

    private final MSSocketClient clientFront;
    private final MSSocketClient clientDB;

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
        clientFront.setHost(connectionProperties.getFrontend().getHost());
        clientFront.setPort(connectionProperties.getFrontend().getPort());

        val frontendMsClient = new MSClientImpl(connectionProperties.getFrontend().getName(), clientFront);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem){
        clientDB.setHost(connectionProperties.getDb().getHost());
        clientDB.setPort(connectionProperties.getDb().getPort());
        val databaseMsClient = new MSClientImpl(connectionProperties.getDb().getName(), clientDB);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
