package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.custom.orm.Id;

@Getter
@AllArgsConstructor
public class Account {
    @Id
    private final long no;
    private final String type;
    private final String rest;

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
