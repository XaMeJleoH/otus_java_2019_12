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

public class EntityHelper {

    private final Map<String, Entity> entityMap = new HashMap<>();

    private Entity serialize(Class clazz) {
        if (entityMap.containsKey(clazz.getName())) {
            return entityMap.get(clazz.getName());
        }
        String primaryKey = null;
        List<String> columnNames = new ArrayList<>();
        var fields = clazz.getDeclaredFields();
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
        entityMap.put(clazz.getName(), new Entity(primaryKey, clazz.getSimpleName(), columnNames));
        return entityMap.get(clazz.getName());
    }

    public <T> EntityValue serialize(T objectData) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = objectData.getClass();
        Entity entity = serialize(aClass);
        List<String> columnValues = new ArrayList<>();
        String primaryKey;
        for (String fName : entity.getColumnNames()) {
            var f = aClass.getDeclaredField(fName);
            f.setAccessible(true);
            columnValues.add(f.get(objectData).toString());
        }
        var primaryKeyField = aClass.getDeclaredField(entity.getPrimaryKey());
        primaryKeyField.setAccessible(true);
        primaryKey = primaryKeyField.get(objectData).toString();
        return new EntityValue(primaryKey, entity, columnValues);
    }

    public <T> T deserialize(ResultSet resultSet, Class<T> clazz, Entity entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, SQLException {
        var instance = clazz.getConstructor().newInstance();
        var fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (entity.getColumnNames().contains(f.getName()) || entity.getPrimaryKey().equals(f.getName())) {
                f.setAccessible(true);
                f.set(instance, resultSet.getObject(f.getName()));
            }
        }
        return instance;
    }
}
