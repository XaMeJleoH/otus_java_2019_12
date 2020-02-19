package test;

import ru.otus.hw.custom.framework.CustomFrameworkAssertion;
import ru.otus.hw.custom.framework.CustomFrameworkException;
import ru.otus.hw.custom.framework.annotation.After;
import ru.otus.hw.custom.framework.annotation.Before;
import ru.otus.hw.custom.framework.annotation.Test;

public class StringTest {

    @Before
    public static void before() {
        System.out.println("Before ->");
    }

    @Test
    public static void showString() {
        System.out.println("> Test create here <");
    }

    @Test
    public static void createCustomFault() throws CustomFrameworkException {
        System.out.println(">! Calling custom throw !<");
        throw new CustomFrameworkException("Custome error is here! ");
    }

    @Test
    public static void checkNumber() throws CustomFrameworkException {
        int num = 1+1;
        CustomFrameworkAssertion.integerIsEquals(3, num);
    }

    @Test
    public static void divOnNull() throws CustomFrameworkException {
        System.out.println(">! Calling division on Null throw !<");
        int num = 2;
        System.out.println(2/0);
    }

    @Test
    public static void showTwoStrings() {
        System.out.println("> First string <");
        System.out.println("> Second string <");
    }

    @After
    public static void after() {
        System.out.println("<- After");
    }

}
