package ru.geekbraims.javacore.homeworks.lesson7;

import java.lang.reflect.Method;

public class MainApp {

    public static void main(String[] args) {
        start(Tester.class);
    }

    private static void start(Class testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        try {
            int afterSuiteCounter = 0;
            int beforeSuiteCounter = 0;
            for (Method o : methods) {
                if (o.getAnnotation(BeforeSuite.class) != null) {
                    if (beforeSuiteCounter == 1) {
                        throw new RuntimeException("@BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре.");
                    }
                    System.out.println(o);
                    beforeSuiteCounter++;
                }
            }
            for (int priority = 1; priority < 11; priority++) {
                for (Method o : methods) {
                    if (o.getAnnotation(Test.class) != null) {
                        Test test = o.getAnnotation(Test.class);
                        if (test.value() == priority) {
                            System.out.println(o);
                            System.out.println("test_priority: " + test.value());
                        }
                    }
                }
            }
            for (Method o : methods) {
                if (o.getAnnotation(AfterSuite.class) != null) {
                    if (afterSuiteCounter == 1) {
                        throw new RuntimeException("@BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре.");
                    }
                    System.out.println(o);
                    afterSuiteCounter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
