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

        //Создание коллекции
        CustomArrayList<String> strings = new CustomArrayList<>();

        //Проверяем работоспособность метода - "Collections.addAll(Collection<? super T> c, T... elements)"
        strings.add("First");
        Collections.addAll(strings,
                "Second", "Third", "Custom", "Array", "List", "hohooh", "I", "want", "to", "sleep",
                null, ":)", "str", "vrm", "jvm", "harley", "queen", "ji", "fuse", "orangeHat", "shoes", null, "Mistery");

        System.out.println("<!------------- part 1 start -------------->");
        System.out.println("Was created collection with size =" + strings.size());
        System.out.println("The collection contain the follow elements: ");
        System.out.println(strings);

        System.out.println("<!------------- part 1 end --------------> \r\n");
        System.out.println("<!------------- part 2 start -------------->");

        //Проверяем работоспособность метода - "Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)"
        CustomArrayList<String> extStrings = new CustomArrayList<>();
        Collections.addAll(extStrings, "Thor", "Banshee", "Leon", "Tiger");
        Collections.copy(strings, extStrings);


        System.out.println("Was created extended collection with size =" + extStrings.size());
        System.out.print("The extended collection contain the follow elements: ");
        System.out.println(extStrings);
        System.out.println("Collection 'strings' after copy have the follow size =" + strings.size());
        System.out.println("Collection 'strings' contain the follow elements: ");
        System.out.println(strings);

        System.out.println("<!------------- part 2 end -------------->");


    }
}
