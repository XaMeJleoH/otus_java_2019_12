package ru.otus.custom.orm;

import lombok.extern.slf4j.Slf4j;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class JdbcMapperImpl implements JdbcMapper {

    private final Map<String, String> createSqlMap = new HashMap<>();
    private final Map<String, String> loadSqlMap = new HashMap<>();

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor dbExecutor;
    private final EntityHandler entityHandler;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutor dbExecutor, EntityHandler entityHandler) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityHandler = entityHandler;
    }

    @Override
    public <T> long create(T object) {
        try {
            EntityMetaValue entityMetaValue = entityHandler.serialize(object);
            return dbExecutor.insertRecord(getConnection(),
                    getCreateSql(object.getClass().getName(), entityMetaValue),
                    entityMetaValue.getColumnValues());
        } catch (NoSuchFieldException | IllegalAccessException | SQLException ex) {
            log.error("Error on create. Error message:{}, Error:", ex.getMessage(), ex);
        }
        return -1;
    }

    @Override
    public <T> void update(T object) {
        try {
            EntityMetaValue entityMetaValue = entityHandler.serialize(object);
            dbExecutor.insertRecord(getConnection(),
                    getUpdateSql(entityMetaValue),
                    Collections.singletonList(entityMetaValue.getPrimaryKey()));
        } catch (NoSuchFieldException | IllegalAccessException | SQLException ex) {
            log.error("Error on update. Error message:{}, Error:", ex.getMessage(), ex);
        }
    }

    @Override
    public <T> T load(long id, Class<T> tClass) {
        EntityMeta entityMeta = entityHandler.serialize(tClass);
        try {
            var result = dbExecutor.selectRecord(getConnection(), getLoadSql(tClass.getName(), entityMeta), id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return entityHandler.deserialize(resultSet, tClass, entityMeta);
                    }
                } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                    log.error("Error on select. Error message:{}, Error:", ex.getMessage(), ex);
                }
                return null;
            });

            if (result.isPresent()) {
                return (T) result.get();
            }
        } catch (SQLException ex) {
            log.error("Error on select. Error message:{}, Error:", ex.getMessage(), ex);
        }
        return null;
    }


    private String getUpdateSql(EntityMetaValue entityMetaValue) {
        return "update " + entityMetaValue.getEntityMeta().getName() +
                " set " + fillSets(entityMetaValue) +
                " where " + entityMetaValue.getEntityMeta().getPrimaryKey() + " = ?";
    }

    private String fillSets(EntityMetaValue entityMetaValue) {
        List<String> sets = new ArrayList<>();
        var columnNames = entityMetaValue.getEntityMeta().getColumnNames();
        var columnValues = entityMetaValue.getColumnValues();
        for (int i = 0; i < columnNames.size(); i++) {
            sets.add(columnNames.get(i) + " = '" + columnValues.get(i) + "'");
        }
        return String.join(", ", sets);
    }

    private String getCreateSql(String className, EntityMetaValue entityMetaValue) {
        if (createSqlMap.containsKey(className)) {
            return createSqlMap.get(className);
        }

        createSqlMap.put(className,
                "insert into " + entityMetaValue.getEntityMeta().getName() +
                        "(" + String.join(", ", entityMetaValue.getEntityMeta().getColumnNames()) + ")" +
                        " values (" + "?" + " , ?".repeat(entityMetaValue.getEntityMeta().getColumnNames().size() - 1) + ")");

        return createSqlMap.get(className);
    }

    private String getLoadSql(String className, EntityMeta entityMeta) {
        if (loadSqlMap.containsKey(className)) {
            return loadSqlMap.get(className);
        }

        loadSqlMap.put(className,
                "select " + entityMeta.getPrimaryKey() + ", " + String.join(", ", entityMeta.getColumnNames()) +
                        " from " + entityMeta.getName() +
                        " where " + entityMeta.getPrimaryKey() + "  = ?");

        return loadSqlMap.get(className);
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
