package springbook.user.sqlservice;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.InputStream;
import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class JaxbXmlSqlReader implements SqlReader{
  private static final String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";

  private String sqlmapFile = DEFAULT_SQLMAP_FILE;

  public void setSqlmapFile(String sqlmapFile) {
    this.sqlmapFile = sqlmapFile;
  }
  @Override
  public void read(SqlRegistry sqlRegistry) {
    String contextPath = Sqlmap.class.getPackage().getName();
    try {
      JAXBContext context = JAXBContext.newInstance(contextPath);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
      Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

      for (SqlType sql : sqlmap.getSql()) {
        sqlRegistry.registerSql(sql.getKey(), sql.getValue());
      }
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
