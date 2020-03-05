package ru.otus.hw.atm;

class Transaction {
    Denomination denomination;
    int count;

    Transaction(Denomination denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }
}
