package ru.otus.jdbc.dao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.custom.orm.JdbcMapper;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class UserDaoJdbc implements UserDao {
  private static Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final JdbcMapper jdbcMapper;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper jdbcMapper) {
    this.sessionManager = sessionManager;
    this.jdbcMapper = jdbcMapper;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      var user = jdbcMapper.load(id, User.class);
      return Optional.ofNullable(user);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    try {
      return jdbcMapper.create(user);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public void updateUser(User user) {
    try {
      jdbcMapper.update(user);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
