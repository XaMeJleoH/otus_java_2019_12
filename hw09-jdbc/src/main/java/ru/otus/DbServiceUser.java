package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.custom.orm.EntityHelper;
import ru.otus.custom.orm.JdbcMapperImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class DbServiceUser {
  private static final String CREATE_TABLE_USER_SQL = "create table user(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))";

  public static void main(String[] args) throws Exception {
    DataSource dataSource = new DataSourceH2();
    DbServiceUser demo = new DbServiceUser();

    demo.createTable(dataSource);

    SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    UserDao userDao = new UserDaoJdbc(sessionManager, new JdbcMapperImpl(sessionManager, new DbExecutor(), new EntityHelper()));
    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

    long id = dbServiceUser.saveUser(new User("Sheldon", 31));
    Optional<User> user = dbServiceUser.getUser(id);
    System.out.println(user);
    user.ifPresentOrElse(
            crUser -> log.info("created user, name:{}", crUser.getName()),
            () -> log.info("user was not created")
    );

    dbServiceUser.updateUser(new User(id, "Penny", 28));
    user = dbServiceUser.getUser(id);
    System.out.println(user);
    user.ifPresentOrElse(
            crUser -> log.info("updated user:" + crUser),
            () -> log.info("user was not updated")
    );
  }

  private void createTable(DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement pst = connection.prepareStatement(CREATE_TABLE_USER_SQL)) {
      pst.executeUpdate();
    }
    System.out.println("table created");
  }
}
