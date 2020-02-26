package ru.otus.hw.cassette.russian;

import ru.otus.hw.cassette.Cassette;

public class Russian1000 extends Cassette {

    @Override
    public int put(int pcs) {
        System.out.println("1000 Rub added");
        return super.put(pcs);
    }

    @Override
    public int pick(int pcs) {
        System.out.println("1000 Rub deleted");
        return super.pick(pcs);
    }
}
