package springbook.learningtest.spring.pointcut;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

class PointcutExpressionTest {

  @Test
  public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public int "
        + "springbook.learningtest.spring.pointcut.Target.minus(int,int) "
        + "throws java.lang.RuntimeException)");

    // Target.minus()
    assertTrue(pointcut.getClassFilter().matches(Target.class) &&
        pointcut.getMethodMatcher().matches(
            Target.class.getMethod("minus", int.class, int.class), null));

    // Target.plus()
    assertFalse(pointcut.getClassFilter().matches(Target.class) &&
        pointcut.getMethodMatcher().matches(
            Target.class.getMethod("plus", int.class, int.class), null));

    // Bean.method()
    assertFalse(pointcut.getClassFilter().matches(Bean.class) &&
        pointcut.getMethodMatcher().matches(
            Target.class.getMethod("method"), null));
  }

  public void pointcutMatches(String expression, Boolean expected, Class<?> clazz,
      String methodName, Class<?>... args) throws Exception {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(expression);

    assertEquals(pointcut.getClassFilter().matches(clazz)
        && pointcut.getMethodMatcher().matches(
        clazz.getMethod(methodName, args), null), expected);
  }

  public void targetClassPointcutMatches(String expression, boolean... expected)
      throws Exception {
    pointcutMatches(expression, expected[0], Target.class, "hello");
    pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
    pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
    pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
    pointcutMatches(expression, expected[4], Target.class, "method");
    pointcutMatches(expression, expected[5], Bean.class, "method");
  }

  @Test
  public void pointcut() throws Exception {
    targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
    targetClassPointcutMatches("execution(* hello(..))", true, true, false, false, false,
        false);
    targetClassPointcutMatches("execution(* hello())", true, false, false, false, false,
        false);
    targetClassPointcutMatches("execution(* hello(String))", false, true, false, false,
        false, false);
    targetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true,
        true);
    targetClassPointcutMatches("execution(* *(int,int))", false, false, true, true, false,
        false);
    targetClassPointcutMatches("execution(* *())", true, false, false, false, true, true);
    targetClassPointcutMatches(
        "execution(* springbook.learningtest.spring.pointcut.Target.*(..))", true, true,
        true, true, true, false);
    targetClassPointcutMatches("execution(* springbook.learningtest.spring.pointcut.*.*(..))", true, true, true, true, true, true);
    targetClassPointcutMatches("execution(* springbook.learningtest.spring.pointcut..*.*(..))", true, true, true, true, true, true);
    targetClassPointcutMatches("execution(* springbook..*.*(..))", true, true, true, true, true, true);
    targetClassPointcutMatches("execution(* com..*.*(..))", false, false, false, false, false, false);
    targetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
    targetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
    targetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
    targetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
    targetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);
    targetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true);
    targetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false);
    targetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true);


  }
}