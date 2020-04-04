package ru.otus.hw.services;

import ru.otus.core.dao.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        // TODO: 04.04.2020
/*        return userDao.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);*/
        return false;
    }

}
