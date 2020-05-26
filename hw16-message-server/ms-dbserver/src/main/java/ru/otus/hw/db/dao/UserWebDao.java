package ru.otus.hw.db.dao;

import ru.otus.hw.db.model.User;
import ru.otus.hw.db.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserWebDao {
  Optional<User> findById(long id);

  List<User> findAll();

  long saveUser(User user);

  void updateUser(User user);

  void deleteUser(User user);

  SessionManager getSessionManager();
}
