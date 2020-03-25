package ru.otus.custom.orm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class EntityMetaValue {
    private final String primaryKey;
    private final EntityMeta entityMeta;
    private final List<String> columnValues;

    EntityMetaValue(String primaryKey, EntityMeta entityMeta, List<String> columnValues) {
        this.primaryKey = primaryKey;
        this.entityMeta = entityMeta;
        this.columnValues = List.copyOf(columnValues != null ? columnValues : new ArrayList<>());
    }
}
