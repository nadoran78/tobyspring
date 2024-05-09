package springbook.learningtest.jdk.oxm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/OxmTest-context.xml")
public class OxmTest {
  @Autowired
  Unmarshaller unmarshaller;

  @Test
  public void unmarshallSqlMap() throws XmlMappingException, IOException {
    Source xmlSource = new StreamSource(
        getClass().getResourceAsStream("/sqlmap-test.xml"));

    Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

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
