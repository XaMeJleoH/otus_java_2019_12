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

    private final Map<Class, EntityMeta> entityMap = new HashMap<>();

    public EntityMeta serialize(Class tClass) {
        if (entityMap.containsKey(tClass)) {
            return entityMap.get(tClass);
        }
        String primaryKey = null;
        List<String> columnNames = new ArrayList<>();
        var fields = tClass.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isTransient(f.getModifiers())) {
                continue;
            }
            if (f.getDeclaredAnnotation(Id.class) != null) {
                primaryKey = f.getName();
                continue;
            }
            columnNames.add(f.getName());
        }
        entityMap.put(tClass, new EntityMeta(primaryKey, tClass.getSimpleName(), columnNames));
        return entityMap.get(tClass);
    }

    public <T> EntityMetaValue serialize(T objectData) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = objectData.getClass();
        EntityMeta entityMeta = serialize(aClass);
        List<Object> columnValues = new ArrayList<>();
        String primaryKey;
        for (String fName : entityMeta.getColumnNames()) {
            var f = aClass.getDeclaredField(fName);
            f.setAccessible(true);
            columnValues.add(f.get(objectData));

        }
        var primaryKeyField = aClass.getDeclaredField(entityMeta.getPrimaryKey());
        primaryKeyField.setAccessible(true);
        primaryKey = primaryKeyField.get(objectData).toString();
        return new EntityMetaValue(primaryKey, entityMeta, columnValues);
    }

    public <T> T deserialize(ResultSet resultSet, Class<T> tClass, EntityMeta entityMeta) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        var instance = tClass.getConstructor().newInstance();
        var fields = tClass.getDeclaredFields();
        for (Field f : fields) {
            if (entityMeta.getColumnNames().contains(f.getName()) || entityMeta.getPrimaryKey().equals(f.getName())) {
                f.setAccessible(true);
                f.set(instance, resultSet.getObject(f.getName()));
            }
        }
        return instance;
    }
}
