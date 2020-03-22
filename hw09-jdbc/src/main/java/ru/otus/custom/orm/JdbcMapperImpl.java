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
    private final EntityHelper entityHelper;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutor dbExecutor, EntityHelper entityHelper) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityHelper = entityHelper;
    }

    @Override
    public <T> long create(T object) {
        try {
            EntityValue entityValue = entityHelper.serialize(object);
            return dbExecutor.insertRecord(getConnection(),
                    getCreateSql(object.getClass().getName(), entityValue),
                    entityValue.getColumnValues());
        } catch (NoSuchFieldException | IllegalAccessException | SQLException ex) {
            log.error("Error on create. Error message:{}, Error:", ex.getMessage(), ex);
        }
        return -1;
    }

    @Override
    public <T> void update(T object) {
        try {
            EntityValue entityValue = entityHelper.serialize(object);
            dbExecutor.insertRecord(getConnection(),
                    getUpdateSql(entityValue),
                    Collections.singletonList(entityValue.getPrimaryKey()));
        } catch (NoSuchFieldException | IllegalAccessException | SQLException ex) {
            log.error("Error on update. Error message:{}, Error:", ex.getMessage(), ex);
        }
    }

    @Override
    public <T> T load(long id, Class<T> tClass) {
        Entity entity = entityHelper.serialize(tClass);
        try {
            var result = dbExecutor.selectRecord(getConnection(), getLoadSql(tClass.getName(), entity), id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return entityHelper.deserialize(resultSet, tClass, entity);
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


    private String getUpdateSql(EntityValue entityValue) {
        return "update " + entityValue.getEntity().getName() +
                " set " + fillSets(entityValue) +
                " where " + entityValue.getEntity().getPrimaryKey() + " = ?";
    }

    private String fillSets(EntityValue entityValue) {
        List<String> sets = new ArrayList<>();
        var columnNames = entityValue.getEntity().getColumnNames();
        var columnValues = entityValue.getColumnValues();
        for (int i = 0; i < columnNames.size(); i++) {
            sets.add(columnNames.get(i) + " = '" + columnValues.get(i) + "'");
        }
        return String.join(", ", sets);
    }

    private String getCreateSql(String className, EntityValue entityValue) {
        if (createSqlMap.containsKey(className)) {
            return createSqlMap.get(className);
        }

        createSqlMap.put(className,
                "insert into " + entityValue.getEntity().getName() +
                        "(" + String.join(", ", entityValue.getEntity().getColumnNames()) + ")" +
                        " values (" + "?" + " , ?".repeat(entityValue.getEntity().getColumnNames().size() - 1) + ")");

        return createSqlMap.get(className);
    }

    private String getLoadSql(String className, Entity entity) {
        if (loadSqlMap.containsKey(className)) {
            return loadSqlMap.get(className);
        }

        loadSqlMap.put(className,
                "select " + entity.getPrimaryKey() + ", " + String.join(", ", entity.getColumnNames()) +
                        " from " + entity.getName() +
                        " where " + entity.getPrimaryKey() + "  = ?");

        return loadSqlMap.get(className);
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
