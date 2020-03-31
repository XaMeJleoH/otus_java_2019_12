package ru.otus.core.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.custom.orm.EntityHandler;
import ru.otus.custom.orm.JdbcMapperImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DbServiceUserImplTest {
    private static final String CREATE_TABLE_USER_SQL = "create table user(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))";
    private static final String DROP_TABLE_USER_SQL = "drop table if exists User";
    private DBServiceUser dbServiceUser;
    private DataSource dataSource = new DataSourceH2();

    private User sheldon = new User("Sheldon", 31);
    private User penny = new User("Penny", 28);


    @BeforeEach
    void setUp() throws SQLException {
        createTable(dataSource);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        UserDao userDao = new UserDaoJdbc(sessionManager, new JdbcMapperImpl(sessionManager, new DbExecutor(), new EntityHandler()));
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTable(dataSource);
    }

    @Test
    @DisplayName(" сохранение User")
    void saveUser() {

        long sheldonIn = dbServiceUser.saveUser(sheldon);
        long pennyId = dbServiceUser.saveUser(penny);

        User returnedSheldon = dbServiceUser.getUser(sheldonIn).get();
        User returnedPenny = dbServiceUser.getUser(pennyId).get();

        assertEquals(sheldon.getName(), returnedSheldon.getName());
        assertEquals(sheldon.getAge(), returnedSheldon.getAge());

        assertEquals(penny.getName(), returnedPenny.getName());
        assertEquals(penny.getAge(), returnedPenny.getAge());
    }

    @Test
    @DisplayName(" обновление User")
    void updateUser() {

        long sheldonIn = dbServiceUser.saveUser(sheldon);
        long pennyId = dbServiceUser.saveUser(penny);

        User returnedSheldon = dbServiceUser.getUser(sheldonIn).orElse(null);
        User returnedPenny = dbServiceUser.getUser(pennyId).orElse(null);

        assertNotNull(returnedSheldon);
        assertNotNull(returnedPenny);

        final User sheldonator = new User(returnedSheldon.getId(), "Sheldonator", 111);
        final User pennyWise = new User(returnedPenny.getId(), "Penny Wise", 125);

        System.out.println("now!");
        dbServiceUser.updateUser(sheldonator);
        dbServiceUser.updateUser(pennyWise);

        User returnedSheldonator = dbServiceUser.getUser(sheldonIn).get();
        User returnedPennyWise = dbServiceUser.getUser(pennyId).get();

        assertEquals(sheldonator.getName(), returnedSheldonator.getName());
        assertEquals(sheldonator.getAge(), returnedSheldonator.getAge());
        assertEquals(pennyWise.getName(), returnedPennyWise.getName());
        assertEquals(pennyWise.getAge(), returnedPennyWise.getAge());
    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(CREATE_TABLE_USER_SQL)) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }

    private void dropTable(DataSource dataSource) throws SQLException {
        try (PreparedStatement pst1 = dataSource.getConnection().prepareStatement(DROP_TABLE_USER_SQL)) {
            pst1.executeUpdate();
        }
        System.out.println("table dropped");
    }
}