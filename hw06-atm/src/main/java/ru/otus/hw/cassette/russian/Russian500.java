package ru.otus.hw.cassette.russian;

import ru.otus.hw.cassette.Cassette;

public class Russian500 extends Cassette {

    @Override
    public int put(int pcs) {
        System.out.println("500 Rub added");
        return super.put(pcs);
    }

    @Override
    public int pick(int pcs) {
        System.out.println("500 Rub deleted");
        return super.pick(pcs);
    }
}
