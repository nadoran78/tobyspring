package springbook.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserServiceTest {

  @Autowired
  UserDao userDao;

  @Autowired
  UserService userService;

  @Autowired
  UserServiceImpl userServiceImpl;

  @Autowired
  PlatformTransactionManager transactionManager;

  @Autowired
  MailSender mailSender;

  List<User> users;

  @BeforeEach
  public void setUp() {
    users = List.of(
        new User("jungchang", "이정창", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
        new User("jiyeong", "권지영", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
        new User("chulgyu", "이철규", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
        new User("cha", "차부장", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
        new User("sunhan", "이선한", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
    );

  }

  @Test
  public void mockUpgradeLevels() throws Exception {
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    UserDao mockUserDao = mock(UserDao.class);
    when(mockUserDao.getAll()).thenReturn(this.users);
    userServiceImpl.setUserDao(mockUserDao);

    MailSender mockMailSender = mock(MailSender.class);
    userServiceImpl.setMailSender(mockMailSender);

    userServiceImpl.upgradeLevels();

    verify(mockUserDao, times(2)).update(any(User.class));
    verify(mockUserDao, times(2)).update(any(User.class));
    verify(mockUserDao).update(users.get(1));
    assertEquals(users.get(1).getLevel(), Level.SILVER);
    verify(mockUserDao).update(users.get(3));
    assertEquals(users.get(3).getLevel(), Level.GOLD);

    ArgumentCaptor<SimpleMailMessage> mailMessageArg =
        ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(mockMailSender, times(2)).send(mailMessageArg.capture());
    List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
    assertEquals(mailMessages.get(0).getTo()[0], users.get(1).getEmail());
    assertEquals(mailMessages.get(1).getTo()[0], users.get(3).getEmail());
  }

  @Test
  @DirtiesContext
  public void upgradeLevels() throws Exception {
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    MockUserDao mockUserDao = new MockUserDao(this.users);
    userServiceImpl.setUserDao(mockUserDao);

    MockMailSender mockMailSender = new MockMailSender();
    userServiceImpl.setMailSender(mockMailSender);

    userServiceImpl.upgradeLevels();

    List<User> updated = mockUserDao.getUpdated();
    assertEquals(updated.size(), 2);
    checkUserAndLevel(updated.get(0), "jiyeong", Level.SILVER);
    checkUserAndLevel(updated.get(1), "cha", Level.GOLD);


    List<String> request = mockMailSender.getRequests();
    assertEquals(request.size(), 2);
    assertEquals(request.get(0), users.get(1).getEmail());
    assertEquals(request.get(1), users.get(3).getEmail());
  }

  private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
    assertEquals(updated.getId(), expectedId);
    assertEquals(updated.getLevel(), expectedLevel);
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
    } else {
      assertEquals(userUpdate.getLevel(), user.getLevel());
    }
  }

  private void checkLevel(User user, Level expectedLevel) {
    User userUpdate = userDao.get(user.getId());
    assertEquals(userUpdate.getLevel(), expectedLevel);
  }

  @Test
  public void add() {
    userDao.deleteAll();

    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
    assertEquals(userWithoutLevelRead.getLevel(), userWithoutLevel.getLevel());
  }

  @Test
  public void upgradeAllOrNothing() throws Exception {
    TestUserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao);
    testUserService.setMailSender(mailSender);

    UserServiceTx txUserService = new UserServiceTx();
    txUserService.setTransactionManager(transactionManager);
    txUserService.setUserService(testUserService);

    userDao.deleteAll();
    for(User user : users) userDao.add(user);

    try {
      txUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    }
    catch (TestUserServiceException e) {
      System.out.println("error 발생");
    }

    checkLevelUpgraded(users.get(1), false);
  }

  static class TestUserService extends UserServiceImpl {
    private String id;

    private TestUserService(String id) {
      this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) throw new TestUserServiceException();
      super.upgradeLevel(user);
    }
  }

  static class TestUserServiceException extends RuntimeException {

  }

  static class MockMailSender implements MailSender {
    private List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
      return requests;
    }

    public void send(SimpleMailMessage mailMessage) throws MailException {
      requests.add(mailMessage.getTo()[0]);
    }

    public void send(SimpleMailMessage[] mailMessage) throws MailException {

    }
  }

  static class MockUserDao implements UserDao {
    private List<User> users;
    private List<User> updated = new ArrayList<>();

    private MockUserDao(List<User> users) {
      this.users = users;
    }

    public List<User> getUpdated() {
      return this.updated;
    }

    public List<User> getAll() {
      return this.users;
    }

    public void update(User user) {
      updated.add(user);
    }

    public void add(User user) {throw new UnsupportedOperationException();}
    public void deleteAll() {throw new UnsupportedOperationException();}
    public User get(String id) {throw new UnsupportedOperationException();}
    public int getCount() {throw new UnsupportedOperationException();}
  }
}