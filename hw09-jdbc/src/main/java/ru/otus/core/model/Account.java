package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.custom.orm.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private long no;
    private String type;
    private String rest;

    public Account(String type, String rest) {
        this(0, type, rest);
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
