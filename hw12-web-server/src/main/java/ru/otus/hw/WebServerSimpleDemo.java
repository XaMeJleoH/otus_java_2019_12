package ru.otus.hw;

import core.service.impl.DbServiceUserCacheImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw.cache.HwCache;
import ru.otus.hw.cache.MyCache;
import ru.otus.hw.server.UsersWebServer;
import ru.otus.hw.server.UsersWebServerSimple;
import ru.otus.hw.services.TemplateProcessor;
import ru.otus.hw.services.TemplateProcessorImpl;

import java.util.ArrayList;
import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerSimpleDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManagerCache = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManagerCache);
        HwCache<Long, User> cache = new MyCache<>();
        DBServiceUser dbServiceUser = new DbServiceUserCacheImpl(userDao, cache);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new UsersWebServerSimple(WEB_SERVER_PORT, dbServiceUser,
                 templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
