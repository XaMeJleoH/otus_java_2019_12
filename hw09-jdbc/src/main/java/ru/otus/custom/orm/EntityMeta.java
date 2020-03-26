package ru.otus.custom.orm;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
class EntityMeta {
    private final String primaryKey;
    private final String name;
    private final List<Field> columnNames;

    EntityMeta(String primaryKey, String name, List<Field> columnNames) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.columnNames = List.copyOf(columnNames != null ? columnNames : new ArrayList<>());
    }
}
