package ru.otus.hw;

import ru.otus.hw.cassette.*;
import ru.otus.hw.cassette.russian.Russian100;
import ru.otus.hw.cassette.russian.Russian5000;

public class Main {
    public static void main(String[] args) {

        Cassette ruCassette100 = new Russian100();
        Cassette ruCassette5000 = new Russian5000();

        System.out.println("Внесем " + 10 + " купюр в рублевую кассеиу - 100");
        ruCassette100.put(10);
        System.out.println("Внесем " + 3 + " купюр  в рублевую кассеиу - 100");
        ruCassette100.put(3);
        System.out.println("Снимем " + 5 + " купюр  из рублевой кассеты - 100");
        ruCassette100.pick(5);

        System.out.println("My name is ATM. I like rob the clients");

        System.out.println("Внесем " + 1 + "  купюр в рублевую кассеиу - 5000");
        ruCassette5000.put(1);
        System.out.println("Снимем " + 4 + " купюр в рублевую кассеиу - 5000");
        ruCassette5000.pick(4);

        System.out.println("Сейчас в кассетнике " + ruCassette100.getSize() + " купюр");
        System.out.println("Сейчас в кассетнике " + ruCassette5000.getSize() + " купюр");


    }
}
