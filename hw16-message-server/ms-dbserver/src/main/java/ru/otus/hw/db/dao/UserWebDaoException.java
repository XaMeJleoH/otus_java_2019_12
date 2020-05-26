package ru.otus.hw.db.dao;

public class UserWebDaoException extends RuntimeException {
  public UserWebDaoException(Exception ex) {
    super(ex);
  }
}
