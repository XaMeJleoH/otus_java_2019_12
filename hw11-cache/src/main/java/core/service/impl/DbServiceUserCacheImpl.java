package core.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceException;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hw.cache.HwCache;
import ru.otus.hw.cache.HwListener;

import java.util.Optional;

@Slf4j
public class DbServiceUserCacheImpl implements DBServiceUser {

    private final UserDao userDao;
    private final HwCache<Long, User> cache;

    public DbServiceUserCacheImpl(UserDao userDao, HwCache<Long, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
        HwListener<Long, User> listener = (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(listener);
    }

    @Override
    public long saveUser(User user) {
        Long userId;
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userId = userDao.saveUser(user);
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
            try (SessionManager sessionManager = userDao.getSessionManager()) {
                sessionManager.beginSession();
                try {
                    userOptional = userDao.findById(id);
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
    public void updateUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.updateUser(user);
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
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.deleteUser(user);
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
