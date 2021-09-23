package ru.geekbraims.javacore.homeworks.lesson6;


import java.util.Arrays;

public class ArrayOperations {

    public static void main(String[] args) {
        int [] temparray = {1, 1, 1, 4, 4, 1, 4, 4};
        System.out.println(isArrayHasFourOrThree(temparray));
    }

    public static int[] NumbersAfterLastFour(int[] array) {
        boolean arrayhasfour = false;
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 4) {
                arrayhasfour = true;
                temp = i;
            }
        }
        if (arrayhasfour && temp != array.length - 1) {
            int[] newArray = Arrays.copyOfRange(array, temp+1, array.length);
            return newArray;
        } else {
            throw new RuntimeException("Массив должен содержать минимум одну четверку");
        }
    }

    public static boolean isArrayHasFourOrThree(int[] array) {
        boolean firstValue = false;
        boolean secondValue = false;
        for (int value : array) {
            if(value == 1) {
                firstValue = true;
            } else if (value == 4) {
                secondValue = true;
            } else {
                return false;
            }
        }
        return firstValue && secondValue;
    }
}
