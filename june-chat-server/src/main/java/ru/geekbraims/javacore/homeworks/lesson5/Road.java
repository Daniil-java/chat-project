package ru.geekbraims.javacore.homeworks.lesson5;

import java.util.concurrent.BrokenBarrierException;

public class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            if(this.length == 40) {
                System.out.println(c.getName() + " закончил этап: " + description);
                if(MainClass.winner.writeLock().tryLock()) {
                    System.out.println(c.getName() + " WIN");
                }
                MainClass.finish.countDown();
            } else {
                MainClass.road.await();
                System.out.println(c.getName() + " закончил этап: " + description);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
