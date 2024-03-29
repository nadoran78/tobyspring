package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

class UserDaoTest {

  private UserDao dao;
  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {

    dao = new UserDao();
    DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb",
        "root", "toby", true);
    dao.setDataSource(dataSource);

    this.user1 = new User("test1", "홍길동1", "password1");
    this.user2 = new User("test2", "홍길동2", "password2");
    this.user3 = new User("test3", "홍길동3", "password3");
  }

  @Test
  public void addAndGet() throws SQLException {

    dao.deleteAll();
    assertEquals(0, dao.getCount());

    dao.add(user1);
    dao.add(user2);
    assertEquals(2, dao.getCount());

    User userGet1 = dao.get(user1.getId());
    assertEquals(userGet1.getName(), user1.getName());
    assertEquals(userGet1.getPassword(), user1.getPassword());

    User userGet2 = dao.get(user2.getId());
    assertEquals(userGet2.getName(), user2.getName());
    assertEquals(userGet2.getPassword(), user2.getPassword());

  }

  @Test
  public void count() throws SQLException {

    dao.deleteAll();
    assertEquals(0, dao.getCount());

    dao.add(user1);
    assertEquals(1, dao.getCount());

    dao.add(user2);
    assertEquals(2, dao.getCount());

    dao.add(user3);
    assertEquals(3, dao.getCount());
  }

  @Test
  public void getUserFailure() throws SQLException {

    dao.deleteAll();
    assertEquals(dao.getCount(), 0);

    assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
  }
}