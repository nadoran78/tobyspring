package springbook.learningtest.jdk.jaxb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class JaxbTest {
  @Test
  public void readSqlmap() throws JAXBException, IOException {
    String contextPath = Sqlmap.class.getPackage().getName();
    JAXBContext context = JAXBContext.newInstance(contextPath);

    Unmarshaller unmarshaller = context.createUnmarshaller();

    Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
        getClass().getResourceAsStream("/sqlmap-test.xml"));
    List<SqlType> sqlList = sqlmap.getSql();

    assertEquals(sqlList.size(), 3);
    assertEquals(sqlList.get(0).getKey(), "add");
    assertEquals(sqlList.get(0).getValue(), "insert");
    assertEquals(sqlList.get(1).getKey(), "get");
    assertEquals(sqlList.get(1).getValue(), "select");
    assertEquals(sqlList.get(2).getKey(), "delete");
    assertEquals(sqlList.get(2).getValue(), "delete");
  }

}
