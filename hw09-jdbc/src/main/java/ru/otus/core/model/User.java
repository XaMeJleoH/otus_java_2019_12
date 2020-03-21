package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.otus.custom.orm.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */

@Getter
@AllArgsConstructor
public class User {
    @Id
    private final long id;
    private final String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
