package ru.otus.hw.configuration;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.db.handlers.GetUserDataRequestHandler;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.front.FrontendService;
import ru.otus.hw.front.FrontendServiceImpl;
import ru.otus.hw.front.handlers.GetUserDataResponseHandler;
import ru.otus.hw.messagesystem.*;

import javax.annotation.PostConstruct;

@Configuration
public class MSConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    private final DBServiceWebUser dbServiceWebUser;

    @Autowired
    public MSConfig(DBServiceWebUser dbServiceWebUser) {
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient databaseMsClient(){
        val databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceWebUser));
        messageSystem().addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    public FrontendService frontendService(){
        val frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
        val frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        messageSystem().addClient(frontendMsClient);
        return frontendService;
    }

}
