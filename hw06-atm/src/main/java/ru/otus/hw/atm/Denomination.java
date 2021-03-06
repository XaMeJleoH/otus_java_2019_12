package ru.otus.hw.atm;

public enum Denomination {

    RUR_FIFTY(50),
    RUR_ONE_HUNDRED(100),
    RUR_FIVE_HUNDRED(500),
    RUR_ONE_THOUSAND(1000),
    RUR_TWO_THOUSAND(2000),
    RUR_FIVE_THOUSAND(5000);

    private int denomination;

    Denomination(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "denomination='" + denomination + '\'' +
                '}';
    }


}
