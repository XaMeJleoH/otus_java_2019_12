package ru.otus.hw;


import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw.db.cache.HwCache;
import ru.otus.hw.db.cache.HwCacheImpl;
import ru.otus.hw.db.dao.UserWebDao;
import ru.otus.hw.db.dao.UserWebDaoImpl;
import ru.otus.hw.db.handlers.GetUserDataRequestHandler;
import ru.otus.hw.db.model.Address;
import ru.otus.hw.db.model.Phone;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.db.service.DBServiceWebUserImpl;
import ru.otus.hw.front.FrontendService;
import ru.otus.hw.front.FrontendServiceImpl;
import ru.otus.hw.front.handlers.GetUserDataResponseHandler;
import ru.otus.hw.hibernate.HibernateUtils;
import ru.otus.hw.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw.messagesystem.*;

public class MSMain {
    private static final Logger logger = LoggerFactory.getLogger(MSMain.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static void main(String[] args) throws InterruptedException {
        // TODO: 22.04.2020
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManagerCache = new SessionManagerHibernate(sessionFactory);
        UserWebDao userWebDao = new UserWebDaoImpl(sessionManagerCache);
        HwCache<Long, User> cache = new HwCacheImpl<>();
        DBServiceWebUser dbService = new DBServiceWebUserImpl(userWebDao, cache);
        User user= new User("Sheldon", 31, new Address());
        dbService.saveUser(user);

        MessageSystem messageSystem = new MessageSystemImpl();

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbService));
        messageSystem.addClient(databaseMsClient);


        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        frontendService.getUserData(1, data -> logger.info("got data:{}", data));

        Thread.sleep(100);
        messageSystem.dispose();
        logger.info("done");
    }
}
