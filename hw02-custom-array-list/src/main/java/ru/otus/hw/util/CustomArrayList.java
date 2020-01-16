package ru.otus.hw.util;

import java.util.*;

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
    public boolean add(T t) {
        if (capacityArray == array.length){
            array = Arrays.copyOf(array, capacityArray * (int) Math.round(capacityArray * 1.5));
        }
        array[capacityArray] = t;
        capacityArray++;
        return true;
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
    public Object[] toArray() {
        return new Object[0];
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
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
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
    public ListIterator<T> listIterator() {
        return null;
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
