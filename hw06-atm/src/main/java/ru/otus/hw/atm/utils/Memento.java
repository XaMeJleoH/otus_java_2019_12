package ru.otus.hw.atm.utils;

import ru.otus.hw.atm.Denomination;
import ru.otus.hw.atm.cassette.Cassette;
import ru.otus.hw.atm.cassette.CassetteImpl;

import java.util.TreeMap;

public class Memento {
    private final TreeMap<Denomination, Cassette> cassettesState;

    public Memento(TreeMap<Denomination, Cassette> cassettes) {
        this.cassettesState = cloneCassettes(cassettes);
    }

    public TreeMap<Denomination, Cassette> getCassettesState() {
        return cassettesState;
    }

    private TreeMap<Denomination, Cassette> cloneCassettes(TreeMap<Denomination, Cassette> cassettes) {
        TreeMap<Denomination, Cassette> result = new TreeMap<>((o1, o2) -> o2.getDenomination() - o1.getDenomination());
        cassettes.values().forEach(cassette -> result
                .put(cassette.denomination(), new CassetteImpl(cassette.denomination(), cassette.maxSize(), cassette.currentSize())));
        return result;
    }
}
