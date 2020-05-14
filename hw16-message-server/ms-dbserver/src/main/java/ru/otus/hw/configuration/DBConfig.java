package ru.otus.hw.configuration;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otus.hw.client.DBClient;
import ru.otus.hw.db.handlers.GetUserDataRequestHandler;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.messagesystem.*;
import ru.otus.hw.socket.DBSocketClient;
import ru.otus.hw.socket.DBSocketServer;

@Configuration
public class DBConfig {

    @Value("${db1.name}")
    private String db1;

    @Value("${db1.port}")
    private int db1Port;

    @Value("${db2.name}")
    private String db2;

    @Value("${db2.port}")
    private int db2Port;

    private final DBSocketClient dbSocketClient;
    private final DBServiceWebUser dbServiceWebUser;

    @Autowired
    public DBConfig(DBSocketClient dbSocketClient, DBServiceWebUser dbServiceWebUser) {
        this.dbSocketClient = dbSocketClient;
        this.dbServiceWebUser = dbServiceWebUser;
    }

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    @Profile("db1")
    public DBSocketServer dbServer1() {
        return new DBSocketServer(msClientImpl1(), db1Port);
    }

    @Bean
    @Profile("db1")
    public MsClient msClientImpl1() {
        return getMsClient(db1);
    }

    @Bean
    @Profile("db2")
    public DBSocketServer dbServer2() {
        return new DBSocketServer(msClientImpl1(), db2Port);
    }

    @Bean
    @Profile("db2")
    public MsClient msClientImpl2() {
        return getMsClient(db2);
    }

    private MsClient getMsClient(String db1) {
        val databaseMsClient = new DBClient(db1, dbSocketClient);
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceWebUser));
        messageSystem().addClient(databaseMsClient);
        return databaseMsClient;
    }
}
