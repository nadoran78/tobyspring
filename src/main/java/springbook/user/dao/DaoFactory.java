package springbook.user.dao;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public DataSource dataSource() throws ClassNotFoundException {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    dataSource.setDriverClass(
        (Class<? extends Driver>) Class.forName("com.mysql.cj.jdbc.Driver"));
    dataSource.setUrl("jdbc:mysql://localhost/springbook");
    dataSource.setUsername("root");
    dataSource.setPassword("toby");

    return dataSource;
  }

  @Bean
  public UserDao userDao() throws ClassNotFoundException {
    UserDao userDao = new UserDao();
    userDao.setDataSource(dataSource());
    return userDao;
  }

  public AccountDao accountDao() {
    return new AccountDao(connectionMaker());
  }

  public MessageDao messageDao() {
    return new MessageDao(connectionMaker());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new DConnectionMaker();
  }
}
