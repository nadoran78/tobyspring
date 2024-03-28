package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.domain.User;

class UserDaoTest {

  @Test
  public void addAndGet() throws SQLException {
    ApplicationContext context = new GenericXmlApplicationContext(
        "applicationContext.xml");

    UserDao dao = context.getBean("userDao", UserDao.class);

    User user = new User();
    user.setId("test1");
    user.setName("hello");
    user.setPassword("lucky");

    dao.add(user);

    System.out.println(user.getId() + "  등록 성공");

    User user2 = dao.get(user.getId());

    assertEquals(user2.getName(), user.getName());
    assertEquals(user2.getPassword(), user.getPassword());

//    if (!user.getName().equals(user2.getName())) {
//      System.out.println("테스트 실패 (name)");
//    } else if (!user.getPassword().equals(user2.getPassword())) {
//      System.out.println("테스트 실패 (password)");
//    } else {
//      System.out.println("조회 테스트 성공");
//    }
  }

}