package ru.otus.hw.atm.cassette;

import ru.otus.hw.atm.ATMException;
import ru.otus.hw.atm.Denomination;

import java.util.List;

public interface Cassette {

    Denomination denomination();

    int currentSize();

    void putCash(int count) throws ATMException;

    List<Denomination> pickCash(int count) throws ATMException;

    boolean isHaveSpace(int count);

    int maxSize();
}
