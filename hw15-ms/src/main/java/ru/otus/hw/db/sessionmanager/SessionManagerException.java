package ru.otus.hw.db.sessionmanager;


public class SessionManagerException extends RuntimeException {
  public SessionManagerException(String msg) {
    super(msg);
  }

  SessionManagerException(Exception ex) {
    super(ex);
  }
}
