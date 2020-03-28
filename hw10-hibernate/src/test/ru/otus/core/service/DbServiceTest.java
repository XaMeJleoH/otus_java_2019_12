package ru.otus.core.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DbServiceTest {

    private DBServiceUser dbServiceUser;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }


    @Test
    @DisplayName(" сохранение User")
    void saveUser() {

        User sheldon = getUser();
        long id = dbServiceUser.saveUser(sheldon);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);
        System.out.println(mayBeCreatedUser);

        User returnedSheldon = mayBeCreatedUser.get();

        assertThat(sheldon.getId()).isGreaterThan(0);
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("name", sheldon.getName());
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("age", sheldon.getAge());
        assertThat(returnedSheldon.getAddress()).isNotNull().hasFieldOrPropertyWithValue("street", sheldon.getAddress().getStreet());
        assertEquals(3, returnedSheldon.getPhones().size());


        dbServiceUser.deleteUser(returnedSheldon);
        Optional<User> voidSheldon = dbServiceUser.getUser(id);
        assertFalse(voidSheldon.isPresent());
    }

    @Test
    @DisplayName(" обновление User")
    void updateUser() {

        User sheldon = getUser();
        long id = dbServiceUser.saveUser(sheldon);
        Optional<User> mayBeCreatedSheldon = dbServiceUser.getUser(id);
        System.out.println(mayBeCreatedSheldon);

        User returnedSheldon = mayBeCreatedSheldon.get();

        assertThat(sheldon.getId()).isGreaterThan(0);
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("name", sheldon.getName());
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("age", sheldon.getAge());
        assertThat(returnedSheldon.getAddress()).isNotNull().hasFieldOrPropertyWithValue("street", sheldon.getAddress().getStreet());
        assertEquals(3, returnedSheldon.getPhones().size());

        returnedSheldon.setName("Shredinger");
        returnedSheldon.getAddress().setStreet("Box");
        returnedSheldon.getPhones().add(getAdditionalPhone(returnedSheldon));
        dbServiceUser.updateUser(returnedSheldon);
        Optional<User> mayBeCreatedShredinger = dbServiceUser.getUser(id);
        System.out.println(mayBeCreatedShredinger);

        User returnedShredinger = mayBeCreatedSheldon.get();
        assertThat(returnedShredinger.getId()).isEqualTo(sheldon.getId());
        assertThat(returnedShredinger).isNotNull().hasFieldOrPropertyWithValue("name", returnedShredinger.getName());
        assertThat(returnedShredinger).isNotNull().hasFieldOrPropertyWithValue("age", returnedShredinger.getAge());
        assertThat(returnedShredinger.getAddress()).isNotNull().hasFieldOrPropertyWithValue("street", returnedShredinger.getAddress().getStreet());
        assertEquals(4, returnedSheldon.getPhones().size());

        dbServiceUser.deleteUser(returnedShredinger);
        Optional<User> voidShredinger = dbServiceUser.getUser(id);
        assertFalse(voidShredinger.isPresent());
    }

    private User getUser() {
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
        return sheldon;
    }

    private Phone getAdditionalPhone(User user) {
        Phone additionalPhone = new Phone();
        additionalPhone.setUser(user);
        additionalPhone.setNumber("bazinga");
        return additionalPhone;
    }

}
