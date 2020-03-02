package ru.otus.hw.atm;

import ru.otus.hw.atm.utils.Listener;

import java.util.List;

public interface ATM extends Listener {

    void putCash(List<Denomination> denominations) throws ATMException;

    List<Denomination> pickCash(int sum) throws ATMException;

    long getBalance();
}
