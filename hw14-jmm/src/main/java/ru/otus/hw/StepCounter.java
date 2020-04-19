package ru.otus.hw;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StepCounter {
    private int leftCount, rightCount = 0;
    private AtomicBoolean onSomeSide = new AtomicBoolean(false);
    private static final int LIMIT = 10;

    public static void main(String[] args) throws InterruptedException {
        StepCounter counter = new StepCounter();
        counter.go();
    }

    private void loop() {
        while (leftCount != LIMIT && rightCount != LIMIT){
            forwardStep();
        }
    }

    private void forwardStep() {
        if(onSomeSide.compareAndSet(true, false)) {
            leftCount++;
            log.info("L leg, count= {}", leftCount);
            sleep(1);
        } else if (onSomeSide.compareAndSet(false, true)) {
            rightCount++;
            log.info("R leg, count= {}", rightCount);
            sleep(2);
        }
    }

    private void go() throws InterruptedException {
        Thread thread1 = new Thread(this::loop);
        Thread thread2 = new Thread(this::loop);

        thread1.start();
        thread2.start();

        System.out.println("A baby start to walk");
        thread1.join();
        thread2.join();
        System.out.println("A baby finished walk");
    }

    private static void sleep(int duration) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
