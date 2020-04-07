package ru.otus.hw.web.repostory;

import ru.otus.core.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    long create(String name);
}
