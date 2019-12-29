package ru.otus.hw;

import com.google.common.collect.ComparisonChain;

public class HelloOtus {

    public static void main(String... args) {
        int num1 = 1;
        int num2 = 10;

        System.out.println(ComparisonChain.start()
                .compare(num1, num2)
                .result());
    }
}
