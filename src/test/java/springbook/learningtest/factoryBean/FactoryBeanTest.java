package springbook.learningtest.factoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/FactoryBeanTest-context.xml")
class FactoryBeanTest {
  @Autowired
  ApplicationContext context;

  @Test
  public void getMessageFromFactoryBean() {
    Object message = context.getBean("message");
    assertTrue(message instanceof Message);
    assertEquals(((Message)message).getText(), "Factory Bean");
  }

  @Test
  public void getFactoryBean() throws Exception {
    Object factory = context.getBean("&message");
    assertTrue(factory instanceof MessageFactoryBean);
  }
}