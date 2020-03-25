package ru.otus.custom.orm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class EntityMeta {
    private final String primaryKey;
    private final String name;
    private final List<String> columnNames;

    EntityMeta(String primaryKey, String name, List<String> columnNames) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.columnNames = List.copyOf(columnNames != null ? columnNames : new ArrayList<>());
    }
}
