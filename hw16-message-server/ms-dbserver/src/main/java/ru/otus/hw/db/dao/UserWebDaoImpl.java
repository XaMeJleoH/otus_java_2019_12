package ru.otus.hw.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.otus.hw.db.model.User;
import ru.otus.hw.db.sessionmanager.SessionManager;
import ru.otus.hw.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hw.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserWebDaoImpl implements UserWebDao {
    private final SessionManagerHibernate sessionManager;

    public UserWebDaoImpl(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        var currentSession = sessionManager.getCurrentSession();
        try {
            var session = currentSession.getHibernateSession();
            var criteriaBuilder = session.getCriteriaBuilder();
            var criteriaQuery = criteriaBuilder.createQuery(User.class);
            var rootEntry = criteriaQuery.from(User.class);
            var all = criteriaQuery.select(rootEntry);
            var allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserWebDaoException(e);
        }
    }


    @Override
    public long saveUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
            }
            return user.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserWebDaoException(e);
        }
    }


    @Override
    public void deleteUser(User user) {
        var currentSession = sessionManager.getCurrentSession();
        try {
            currentSession.getHibernateSession().delete(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserWebDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        var currentSession = sessionManager.getCurrentSession();
        try {
            currentSession.getHibernateSession().update(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserWebDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
