package ru.otus.hw.util;

import java.util.*;
import java.util.stream.Collectors;

public class CustomArrayList<T> implements List<T> {

    private static final int defCapacity = 5;

    private Object[] array;
    private int capacityArray;

    public CustomArrayList() {
        array = new Object[defCapacity];
    }

    public CustomArrayList(int capacityArray) {
        if (capacityArray > 0){
            array = new Object[capacityArray];
        } else if (capacityArray == 0){
            array = new Object[]{};
        } else if (capacityArray < 0) {
            array = new Object[Math.abs(capacityArray)];
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return Arrays.stream(array)
                .limit(capacityArray)
                .map(o -> o != null ? o.toString() : null)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean add(T t) {
        if (capacityArray == array.length){
            array = Arrays.copyOf(array, capacityArray * (int) Math.round(capacityArray * 1.5));
        }
        array[capacityArray] = t;
        capacityArray++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        Objects.checkIndex(index, capacityArray);
        return (T) array[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        Objects.checkIndex(index, capacityArray);
        T oldValue = (T) array[index];
        array[index] = element;
        return oldValue;
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size());
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ArrayList<T>().listIterator();
    }


    @Override
    public int size() {
        return capacityArray;
    }

    @Override
    public boolean isEmpty() {
        return capacityArray == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }


    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }


    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }


    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

}
