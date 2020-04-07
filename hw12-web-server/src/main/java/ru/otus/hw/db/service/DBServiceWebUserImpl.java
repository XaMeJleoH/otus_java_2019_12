package ru.otus.hw.db.service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceException;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hw.cache.HwCache;
import ru.otus.hw.cache.HwListener;
import ru.otus.hw.db.dao.UserWebDao;
import ru.otus.hw.db.dao.UserWebDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DBServiceWebUserImpl implements DBServiceWebUser {

    private final UserWebDao userWebDao;
    private final HwCache<Long, User> cache;

    public DBServiceWebUserImpl(UserWebDao userWebDao, HwCache<Long, User> cache) {
        this.userWebDao = userWebDao;
        this.cache = cache;
        HwListener<Long, User> listener = (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(listener);
    }

    @Override
    public long saveUser(User user) {
        Long userId;
        try (SessionManager sessionManager = userWebDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userId = userWebDao.saveUser(user);
                cache.put(userId, user);
                sessionManager.commitSession();
                log.info("created user: {}", userId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
        cache.remove(userId);
        return userId;
    }


    @Override
    public Optional<User> getUser(long id) {
        Optional<User> userOptional = Optional.ofNullable(cache.get(id));
        if (userOptional.isEmpty()) {
            try (SessionManager sessionManager = userWebDao.getSessionManager()) {
                sessionManager.beginSession();
                try {
                    userOptional = userWebDao.findById(id);
                    userOptional.ifPresent(user -> {cache.put(user.getId(), user);});
                    log.info("user: {}", userOptional.orElse(null));
                } catch (Exception e) {
                    if (log != null) {
                        log.error(e.getMessage(), e);
                    }
                    sessionManager.rollbackSession();
                }
            }
        }
        userOptional.ifPresentOrElse(user -> cache.put(id, user), () -> cache.remove(id));
        return userOptional;
    }

    @Override
    public List<User> getAllUsers() {
        try (SessionManager sessionManager = userWebDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var users = userWebDao.findAll();
                log.info("users size: {}", users != null ? users.size() : 0);
                return users;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }
    }

    @Override
    public void updateUser(User user) {
        try (SessionManager sessionManager = userWebDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userWebDao.updateUser(user);
                sessionManager.commitSession();
                log.info("updated user: {}", user.getId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
        cache.remove(user.getId());
    }

    @Override
    public void deleteUser(User user) {
        try (SessionManager sessionManager = userWebDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userWebDao.deleteUser(user);
                sessionManager.commitSession();
                log.info("delete user: {}", user.getId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
        cache.remove(user.getId());
    }

}
