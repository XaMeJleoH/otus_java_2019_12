package ru.otus.custom.orm;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
class EntityMeta {
    private final Field primaryKey;
    private final String name;
    private final List<Field> fields;
    private final Constructor constructor;

    EntityMeta(Field primaryKey, String name, List<Field> fields, Constructor constructor) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.fields = List.copyOf(fields != null ? fields : new ArrayList<>());
        this.constructor = constructor;
    }
}
