package springbook.user.sqlservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractUpdatableSqlRegistryTest {

  protected UpdatableSqlRegistry sqlRegistry;

  @BeforeEach
  public void setUp() {
    sqlRegistry = createUpdatableSqlRegistry();
    sqlRegistry.registerSql("KEY1", "SQL1");
    sqlRegistry.registerSql("KEY2", "SQL2");
    sqlRegistry.registerSql("KEY3", "SQL3");
  }

  abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

  @Test
  public void find() {
    checkFindResult("SQL1", "SQL2", "SQL3");
  }

  protected void checkFindResult(String expected1, String expected2, String expected3) {
    assertEquals(sqlRegistry.findSql("KEY1"), expected1);
    assertEquals(sqlRegistry.findSql("KEY2"), expected2);
    assertEquals(sqlRegistry.findSql("KEY3"), expected3);
  }

  @Test
  public void unknownKey() {
    assertThrows(SqlNotFoundException.class, () -> sqlRegistry.findSql("SQL9999!@#$"));
  }

  @Test
  public void updateSingle() {
    sqlRegistry.updateSql("KEY2", "Modified2");
    checkFindResult("SQL1", "Modified2", "SQL3");
  }

  @Test
  public void updateMulti() {
    Map<String, String> sqlmap = new HashMap<>();
    sqlmap.put("KEY1", "Modified1");
    sqlmap.put("KEY3", "Modified3");

    sqlRegistry.updateSql(sqlmap);
    checkFindResult("Modified1", "SQL2", "Modified3");
  }

  @Test
  public void updateWithNotExistingKey() {
    assertThrows(SqlUpdateFailureException.class,
        () -> sqlRegistry.updateSql("SQL999!@#$", "Modified2"));
  }

}
