package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.config.AppContext;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppContext.class})
class UserDaoTest {

  @Qualifier("userDaoJdbc")
  @Autowired
  private UserDao dao;

  @Autowired
  private DataSource dataSource;
  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {

    this.user1 = new User("test2", "홍길동1", "password1", Level.BASIC, 1, 0);
    this.user2 = new User("test3", "홍길동2", "password2", Level.SILVER, 55, 10);
    this.user3 = new User("test1", "홍길동3", "password3", Level.GOLD, 100, 40);
  }

  @Test
  public void addAndGet() throws SQLException {

    dao.deleteAll();
    assertEquals(0, dao.getCount());

    dao.add(user1);
    dao.add(user2);
    assertEquals(2, dao.getCount());

    User userGet1 = dao.get(user1.getId());
    checkSameUser(userGet1, user1);

    User userGet2 = dao.get(user2.getId());
    checkSameUser(userGet2, user2);

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

  @Test
  public void getAll() throws SQLException {
    dao.deleteAll();

    List<User> users0 = dao.getAll();
    assertEquals(users0.size(), 0);

    dao.add(user1); // Id : test2
    List<User> users1 = dao.getAll();
    assertEquals(users1.size(), 1);
    checkSameUser(user1, users1.get(0));

    dao.add(user2); // Id : test3
    List<User> users2 = dao.getAll();
    assertEquals(users2.size(), 2);
    checkSameUser(user1, users2.get(0));
    checkSameUser(user2, users2.get(1));

    dao.add(user3); // Id: test1
    List<User> users3 = dao.getAll();
    assertEquals(users3.size(), 3);
    checkSameUser(user3, users3.get(0));
    checkSameUser(user1, users3.get(1));
    checkSameUser(user2, users3.get(2));

  }

  private void checkSameUser(User user1, User user2) {
    assertEquals(user1.getId(), user2.getId());
    assertEquals(user1.getName(), user2.getName());
    assertEquals(user1.getPassword(), user2.getPassword());
    assertEquals(user1.getLevel(), user2.getLevel());
    assertEquals(user1.getLogin(), user2.getLogin());
    assertEquals(user1.getRecommend(), user2.getRecommend());
  }

  @Test
  public void duplicateKey() {
    dao.deleteAll();
    ;

    dao.add(user1);
    assertThrows(DuplicateKeyException.class, () -> dao.add(user1));
  }

  @Test
  public void sqlExceptionTranslate() {
    dao.deleteAll();

    try {
      dao.add(user1);
      dao.add(user1);
    } catch (DuplicateKeyException ex) {
      SQLException sqlEx = (SQLException) ex.getRootCause();
      SQLExceptionTranslator set =
          new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

      assertTrue(set.translate(null, null, sqlEx) instanceof DuplicateKeyException);
    }
  }

  @Test
  public void update() {
    dao.deleteAll();

    dao.add(user1);
    dao.add(user2);

    user1.setName("권지영");
    user1.setPassword("spring");
    user1.setLevel(Level.GOLD);
    user1.setLogin(100);
    user1.setRecommend(999);
    dao.update(user1);

    User user1update = dao.get(user1.getId());
    checkSameUser(user1, user1update);
    User user2same = dao.get(user2.getId());
    checkSameUser(user2, user2same);
  }
}