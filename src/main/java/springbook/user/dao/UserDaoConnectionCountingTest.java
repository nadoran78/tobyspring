package springbook.user.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoConnectionCountingTest {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(CountingDaoFactory.class);
    UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);

    //
    // DAO 사용 코드
    //

    CountingConnectionMaker ccm = context.getBean("connectionMaker",
        CountingConnectionMaker.class);
    System.out.println("Connection counter : " + ccm.getCounter());
  }

}
