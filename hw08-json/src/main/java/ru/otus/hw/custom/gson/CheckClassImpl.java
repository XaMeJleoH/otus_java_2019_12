package ru.otus.hw.custom.gson;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class CheckClassImpl implements CheckClass {
    @Override
    public ClassType checkClass(Class<?> fieldType) {
        if (Collection.class.isAssignableFrom(fieldType)) {
            return ClassType.COLLECTION;
        } else if (Map.class.isAssignableFrom(fieldType)) {
            return ClassType.MAP;
        } else if (String.class.isAssignableFrom(fieldType)) {
            return ClassType.STRING;
        } else if (Integer.class.isAssignableFrom(fieldType)) {
            return ClassType.INTEGER;
        } else if (Long.class.isAssignableFrom(fieldType)) {
            return ClassType.LONG;
        } else if (Float.class.isAssignableFrom(fieldType)) {
            return ClassType.FLOAT;
        } else if (Double.class.isAssignableFrom(fieldType)) {
            return ClassType.DOUBLE;
        } else if (Boolean.class.isAssignableFrom(fieldType)) {
            return ClassType.BOOLEAN;
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return ClassType.ENUM;
        } else if (byte.class.isAssignableFrom(fieldType) || Byte.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_BYTE;
        } else if (char.class.isAssignableFrom(fieldType) || Character.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_CHAR;
        } else if (short.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_SHORT;
        } else if (int.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_INT;
        } else if (long.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_LONG;
        } else if (float.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_FLOAT;
        } else if (double.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_DOUBLE;
        } else if (boolean.class.isAssignableFrom(fieldType)) {
            return ClassType.PRIMITIVE_BOOLEAN;
        } else if (fieldType.isArray()){
            return ClassType.ARRAY;
        } else if (LocalDate.class.isAssignableFrom(fieldType)){
            return ClassType.LOCAL_DATE;
        } else {
            return ClassType.OBJECT;
        }
    }
}
