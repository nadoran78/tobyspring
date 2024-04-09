package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-applicationContext.xml")
class UserDaoTest {

  @Autowired
  private UserDao dao;
  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {

    this.user1 = new User("test2", "홍길동1", "password1");
    this.user2 = new User("test3", "홍길동2", "password2");
    this.user3 = new User("test1", "홍길동3", "password3");
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
  }
}