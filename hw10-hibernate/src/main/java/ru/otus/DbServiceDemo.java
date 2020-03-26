package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        User sheldon = new User("Sheldon", 31, new Address("Mira"));

        List<Phone> phones = new ArrayList<>();
        Phone phoneHome = new Phone();
        phoneHome.setUser(sheldon);
        phoneHome.setNumber("811");
        phones.add(phoneHome);

        Phone phoneWork = new Phone();
        phoneWork.setUser(sheldon);
        phoneWork.setNumber("4833 349934");
        phones.add(phoneWork);

        Phone phonePenny = new Phone();
        phonePenny.setUser(sheldon);
        phonePenny.setNumber("993333 33");
        phones.add(phonePenny);

        sheldon.setPhones(phones);

        long id = dbServiceUser.saveUser(sheldon);

        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);
        outputUserOptional("Created user", mayBeCreatedUser);
    }

    private static void outputUserOptional(String header, Optional<User> mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
    }
}
