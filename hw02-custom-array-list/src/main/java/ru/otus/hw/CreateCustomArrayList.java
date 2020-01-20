package ru.otus.hw;

import ru.otus.hw.util.CustomArrayList;

import java.util.Collections;

public class CreateCustomArrayList {

    public static void main(String... args) {

        /*
        Collections.addAll(Collection<? super T> c, T... elements)
        Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
        Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
        */

        //Проверяем работоспособность метода - "Collections.addAll(Collection<? super T> c, T... elements)"
        System.out.println("<!------------- part 1 start -------------->");
        CustomArrayList<String> strings = new CustomArrayList<>();
        strings.add("First");
        Collections.addAll(strings,
                "Second", "Third", "Custom", "Array", "List", "hohooh", "I", "want", "to", "sleep",
                "npe1", ":)", "str", "vrm", "jvm", "harley", "queen", "ji", "fuse", "orangeHat", "shoes", "npe1", "Mistery");

        System.out.println("Was created collection with size= " + strings.size());
        System.out.println("The collection contain the follow elements: " + strings);


        //Проверяем работоспособность метода - "Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)"
        System.out.println("<!------------- part 2 start -------------->");
        CustomArrayList<String> extStrings = new CustomArrayList<>();
        Collections.addAll(extStrings, "Thor", "Banshee", "Leon", "Tiger");
        Collections.copy(strings, extStrings);

        System.out.println("Was created extended collection with size= " + extStrings.size());
        System.out.println("The extended collection contain the follow elements: " + extStrings);
        System.out.println("Collection 'strings' after copy have the follow size= " + strings.size());
        System.out.println("Collection 'strings' contain the follow elements: " + strings);


        //Проверяем работоспособность метода - "Collections.static <T> void sort(List<T> list, Comparator<? super T> c)"
        System.out.println("<!------------- part 3 start -------------->");
        System.out.println("Not sorted elements in strings: " + strings);

        Collections.sort(strings, Collections.reverseOrder());
        System.out.println("strings after sort: " + strings);

        System.out.println("<!------------- part 3 end --------------> \r\n");

    }
}
