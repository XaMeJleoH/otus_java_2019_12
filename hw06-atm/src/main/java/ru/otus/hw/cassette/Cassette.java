package ru.otus.hw.cassette;

import ru.otus.hw.ATMException;
import ru.otus.hw.Denomination;

import java.util.List;

public interface Cassette {

    Denomination denomination();

    int maxSize();

    int currentSize();

    void putCash(int count) throws ATMException;

    List<Denomination> pickCash(int count) throws ATMException;

    boolean isHaveSpace(int count);
}
