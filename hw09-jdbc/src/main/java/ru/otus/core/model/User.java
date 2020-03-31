package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.custom.orm.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String name;
    private int age;

    public User(String name, int age) {
        this(0, name, age);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
