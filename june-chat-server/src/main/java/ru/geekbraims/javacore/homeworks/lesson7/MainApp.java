package ru.geekbraims.javacore.homeworks.lesson7;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainApp {

    public static void main(String[] args) {
        start(Tester.class);
    }

    private static void start(Class testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        Method afterSuiteMth = null;    //Контейнеры для методов After/BeforeSuite
        Method beforeSuiteMth = null;
        ArrayList<Method> list = new ArrayList<Method>();
        try {
            for (Method o : methods) {
                o.setAccessible(true);
                if(o.isAnnotationPresent(Test.class)) {
                    list.add(o);
                } else if(o.isAnnotationPresent(BeforeSuite.class)) {
                    if(beforeSuiteMth != null) {
                        throw new RuntimeException("@BeforeSuite должен присутствовать в единственном экземпляре.");
                    } else {
                        beforeSuiteMth = o;
                    }
                } else if (o.isAnnotationPresent(AfterSuite.class)) {
                    if(afterSuiteMth != null) {
                        throw new RuntimeException("@AfterSuite должен присутствовать в единственном экземпляре.");
                    } else {
                        afterSuiteMth = o;
                    }
                }
            }
            list.sort((o1, o2) -> (int) (o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority()));   //Сортировка по приоритету

            Object testobj = testClass.newInstance();
            if (beforeSuiteMth != null) {
                beforeSuiteMth.invoke(testobj);
            }
            for (Method o : list) {
                o.invoke(testobj);
            }
            if (afterSuiteMth != null) {
                afterSuiteMth.invoke(testobj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
