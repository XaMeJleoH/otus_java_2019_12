package ru.otus.hw.atm;

import ru.otus.hw.atm.cassette.Cassette;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {

    private final TreeMap<Denomination, Cassette> cassetteMap;

    ATMImpl(List<Cassette> cassetteList) {
        this.cassetteMap = new TreeMap<>((o1, o2) -> o2.getDenomination() - o1.getDenomination());
        cassetteList.forEach(cassette -> this.cassetteMap.put(cassette.denomination(), cassette));
    }

    @Override
    public void putCash(List<Denomination> denominations) throws RuntimeException, ATMException {
        Map<Denomination, Long> denominationMap = denominations.stream()
                .collect(Collectors.groupingBy(denomination -> denomination, Collectors.counting()));

        if (denominationMap.keySet().stream()
                .anyMatch(denomination -> cassetteMap.get(denomination)
                        .isHaveSpace(Math.toIntExact(denominationMap.get(denomination))))) {
            throw new ATMException("Can't add the cash, because cassette is full");
        }

        denominationMap.keySet().forEach(denomination -> {
            try {
                cassetteMap.get(denomination).putCash(Math.toIntExact(denominationMap.get(denomination)));
            } catch (ATMException ex) {
                throw new RuntimeException("Was failed put the cash to ATM.", ex);
            }
        });
    }

    @Override
    public List<Denomination> pickCash(int sum) throws RuntimeException, ATMException {
        List<Transaction> transactions = new ArrayList<>();
        for (Denomination denomination : cassetteMap.keySet()) {
            int count = sum / cassetteMap.get(denomination).denomination().getDenomination();
            while (cassetteMap.get(denomination).currentSize() < count && count != 0) {
                count--;
            }
            if (count > 0) {
                transactions.add(new Transaction(denomination, count));
                sum -= count * denomination.getDenomination();
            }
        }

        List<Denomination> cashList = new ArrayList<>();
        transactions.forEach(transaction -> {
            try {
                cashList.addAll(cassetteMap.get(transaction.denomination).pickCash(transaction.count));
            } catch (ATMException ex) {
                throw new RuntimeException("Was failed pick the cash to ATM.", ex);
            }
        });
        return cashList;
    }

    @Override
    public long getBalance() {
        return cassetteMap.values().stream()
                .mapToLong(cassette -> cassette.currentSize() * cassette.denomination().getDenomination())
                .sum();
    }
}
