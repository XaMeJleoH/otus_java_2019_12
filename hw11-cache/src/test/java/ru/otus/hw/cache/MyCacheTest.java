package ru.otus.hw.cache;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import core.service.impl.DbServiceUserCacheImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class MyCacheTest {

    private static final String DIFFERENCE_NOT_CACHE_TEXT = "Difference time for NOT cache in millis: ";
    private static final String DIFFERENCE_CACHE_TEXT = "Difference time for cache in millis: ";

    private static final int COUNT_FOR_LOOP_INSERT = 200;
    private static final int ARRAY_SIZE = 100100;
    private static final int COUNT_FOR_LOOP_SELECT = 30;

    private DBServiceUser dbServiceUser;
    private DBServiceUser dbServiceUserCache;
    
    @BeforeEach
    void setUp() {
        // TODO: 29.03.2020 Запускать с -Xmx156m -Xms156m, для проверки очистки кэша
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        //Not cache
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);

        //Cache
        SessionManagerHibernate sessionManagerCache = new SessionManagerHibernate(sessionFactory);
        UserDao userDaoCache = new UserDaoHibernate(sessionManagerCache);
        HwCache<Long, User> cache = new MyCache<>();
        dbServiceUserCache = new DbServiceUserCacheImpl(userDaoCache, cache);
    }

    @AfterEach
    void tearDown() {
        dbServiceUser = null;
        dbServiceUserCache = null;
    }

    @Test
    @DisplayName(" сохранение User")
    void saveUser() {
        long millisNotCache = createAndAssertUser(dbServiceUser);
        long millisCache = createAndAssertUser(dbServiceUserCache);

        System.out.println("Checking results for select count loop size= " + COUNT_FOR_LOOP_SELECT);
        showTheTimeResult(millisNotCache, millisCache);
        assertTrue(millisCache < millisNotCache);
    }

    private long createAndAssertUser(DBServiceUser someDbService) {
        LocalDateTime startTimeNotCache = LocalDateTime.now();
        User sheldon = getUser();
        long id = someDbService.saveUser(sheldon);
        loopInsertAndSelect(someDbService);

        Optional<User> mayBeCreatedUser = someDbService.getUser(id);
        System.out.println(mayBeCreatedUser);
        User returnedSheldon = mayBeCreatedUser.get();

        assertThat(sheldon.getId()).isGreaterThan(0);
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("name", sheldon.getName());
        assertThat(returnedSheldon).isNotNull().hasFieldOrPropertyWithValue("age", sheldon.getAge());
        assertThat(returnedSheldon.getAddress()).isNotNull().hasFieldOrPropertyWithValue("street", sheldon.getAddress().getStreet());
        assertEquals(3, returnedSheldon.getPhones().size());

        someDbService.deleteUser(returnedSheldon);
        Optional<User> voidSheldon = someDbService.getUser(id);
        assertFalse(voidSheldon.isPresent());
        LocalDateTime endTimeNotCache = LocalDateTime.now();

        return ChronoUnit.MILLIS.between(startTimeNotCache, endTimeNotCache);
    }

    @Test
    @DisplayName(" обновление User")
    void updateUser() {
        long millisNotCache = createAndUpdateAndAssertUser(dbServiceUser);
        long millisCache = createAndUpdateAndAssertUser(dbServiceUserCache);

        System.out.println("Checking results for select count loop size= " + COUNT_FOR_LOOP_SELECT);
        showTheTimeResult(millisNotCache, millisCache);
        assertTrue(millisCache < millisNotCache);
    }

    private long createAndUpdateAndAssertUser(DBServiceUser someDbService) {
        LocalDateTime startTimeNotCache = LocalDateTime.now();
        User sheldon = getUser();
        long id = someDbService.saveUser(sheldon);
        loopInsertAndSelect(someDbService);
        Optional<User> mayBeCreatedSheldon = someDbService.getUser(id);
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
        someDbService.updateUser(returnedSheldon);
        Optional<User> mayBeCreatedShredinger = someDbService.getUser(id);
        System.out.println(mayBeCreatedShredinger);

        User returnedShredinger = mayBeCreatedSheldon.get();
        assertThat(returnedShredinger.getId()).isEqualTo(sheldon.getId());
        assertThat(returnedShredinger).isNotNull().hasFieldOrPropertyWithValue("name", returnedShredinger.getName());
        assertThat(returnedShredinger).isNotNull().hasFieldOrPropertyWithValue("age", returnedShredinger.getAge());
        assertThat(returnedShredinger.getAddress()).isNotNull().hasFieldOrPropertyWithValue("street", returnedShredinger.getAddress().getStreet());
        assertEquals(4, returnedSheldon.getPhones().size());

        someDbService.deleteUser(returnedShredinger);
        Optional<User> voidShredinger = someDbService.getUser(id);
        assertFalse(voidShredinger.isPresent());
        LocalDateTime endTimeNotCache = LocalDateTime.now();

        return ChronoUnit.MILLIS.between(startTimeNotCache, endTimeNotCache);
    }

    private void loopInsertAndSelect(DBServiceUser someDbService) {
        for (int i = 0; i <= COUNT_FOR_LOOP_INSERT; i++) {
            User tempUser = new User();
            tempUser.setPhones(new ArrayList<>(ARRAY_SIZE + i));
            long tempId = someDbService.saveUser(tempUser);
            for (int k = 0; k <= COUNT_FOR_LOOP_SELECT; k++) {
                someDbService.getUser(tempId);
            }
        }
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

    private void showTheTimeResult(long millisNotCache, long millisCache) {
        if (millisCache > millisNotCache) {
            System.out.println(ColorUtil.fillTextColor(DIFFERENCE_CACHE_TEXT + millisCache, false));
            System.out.println(ColorUtil.fillTextColor(DIFFERENCE_NOT_CACHE_TEXT + millisNotCache, true));
        } else {
            System.out.println(ColorUtil.fillTextColor(DIFFERENCE_CACHE_TEXT + millisCache, true));
            System.out.println(ColorUtil.fillTextColor(DIFFERENCE_NOT_CACHE_TEXT + millisNotCache, false));
        }
    }
}