package ru.otus.hw;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class StepCounter {
    private int leftCount, rightCount = 0;
    private AtomicBoolean onSomeSide = new AtomicBoolean();
    private static final int LIMIT = 10;

    public static void main(String[] args) throws InterruptedException {
        StepCounter counter = new StepCounter();
        counter.go();
    }

    private void loop() {
        for (int i = 0; i < LIMIT; i++) {
            isStep(true);
        }

        for (int i = 1; i < LIMIT; i++) {
            isStep(false);
        }
    }

    private void isStep(boolean forward) {
        if (onSomeSide.compareAndSet(true, false)) {
            synchronized (this) {
                leftIncrOrDecr(forward);
                log.info("L leg, count= {}", leftCount);
                sleep(2);
            }
        } else if (onSomeSide.compareAndSet(false, true)) {
            synchronized (this) {
                rightIncrOrDecr(forward);
                log.info("R leg, count= {}", rightCount);
                sleep(1);
            }
        }
    }

    private void rightIncrOrDecr(boolean forward) {
        if (forward) {
            rightCount++;
        } else {
            rightCount--;
        }
    }

    private void leftIncrOrDecr(boolean forward) {
        if (forward) {
            leftCount++;
        } else {
            leftCount--;
        }
    }


    private void go() throws InterruptedException {
        Thread thread1 = new Thread(this::loop);
        Thread thread2 = new Thread(this::loop);

        thread1.start();
        thread2.start();

        System.out.println("Baby start to walk");
        thread1.join();
        thread2.join();
        System.out.println("Baby finished walk");
    }

    private static void sleep(int duration) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
