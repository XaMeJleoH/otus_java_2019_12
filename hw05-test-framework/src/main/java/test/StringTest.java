package test;

import ru.otus.hw.custom.framework.CustomFrameworkException;
import ru.otus.hw.custom.framework.annotation.AfterAnnotation;
import ru.otus.hw.custom.framework.annotation.BeforeAnnotation;
import ru.otus.hw.custom.framework.annotation.TestAnnotation;
import org.junit.Assert;

public class StringTest {

    @BeforeAnnotation
    public static void before() {
        System.out.println("Before ->");
    }

    @TestAnnotation
    public static void showString() {
        System.out.println("> Test create here <");
    }

    @TestAnnotation
    public static void createCustomFault() throws CustomFrameworkException {
        System.out.println(">! Calling custom throw !<");
        throw new CustomFrameworkException("Custome error is here!");
    }

    @TestAnnotation
    public static void checkNumber() {
        int num = 2;
        Assert.assertEquals(2, num);
    }

    @TestAnnotation
    public static void divOnNull() throws CustomFrameworkException {
        System.out.println(">! Calling division on Null throw !<");
        int num = 2;
        System.out.println(2/0);
    }

    @TestAnnotation
    public static void showTwoStrings() {
        System.out.println("> First string <");
        System.out.println("> Second string <");
    }

    @AfterAnnotation
    public static void after() {
        System.out.println("<- After");
    }

}
