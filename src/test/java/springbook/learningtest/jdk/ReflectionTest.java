package springbook.learningtest.jdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

  @Test
  public void invokeMethod() throws Exception {
    String name = "Spring";

    // length()
    assertEquals(name.length(), 6);

    Method lengthMethod = String.class.getMethod("length");
    assertEquals((Integer) lengthMethod.invoke(name), 6);

    // charAt()
    assertEquals(name.charAt(0), 'S');

    Method charAtMethod = String.class.getMethod("charAt", int.class);
    assertEquals((Character)charAtMethod.invoke(name, 0), 'S' );
  }
}
