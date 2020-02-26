package ru.otus.hw.custom.framework;

public class CustomFrameworkAssertion {

    public static String integerIsEquals(Integer expectedInt, Integer reallyInt) throws CustomFrameworkException {
        Boolean isEquals = expectedInt.equals(reallyInt);
        if (isEquals) {
            return isEquals + " - Значения равны";
        } else {
            throw new CustomFrameworkException(isEquals + " - Ожидалось значение " + expectedInt + ", на выходе имеем " + reallyInt);
        }
    }

}
