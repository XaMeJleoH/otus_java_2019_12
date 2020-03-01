package ru.otus.hw.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.atm.cassette.Cassette;
import ru.otus.hw.atm.cassette.CassetteImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMImplTest {

    private ATMImpl atm;
    private static final int SIZE_RUR_ONE_HUNDRED = 20;
    private static final int SIZE_RUR_ONE_THOUSAND = 20;
    private static final int SIZE_RUR_FIVE_THOUSAND = 50;


    @BeforeEach
    void setUp() {
        List<Cassette> cassettes = new ArrayList<>();
        cassettes.add(new CassetteImpl(Denomination.RUR_ONE_HUNDRED, SIZE_RUR_ONE_HUNDRED, 0));
        cassettes.add(new CassetteImpl(Denomination.RUR_ONE_THOUSAND, SIZE_RUR_ONE_THOUSAND, 0));
        cassettes.add(new CassetteImpl(Denomination.RUR_FIVE_THOUSAND, SIZE_RUR_FIVE_THOUSAND, 0));
        atm = new ATMImpl(cassettes);
    }

    @DisplayName("Внесение наличных")
    @Test
    void putCash() throws ATMException {
        List<Denomination> denominations = initialize();
        atm.putCash(denominations);
        int sum = Denomination.RUR_ONE_HUNDRED.getDenomination() * SIZE_RUR_ONE_HUNDRED
                + Denomination.RUR_ONE_THOUSAND.getDenomination() * SIZE_RUR_ONE_THOUSAND
                + Denomination.RUR_FIVE_THOUSAND.getDenomination() * SIZE_RUR_FIVE_THOUSAND;
        assertEquals(sum, atm.getBalance());
    }

    @DisplayName("Получение наличных")
    @Test
    void pickCash() throws ATMException {
        List<Denomination> denominations = initialize();
        atm.putCash(denominations);

        List<Denomination> cash = atm.pickCash(28700);
        Map<Denomination, Long> denominationMap = cash.stream()
                .collect(Collectors.groupingBy(denomination -> denomination, Collectors.counting()));
        assertEquals(5, denominationMap.get(Denomination.RUR_FIVE_THOUSAND));
        assertEquals(3, denominationMap.get(Denomination.RUR_ONE_THOUSAND));
        assertEquals(7, denominationMap.get(Denomination.RUR_ONE_HUNDRED));
    }

    @DisplayName("Получение баланса")
    @Test
    void getBalance() throws ATMException {
        assertEquals(0, atm.getBalance());

        List<Denomination> denominations = initialize();
        atm.putCash(denominations);

        assertEquals(272000, atm.getBalance());
        atm.pickCash(28700);
        assertEquals(243300, atm.getBalance());
    }

    private static List<Denomination> initialize() {
        List<Denomination> denominations = new ArrayList<>();

        for (int i = 0; i < SIZE_RUR_ONE_HUNDRED; i++) {
            denominations.add(Denomination.RUR_ONE_HUNDRED);
        }

        for (int i = 0; i < SIZE_RUR_ONE_THOUSAND; i++) {
            denominations.add(Denomination.RUR_ONE_THOUSAND);
        }

        for (int i = 0; i < SIZE_RUR_FIVE_THOUSAND; i++) {
            denominations.add(Denomination.RUR_FIVE_THOUSAND);
        }

        return denominations;
    }

}