package ru.otus.hw;

import ru.otus.hw.atm.ATM;
import ru.otus.hw.atm.utils.Listener;

import java.util.Collection;

public class DepartmentImpl implements Department{

    private final Collection<ATM> atmList;

    public DepartmentImpl(Collection<ATM> atmList) {
        this.atmList = atmList;
    }

    @Override
    public void addATM(ATM atm) {
        atmList.add(atm);
    }

    @Override
    public boolean removeATM(ATM atm) {
        return atmList.remove(atm);
    }

    @Override
    public long getBalance() {
        return atmList.stream().mapToLong(ATM::getBalance).sum();
    }

    @Override
    public void printBalanceOnScreen() {
        atmList.forEach(Listener::printBalanceOnScreen);
    }

    @Override
    public void resetATM() {
        atmList.forEach(Listener::resetATM);
    }
}
