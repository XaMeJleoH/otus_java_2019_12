package ru.otus.hw.web.repostory;

import org.springframework.stereotype.Repository;
import ru.otus.core.model.User;
import ru.otus.hw.web.generators.UserIdGenerator;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private final UserIdGenerator userIdGenerator;

    public UserRepositoryImpl(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
        // TODO: 07.04.2020
/*        this.users.add(new User(this.userIdGenerator.getUserId(), "Ivan"));
        this.users.add(new User(this.userIdGenerator.getUserId(), "Petr"));*/
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public long create(String name) {
        long userId = this.userIdGenerator.getUserId();
        // TODO: 07.04.2020
        /*this.users.add(new User(userId, name));*/
        return userId;
    }
}
