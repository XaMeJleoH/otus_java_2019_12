package ru.otus.hw.configuration;

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

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient databaseMsClient(){
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public MsClient frontendMsClient(){
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public FrontendService frontendService(){
        return new FrontendServiceImpl(frontendMsClient(), DATABASE_SERVICE_CLIENT_NAME);
    }

    @PostConstruct
    private void postConstruct() {
        databaseMsClient().addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceWebUser));
        frontendMsClient().addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService()));

        messageSystem().addClient(frontendMsClient());
        messageSystem().addClient(databaseMsClient());
    }

}
