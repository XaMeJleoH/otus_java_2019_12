package ru.otus.hw;

import ru.otus.hw.cassette.Cassette;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {

    private final Map<Denomination, Cassette> cassettes;

    public ATMImpl(Map<Denomination, Cassette> cassettes) {
        this.cassettes = cassettes;
    }

    @Override
    public void putCash(List<Denomination> denominations) throws RuntimeException {
        Map<Denomination, Long> groupingBanknotes = denominations.stream()
                .collect(Collectors.groupingBy(denomination -> denomination, Collectors.counting()));

        groupingBanknotes.keySet().forEach(denomination -> {
            try {
                cassettes.get(denomination).putCash(Math.toIntExact(groupingBanknotes.get(denomination)));
            } catch (ATMException ex) {
                throw new RuntimeException("Was failed added the cash to ATM.", ex);
            }
        });
    }

    @Override
    public List<Denomination> pickCash(int sum) {
        return null;
    }

    @Override
    public long getBalance() {
        return 0;
    }
}
