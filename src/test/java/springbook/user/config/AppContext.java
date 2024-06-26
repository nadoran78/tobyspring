package springbook.user.config;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceTest.TestUserService;

@Configuration
@ComponentScan(basePackages = "springbook.user.dao")
@EnableTransactionManagement
@EnableSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig{

  @Value("${db.driverClass}") Class<? extends Driver> driverClass;
  @Value("${db.url}") String url;
  @Value("${db.username}") String username;
  @Value("${db.password}") String password;

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource ds = new SimpleDriverDataSource();

    ds.setDriverClass(this.driverClass);
    ds.setUrl(this.url);
    ds.setUsername(this.username);
    ds.setPassword(this.password);

    return ds;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    DataSourceTransactionManager tm = new DataSourceTransactionManager();
    tm.setDataSource(dataSource());
    return tm;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Override
  public Resource getSqlMapResource() {
    return new ClassPathResource("/sqlmap.xml", UserDao.class);
  }

  @Configuration
  @Profile("production")
  public static class ProductionAppContext {

    @Bean
    public MailSender mailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("localhost");
      return mailSender;
    }
  }

  @Configuration
  @Profile("test")
  public static class TestAppContext {

    @Bean
    public UserService testUserService() {
      return new TestUserService();
    }

    @Bean
    public MailSender mailSender() {
      return new DummyMailSender();
    }
  }
}
