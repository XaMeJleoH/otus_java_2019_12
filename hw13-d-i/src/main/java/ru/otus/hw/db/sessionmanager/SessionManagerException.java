package ru.otus.hw.db.sessionmanager;


class SessionManagerException extends RuntimeException {
  SessionManagerException(String msg) {
    super(msg);
  }

  SessionManagerException(Exception ex) {
    super(ex);
  }
}
