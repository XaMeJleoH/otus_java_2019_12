package ru.otus.custom.orm;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
class EntityMetaValue {
    private final String primaryKey;
    private final EntityMeta entityMeta;
    private final List<Object> columnValues;

    EntityMetaValue(String primaryKey, EntityMeta entityMeta, List<Object> columnValues) {
        this.primaryKey = primaryKey;
        this.entityMeta = entityMeta;
        this.columnValues = List.copyOf(columnValues != null ? columnValues : new ArrayList<>());
    }
}
