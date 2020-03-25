package ru.otus.core.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.custom.orm.EntityHandler;
import ru.otus.custom.orm.JdbcMapperImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.AccountDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class DbServiceAccountImplTest {
    private static final String CREATE_TABLE_ACCOUNT_SQL = "create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)";
    private static final String DROP_TABLE_ACCOUNT_SQL = "drop table if exists Account";
    private DBServiceAccount dbServiceAccount;
    private DataSource dataSource = new DataSourceH2();

    private Account sheldonAccount = new Account("Comics", BigDecimal.ONE);
    private Account pennyAccount = new Account("Brushes", BigDecimal.TEN);


    @BeforeEach
    void setUp() throws SQLException {
        createTable(dataSource);
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        AccountDao accountDao = new AccountDaoJdbc(sessionManager, new JdbcMapperImpl(sessionManager, new DbExecutor(), new EntityHandler()));
        dbServiceAccount = new DbServiceAccountImpl(accountDao);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTable(dataSource);
    }

    @Test
    @DisplayName(" сохранение Account")
    void saveAccount() {

        long sheldonIn = dbServiceAccount.saveAccount(sheldonAccount);
        long pennyId = dbServiceAccount.saveAccount(pennyAccount);

        Account returnedSheldon = dbServiceAccount.getAccount(sheldonIn).get();
        Account returnedPenny = dbServiceAccount.getAccount(pennyId).get();

        assertEquals(sheldonAccount.getType(), returnedSheldon.getType());
        assertEquals(sheldonAccount.getRest(), returnedSheldon.getRest());

        assertEquals(pennyAccount.getType(), returnedPenny.getType());
        assertEquals(pennyAccount.getRest(), returnedPenny.getRest());
    }

    @Test
    @DisplayName(" обновление Account")
    void updateAccount() {

        long sheldonIn = dbServiceAccount.saveAccount(sheldonAccount);
        long pennyId = dbServiceAccount.saveAccount(pennyAccount);

        Account returnedSheldonAccount = dbServiceAccount.getAccount(sheldonIn).get();
        Account returnedPennyAccount = dbServiceAccount.getAccount(pennyId).get();

        final Account sheldonAccountAccept = new Account(returnedSheldonAccount.getNo(), "Comics", BigDecimal.TEN);
        final Account pennyAccountCanceled = new Account(returnedPennyAccount.getNo(), "Brushes", BigDecimal.ZERO);

        dbServiceAccount.updateAccount(sheldonAccountAccept);
        dbServiceAccount.updateAccount(pennyAccountCanceled);

        Account returnedSheldonAccountAccept = dbServiceAccount.getAccount(sheldonIn).get();
        Account returnedPennyAccountCanceled= dbServiceAccount.getAccount(pennyId).get();

        assertEquals(sheldonAccountAccept.getType(), returnedSheldonAccountAccept.getType());
        assertEquals(sheldonAccountAccept.getRest(), returnedSheldonAccountAccept.getRest());
        assertEquals(pennyAccountCanceled.getType(), returnedPennyAccountCanceled.getType());
        assertEquals(pennyAccountCanceled.getRest(), returnedPennyAccountCanceled.getRest());
    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(CREATE_TABLE_ACCOUNT_SQL)) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }

    private void dropTable(DataSource dataSource) throws SQLException {
        try (PreparedStatement pst1 = dataSource.getConnection().prepareStatement(DROP_TABLE_ACCOUNT_SQL)) {
            pst1.executeUpdate();
        }
        System.out.println("table dropped");
    }
}