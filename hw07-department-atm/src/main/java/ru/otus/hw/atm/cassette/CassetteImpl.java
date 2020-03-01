package ru.otus.hw.atm.cassette;

import ru.otus.hw.atm.ATMException;
import ru.otus.hw.atm.Denomination;

import java.util.ArrayList;
import java.util.List;

public class CassetteImpl implements Cassette {

    private final Denomination denomination;
    private final int maxSize;
    private int currentSize;

    public CassetteImpl(Denomination denomination, int maxSize, int currentSize) {
        this.denomination = denomination;
        this.maxSize = maxSize;
        this.currentSize = currentSize;
    }

    @Override
    public Denomination denomination() {
        return denomination;
    }

    @Override
    public int currentSize() {
        return currentSize;
    }

    @Override
    public void putCash(int count) throws ATMException {
        if (isHaveSpace(count)) {
            throw new ATMException("Cassette is full");
        }
        this.currentSize += count;
    }

    @Override
    public List<Denomination> pickCash(int count) throws ATMException {
        if (count > this.currentSize) {
            throw new ATMException("Cassette isn't have needed " + count + " denomination. Where denomination is equals= " + denomination.getDenomination());
        }
        List<Denomination> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(denomination);
        }
        this.currentSize -= count;
        return result;
    }

    @Override
    public boolean isHaveSpace(int count) {
        return maxSize < currentSize + count;
    }
}
