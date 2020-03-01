package ru.otus.hw.atm;

import java.util.List;

public interface ATM {

    void putCash(List<Denomination> denominations) throws ATMException;

    List<Denomination> pickCash(int sum) throws ATMException;

    long getBalance();
}
