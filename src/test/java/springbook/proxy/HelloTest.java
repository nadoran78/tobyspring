package springbook.proxy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelloTest {
  @Test
  public void simpleProxy() {
    Hello hello = new HelloTarget();
    assertEquals(hello.sayHello("Toby"), "Hello Toby");
    assertEquals(hello.sayHi("Toby"), "Hi Toby");
    assertEquals(hello.sayThankYou("Toby"), "Thank You Toby");

    Hello proxiedHello = new HelloUppercase(new HelloTarget());
    assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
    assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
    assertEquals(proxiedHello.sayThankYou("Toby"), "THANK YOU TOBY");
  }


}