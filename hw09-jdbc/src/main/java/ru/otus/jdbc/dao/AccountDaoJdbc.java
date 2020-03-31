package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.AccountDaoException;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.custom.orm.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {
  private static Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final JdbcMapper jdbcMapper;

  public AccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper jdbcMapper) {
    this.sessionManager = sessionManager;
    this.jdbcMapper = jdbcMapper;
  }


  @Override
  public Optional<Account> findById(long id) {
    try {
      var account = jdbcMapper.load(id, Account.class);
      return Optional.ofNullable(account);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveAccount(Account account) {
    try {
      return jdbcMapper.create(account);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new AccountDaoException(e);
    }
  }

  @Override
  public void updateAccount(Account account) {
    try {
      jdbcMapper.update(account);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new AccountDaoException(e);
    }
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
