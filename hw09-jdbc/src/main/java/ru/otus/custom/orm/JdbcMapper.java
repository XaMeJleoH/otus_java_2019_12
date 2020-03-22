package ru.otus.custom.orm;

public interface JdbcMapper {

    <T> long create(T object);
    <T> void update(T object);
    <T> T load(long id, Class<T> tClass);
}
