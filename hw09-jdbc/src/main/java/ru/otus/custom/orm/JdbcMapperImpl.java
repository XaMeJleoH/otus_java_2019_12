package ru.otus.custom.orm;

import lombok.extern.slf4j.Slf4j;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
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
        } catch (IllegalAccessException | SQLException ex) {
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
        } catch (IllegalAccessException | SQLException ex) {
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
                        return entityHandler.deserialize(resultSet, entityMeta);
                    }
                } catch (SQLException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
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
                " where " + entityMetaValue.getEntityMeta().getPrimaryKey().getName() + " = ?";
    }

    private String fillSets(EntityMetaValue entityMetaValue) {
        List<String> sets = new ArrayList<>();
        var columnNames = entityMetaValue.getEntityMeta().getFields();
        var columnValues = entityMetaValue.getColumnValues();
        for (int i = 0; i < columnNames.size(); i++) {
            sets.add(columnNames.get(i).getName() + " = '" + columnValues.get(i) + "'");
        }
        return String.join(", ", sets);
    }

    private String getCreateSql(String className, EntityMetaValue entityMetaValue) {
        if (createSqlMap.containsKey(className)) {
            return createSqlMap.get(className);
        }

        createSqlMap.put(className,
                "insert into " + entityMetaValue.getEntityMeta().getName() +
                        "(" + convertFieldToSQLString(entityMetaValue.getEntityMeta().getFields()) + ")" +
                        " values (" + "?" + " , ?".repeat(entityMetaValue.getEntityMeta().getFields().size() - 1) + ")");

        return createSqlMap.get(className);
    }

    private String getLoadSql(String className, EntityMeta entityMeta) {
        if (loadSqlMap.containsKey(className)) {
            return loadSqlMap.get(className);
        }

        loadSqlMap.put(className,
                "select " + entityMeta.getPrimaryKey().getName() + ", " + convertFieldToSQLString(entityMeta.getFields()) +
                        " from " + entityMeta.getName() +
                        " where " + entityMeta.getPrimaryKey().getName() + "  = ?");

        return loadSqlMap.get(className);
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    private String convertFieldToSQLString(List<Field> fields) {
        List<String> sqlSet = new ArrayList<>();
        for (Field field : fields) {
            sqlSet.add(field.getName());
        }
        return String.join(", ", sqlSet);
    }

}
