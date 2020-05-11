package ru.otus.hw.db.service;

class DbServiceException extends RuntimeException {
  DbServiceException(Exception e) {
    super(e);
  }
}
