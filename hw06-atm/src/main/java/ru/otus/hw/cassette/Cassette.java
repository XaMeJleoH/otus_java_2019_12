package ru.otus.hw.cassette;

public abstract class Cassette {
    private int sizeCassette = 50;
    private int currentSize = 20;
    private String currency;

    public int put(int pcs) {
        currentSize = currentSize + pcs;
        return currentSize;
    }

    public int pick(int pcs) {
        currentSize = currentSize - pcs;
        return currentSize;
    }

    public int getSize() {
        return currentSize;
    }

}
