package ru.otus.hw.custom.gson;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class CustomGsonImpl implements CustomGson {

    private CheckClass checkClass = new CheckClassImpl();

    @Override
    public String toJson(Object object) throws IllegalAccessException {
        if (object != null) {
            return ObjectBuilder(object).build().toString();
        }
        throw new IllegalAccessException("Object is null");
    }

    private JsonObjectBuilder ObjectBuilder(Object object) throws IllegalAccessException {
        var objectBuilder = Json.createObjectBuilder();
        var objectClass = object.getClass();

        while (!objectClass.equals(Object.class)) {
            var fields = objectClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                var fieldValue = field.get(object);
                if (fieldValue == null || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                fillJsonObjectBuilder(objectBuilder, field.getName(), field.getType(), fieldValue);
            }
            objectClass = objectClass.getSuperclass();
        }
        return objectBuilder;
    }


    private JsonObjectBuilder MapBuilder(Object object) throws IllegalAccessException {
        var objectBuilder = Json.createObjectBuilder();
        for (Object key : ((Map) object).keySet()) {
            Object value = ((Map) object).get(key);
            fillJsonObjectBuilder(objectBuilder, key.toString(), value.getClass(), value);
        }
        return objectBuilder;
    }

    private JsonArrayBuilder ArrayBuilder(Object object) throws IllegalAccessException {
        var arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < Array.getLength(object); i++) {
            Object value = Array.get(object, i);
            if (value != null) {
                fillJsonArrayBuilder(arrayBuilder, value.getClass(), value);
            } else {
                arrayBuilder.addNull();
            }
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder ArrayBuilder(Collection collection) throws IllegalAccessException {
        return ArrayBuilder(collection.toArray());
    }

    private void fillJsonObjectBuilder(JsonObjectBuilder builder, String fieldName, Class<?> fieldType, Object fieldValue) throws IllegalAccessException {
        switch (checkClass.checkClass(fieldType)) {
            case COLLECTION:
                builder.add(fieldName, ArrayBuilder((Collection) fieldValue));
                break;
            case MAP:
                builder.add(fieldName, MapBuilder(fieldValue));
                break;
            case STRING:
                builder.add(fieldName, (String) fieldValue);
                break;
            case INTEGER:
                builder.add(fieldName, (Integer) fieldValue);
                break;
            case LONG:
                builder.add(fieldName, (Long) fieldValue);
                break;
            case FLOAT:
                builder.add(fieldName, (Float) fieldValue);
                break;
            case DOUBLE:
                builder.add(fieldName, (Double) fieldValue);
                break;
            case BOOLEAN:
                builder.add(fieldName, (Boolean) fieldValue);
                break;
            case ENUM:
                builder.add(fieldName, fieldValue.toString());
                break;
            case PRIMITIVE_BYTE:
                builder.add(fieldName, (byte) fieldValue);
                break;
            case PRIMITIVE_CHAR:
                builder.add(fieldName, (char) fieldValue);
                break;
            case PRIMITIVE_SHORT:
                builder.add(fieldName, (short) fieldValue);
                break;
            case PRIMITIVE_INT:
                builder.add(fieldName, (int) fieldValue);
                break;
            case PRIMITIVE_LONG:
                builder.add(fieldName, (long) fieldValue);
                break;
            case PRIMITIVE_FLOAT:
                builder.add(fieldName, (float) fieldValue);
                break;
            case PRIMITIVE_DOUBLE:
                builder.add(fieldName, (double) fieldValue);
                break;
            case PRIMITIVE_BOOLEAN:
                builder.add(fieldName, (boolean) fieldValue);
                break;
            case ARRAY:
                builder.add(fieldName, ArrayBuilder(fieldValue));
                break;
            case LOCAL_DATE:
                builder.add(fieldName, fieldValue.toString());
                break;

            default:
                builder.add(fieldName, ObjectBuilder(fieldValue));
                break;
        }
    }

    private void fillJsonArrayBuilder(JsonArrayBuilder builder, Class<?> fieldType, Object fieldValue) throws IllegalAccessException {
        switch (checkClass.checkClass(fieldType)) {
            case COLLECTION:
                builder.add(ArrayBuilder((Collection) fieldValue));
                break;
            case MAP:
                builder.add(MapBuilder(fieldValue));
                break;
            case STRING:
                builder.add((String) fieldValue);
                break;
            case INTEGER:
                builder.add((Integer) fieldValue);
                break;
            case LONG:
                builder.add((Long) fieldValue);
                break;
            case FLOAT:
                builder.add((Float) fieldValue);
                break;
            case DOUBLE:
                builder.add((Double) fieldValue);
                break;
            case BOOLEAN:
                builder.add((Boolean) fieldValue);
                break;
            case ENUM:
                builder.add(fieldValue.toString());
                break;
            case PRIMITIVE_BYTE:
                builder.add((byte) fieldValue);
                break;
            case PRIMITIVE_CHAR:
                builder.add((char) fieldValue);
                break;
            case PRIMITIVE_SHORT:
                builder.add((short) fieldValue);
                break;
            case PRIMITIVE_INT:
                builder.add((int) fieldValue);
                break;
            case PRIMITIVE_LONG:
                builder.add((long) fieldValue);
                break;
            case PRIMITIVE_FLOAT:
                builder.add((float) fieldValue);
                break;
            case PRIMITIVE_DOUBLE:
                builder.add((double) fieldValue);
                break;
            case PRIMITIVE_BOOLEAN:
                builder.add((boolean) fieldValue);
                break;
            case ARRAY:
                builder.add(ArrayBuilder(fieldValue));
                break;
            case LOCAL_DATE:
                builder.add(fieldValue.toString());
                break;

            default:
                builder.add(ObjectBuilder(fieldValue));
                break;
        }
    }
}

