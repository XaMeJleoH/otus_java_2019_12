package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.atm.ATM;
import ru.otus.hw.atm.ATMException;
import ru.otus.hw.atm.ATMImpl;
import ru.otus.hw.atm.Denomination;
import ru.otus.hw.atm.cassette.Cassette;
import ru.otus.hw.atm.cassette.CassetteImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentImplTest {

    private DepartmentImpl department;
    private Collection<ATM> ATMList;
    private static final int SIZE_RUR_FIFTY = 20;
    private static final int SIZE_RUR_ONE_HUNDRED = 20;
    private static final int SIZE_RUR_FIVE_HUNDRED = 20;
    private static final int SIZE_RUR_ONE_THOUSAND = 20;
    private static final int SIZE_RUR_TWO_THOUSAND = 20;
    private static final int SIZE_RUR_FIVE_THOUSAND = 50;

    private static final int countOfAtm = 4;


    @BeforeEach
    void setUp() {
        ATMList = new ArrayList<>();
        List<Cassette> cassettes = initializeCassettes();

        for (int i=0; i< countOfAtm; i++){
            ATMList.add(new ATMImpl(cassettes));
        }
        department = new DepartmentImpl(ATMList);
    }

    private List<Cassette> initializeCassettes() {
        List<Cassette> cassettes = new ArrayList<>();
        cassettes.add(new CassetteImpl(Denomination.RUR_FIFTY, SIZE_RUR_FIFTY, 3));
        cassettes.add(new CassetteImpl(Denomination.RUR_ONE_HUNDRED, SIZE_RUR_ONE_HUNDRED, 1));
        cassettes.add(new CassetteImpl(Denomination.RUR_FIVE_HUNDRED, SIZE_RUR_FIVE_HUNDRED, 2));
        cassettes.add(new CassetteImpl(Denomination.RUR_ONE_THOUSAND, SIZE_RUR_ONE_THOUSAND, 3));
        cassettes.add(new CassetteImpl(Denomination.RUR_TWO_THOUSAND, SIZE_RUR_TWO_THOUSAND, 2));
        cassettes.add(new CassetteImpl(Denomination.RUR_FIVE_THOUSAND, SIZE_RUR_FIVE_THOUSAND, 5));
        return cassettes;
    }

    @Test
    void addATM() {
        ATM tempATM = new ATMImpl(initializeCassettes());
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*countOfAtm, department.getBalance());
        department.addATM(tempATM);
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*(countOfAtm+1), department.getBalance());
    }

    @Test
    void removeATM() {
        ATM tempATM = new ATMImpl(initializeCassettes());
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*countOfAtm, department.getBalance());
        department.addATM(tempATM);
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*(countOfAtm+1), department.getBalance());
        department.removeATM(tempATM);
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*countOfAtm, department.getBalance());
    }

    @Test
    void getBalance() {
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*countOfAtm, department.getBalance());
    }

    @Test
    void printBalanceOnScreen() {
        //Вывод в консоль баланс каждого банкомата
        department.printBalanceOnScreen();
    }

    @Test
    void resetATM() throws ATMException {
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*countOfAtm, department.getBalance());
        ATM tempATM = new ATMImpl(initializeCassettes());
        department.addATM(tempATM);
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*(countOfAtm+1), department.getBalance());
        tempATM.pickCash(22000);
        assertEquals(((50*3+100+500+500+3*1000+2*2000+5*5000)*(countOfAtm+1))-22000, department.getBalance());
        department.resetATM();
        assertEquals((50*3+100+500+500+3*1000+2*2000+5*5000)*(countOfAtm+1), department.getBalance());
    }

}