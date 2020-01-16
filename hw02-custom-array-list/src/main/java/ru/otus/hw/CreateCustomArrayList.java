package ru.otus.hw;

import ru.otus.hw.util.CustomArrayList;

import java.util.Collections;

public class CreateCustomArrayList {

    public static void main(String... args) {

        CustomArrayList<String> strings = new CustomArrayList<>();
        strings.add("First");
        Collections.addAll(strings, "Second", "Third");

        System.out.println(strings.size());

  /*    Collections.addAll(Collection<? super T> c, T... elements)
        Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
        Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
    */


    }
}
