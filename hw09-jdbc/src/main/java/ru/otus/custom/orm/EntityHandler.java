package ru.otus.custom.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityHandler {

    private final Map<Class<?>, EntityMeta> entityMap = new HashMap<>();

    EntityMeta serialize(Class<?> tClass) {
        if (entityMap.containsKey(tClass)) {
            return entityMap.get(tClass);
        }
        Field primaryKey = null;
        List<Field> columnNames = new ArrayList<>();
        var fields = tClass.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isTransient(f.getModifiers())) {
                continue;
            }
            if (f.getDeclaredAnnotation(Id.class) != null) {
                primaryKey = f;
                continue;
            }
            columnNames.add(f);
        }
        entityMap.put(tClass, new EntityMeta(primaryKey, tClass.getSimpleName(), columnNames));
        return entityMap.get(tClass);
    }

    <T> EntityMetaValue serialize(T objectData) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = objectData.getClass();
        EntityMeta entityMeta = serialize(aClass);
        List<Object> columnValues = new ArrayList<>();
        for (Field field : entityMeta.getFields()) {
            field.setAccessible(true);
            columnValues.add(field.get(objectData));
        }
        var primaryKeyField = entityMeta.getPrimaryKey();
        primaryKeyField.setAccessible(true);
        return new EntityMetaValue(primaryKeyField.get(objectData).toString(), entityMeta, columnValues);
    }

    <T> T deserialize(ResultSet resultSet, Class<T> tClass, EntityMeta entityMeta) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        var instance = tClass.getConstructor().newInstance();
        var fields = tClass.getDeclaredFields();
        for (Field f : fields) {
            if (entityMeta.getFields().contains(f) || entityMeta.getPrimaryKey().equals(f)) {
                f.setAccessible(true);
                f.set(instance, resultSet.getObject(f.getName()));
            }
        }
        return instance;
    }
}
