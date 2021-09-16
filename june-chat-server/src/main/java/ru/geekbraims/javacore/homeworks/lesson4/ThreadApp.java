package ru.geekbraims.javacore.homeworks.lesson4;

public class ThreadApp {
/*
Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз
(порядок – ABСABСABС). Используйте wait/notify/notifyAll.
 */
    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        ThreadApp threadApp = new ThreadApp();
        Thread thread1 = new Thread(() -> {
            threadApp.printA();
        });
        Thread thread2 = new Thread(() -> {
            threadApp.printB();
        });
        Thread thread3 = new Thread(() -> {
            threadApp.printC();
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }
    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        mon.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        mon.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        mon.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
