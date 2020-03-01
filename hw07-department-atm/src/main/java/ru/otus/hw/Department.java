package ru.otus.hw;

import ru.otus.hw.atm.ATM;

public interface Department {

    void addATM(ATM atm);

    boolean removeATM(ATM atm);

    long getBalance();
}
