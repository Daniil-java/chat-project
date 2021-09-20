package ru.geekbraims.javacore.homeworks.lesson6;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class ArrayOperationsTest extends TestCase {
    private ArrayOperations arrayOperations;



    @Before
    public void init() {
        arrayOperations = new ArrayOperations();
    }



    @Test
    public void testNumbersAfterLastFour1() {
        int [] temparray = {1, 4, 2, 1, 4, 4, 5};
        int [] expected = {5};
        int [] testarray1 = arrayOperations.NumbersAfterLastFour(temparray);
        assertEquals(Arrays.toString(expected), Arrays.toString(testarray1));
    }

    @Test
    public void testNumbersAfterLastFour2() {
        int [] temparray = {1, 4, 2, 1, 1, 1, 1};
        int [] expected = {2, 1, 1, 1, 1};
        int [] testarray1 = arrayOperations.NumbersAfterLastFour(temparray);
        assertEquals(Arrays.toString(expected), Arrays.toString(testarray1));
    }

    @Test
    public void testNumbersAfterLastFour3() {
        int [] temparray = {4, 1, 2, 1, 0, 0, 5};
        int [] expected = {1, 2, 1, 0, 0, 5};
        int [] testarray1 = arrayOperations.NumbersAfterLastFour(temparray);
        assertEquals(Arrays.toString(expected), Arrays.toString(testarray1));
    }

    @Test
    public void testNumbersAfterLastFour4() {
        try {
            int [] temparray = {1, 1, 2, 1, 1, 1, 2};
            int [] expected = {};
            int [] testarray1 = arrayOperations.NumbersAfterLastFour(temparray);
            assertEquals(Arrays.toString(expected), Arrays.toString(testarray1));
        } catch (RuntimeException exception) {
            System.out.println("Массив должен содержать минимум одну четверку");
        }
    }

    @Test
    public void testisArrayHasFourOrThree5() {
        int [] temparray = {1, 1, 1, 4, 4, 1, 4, 4};
        assertEquals(true, arrayOperations.isArrayHasFourOrThree(temparray));
    }

    @Test
    public void testisArrayHasFourOrThree6() {
        int [] temparray = {1, 1, 1, 1, 1, 1};
        assertEquals(false, arrayOperations.isArrayHasFourOrThree(temparray));
    }

    @Test
    public void testisArrayHasFourOrThree7() {
        int [] temparray = {4,4,4,4};
        assertEquals(false, arrayOperations.isArrayHasFourOrThree(temparray));
    }

    @Test
    public void testisArrayHasFourOrThree8() {
        int [] temparray = {1, 4, 4, 1, 1, 4, 3};
        assertEquals(false, arrayOperations.isArrayHasFourOrThree(temparray));
    }

}