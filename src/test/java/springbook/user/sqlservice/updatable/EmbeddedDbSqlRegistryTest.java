package springbook.user.sqlservice.updatable;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import springbook.user.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

  EmbeddedDatabase db;

  @Override
  protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
    db = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("classpath:/sql/sqlRegistrySchema.sql")
        .build();

    EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
    embeddedDbSqlRegistry.setDataSource(db);

    return embeddedDbSqlRegistry;
  }

  @AfterEach
  public void tearDown() {
    db.shutdown();
  }

  @Test
  public void transactionalUpdate() {
    checkFindResult("SQL1", "SQL2", "SQL3");

    Map<String, String> sqlmap = new HashMap<>();
    sqlmap.put("KEY1", "Modified1");
    sqlmap.put("KEY9999!@#$", "Modified9999");

    try {
      sqlRegistry.updateSql(sqlmap);
      fail();
    } catch (SqlUpdateFailureException e) {}

    checkFindResult("SQL1", "SQL2", "SQL3");
  }
}
