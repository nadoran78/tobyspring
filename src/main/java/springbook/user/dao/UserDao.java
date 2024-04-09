package springbook.user.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.exception.DuplicateUserIdException;
import springbook.user.domain.User;

public class UserDao {
  private final RowMapper<User> userMapper = new RowMapper<User>() {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      return user;
    }
  };

  private JdbcTemplate jdbcTemplate;

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }


  public void add(final User user) throws DuplicateUserIdException {
    this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
        user.getId(), user.getName(), user.getPassword());
//    try {
//      this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
//          user.getId(), user.getName(), user.getPassword());
//    } catch (SQLException e) {
//      if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
//        throw new DuplicateUserIdException(e);
//      else
//        throw new RuntimeException(e);
//    }

  }

  public User get(String id) {
    return this.jdbcTemplate.queryForObject("select * from users where id = ?",
        new Object[]{id}, userMapper);
  }

  public void deleteAll() {
    this.jdbcTemplate.update("delete from users");
  }

  public int getCount() {
    return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
  }

  public List<User> getAll() {
    return this.jdbcTemplate.query("select * from users order by id", userMapper);
  }

}
