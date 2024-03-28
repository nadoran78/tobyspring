package springbook.user;

import java.sql.SQLException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@SpringBootApplication
public class UserDaoTest {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship5");
        user.setName("홍길동");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + "  등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + "  조회 성공");

//        DaoFactory factory = new DaoFactory();
//        UserDao dao1 = factory.userDao();
//        UserDao dao2 = factory.userDao();
//
//        System.out.println(dao1);
//        System.out.println(dao2);

//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//
//        UserDao dao3 = context.getBean("userDao", UserDao.class);
//        UserDao dao4 = context.getBean("userDao", UserDao.class);
//
//        System.out.println(dao3);
//        System.out.println(dao4);
    }

}
