package ru.otus.hw.db.service;


import ru.otus.hw.db.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceWebUser {

  long saveUser(User user);

  Optional<User> getUser(long id);

  List<User> getAllUsers();

  void updateUser(User user);

  void deleteUser(User user);

}
