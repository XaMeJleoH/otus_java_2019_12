package ru.otus.hw;

import core.service.impl.DbServiceUserCacheImpl;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
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
import ru.otus.hw.db.dao.UserWebDao;
import ru.otus.hw.db.dao.UserWebDaoImpl;
import ru.otus.hw.db.service.DBServiceWebUser;
import ru.otus.hw.db.service.DBServiceWebUserImpl;
import ru.otus.hw.web.core.helpers.FileSystemHelper;
import ru.otus.hw.web.core.server.UsersWebServer;
import ru.otus.hw.web.core.server.UsersWebServerSimple;
import ru.otus.hw.web.core.services.TemplateProcessor;
import ru.otus.hw.web.core.services.TemplateProcessorImpl;

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
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManagerCache = new SessionManagerHibernate(sessionFactory);
        UserWebDao userWebDao = new UserWebDaoImpl(sessionManagerCache);
        HwCache<Long, User> cache = new MyCache<>();
        DBServiceWebUser dbServiceUser = new DBServiceWebUserImpl(userWebDao, cache);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        UsersWebServer usersWebServer = new UsersWebServerSimple(WEB_SERVER_PORT, dbServiceUser,
                loginService, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
