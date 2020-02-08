package test;

import ru.otus.hw.annotation.AfterAnnotation;
import ru.otus.hw.annotation.BeforeAnnotation;
import ru.otus.hw.annotation.TestAnnotation;

public class StringTest {

    @BeforeAnnotation
    public static void before() {
        System.out.println("Before ->");
    }

    @TestAnnotation
    public static void createString() {
        System.out.println("> Test here <");
    }

    @AfterAnnotation
    public static void after() {
        System.out.println("<- After");
    }

}
