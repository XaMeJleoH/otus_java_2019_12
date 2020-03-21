package ru.otus.custom.orm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class EntityValue {
    private final String primaryKey;
    private final Entity entity;
    private final List<String> columnValues;

    EntityValue(String primaryKey, Entity entity, List<String> columnValues) {
        this.primaryKey = primaryKey;
        this.entity = entity;
        this.columnValues = List.copyOf(columnValues != null ? columnValues : new ArrayList<>());
    }
}
