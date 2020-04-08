package ru.otus.hw.db.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class DatabaseSessionHibernate implements DatabaseSession {
  private final Session session;
  private final Transaction transaction;

  DatabaseSessionHibernate(Session session) {
    this.session = session;
    this.transaction = session.beginTransaction();
  }

  public Session getHibernateSession() {
    return session;
  }

  Transaction getTransaction() {
    return transaction;
  }

  void close() {
    if (transaction.isActive()) {
      transaction.commit();
    }
    session.close();
  }
}
