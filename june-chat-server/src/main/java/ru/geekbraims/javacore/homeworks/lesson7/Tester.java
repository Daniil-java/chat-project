package ru.geekbraims.javacore.homeworks.lesson7;

public class Tester {

    @BeforeSuite
    public void beforeTest() { System.out.println("BEFORE TEST"); }

    @AfterSuite
    public void afterTest() {
        System.out.println("AFTER TEST");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("TEST. PRIORITY: 1");
    }

    @Test(priority = 2)
    public void test2() {
        System.out.println("TEST. PRIORITY: 2");
    }

    @Test(priority = 3)
    private void test3() {
        System.out.println("PRIVATE TEST. PRIORITY: 3");
    }

    @Test(priority = 4)
    public void test4() {
        System.out.println("TEST. PRIORITY: 4");
    }

    @Test(priority = 4)
    public void test5() {
        System.out.println("TEST. PRIORITY: 44");
    }

    @Test(priority = 4)
    public void test6() {
        System.out.println("TEST. PRIORITY: 444");
    }

    @Test(priority = 7)
    public void test7() {
        System.out.println("TEST. PRIORITY: 7");
    }

    @Test(priority = 8)
    public void test8() {
        System.out.println("TEST. PRIORITY: 8");
    }

    @Test(priority = 9)
    public void test9() {
        System.out.println("TEST. PRIORITY: 9");
    }
}
