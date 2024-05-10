package springbook.user.sqlservice;

public class SqlAdminService {
  private UpdatableSqlRegistry updatableSqlRegistry;

  public void setUpdatableSqlRegistry(UpdatableSqlRegistry updatableSqlRegistry) {
    this.updatableSqlRegistry = updatableSqlRegistry;
  }

  public void updateEventListener(String key, String value) {
    this.updatableSqlRegistry.updateSql(key, value);
  }
}
