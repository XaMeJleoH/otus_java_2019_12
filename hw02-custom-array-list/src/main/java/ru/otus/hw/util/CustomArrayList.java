package ru.otus.hw.util;

import java.util.*;
import java.util.stream.Collectors;

public class CustomArrayList<T> implements List<T> {

    private static final int defCapacity = 5;
    private static final double incCapacity = 1.5;

    private Object[] array;
    private int capacityArray;

    public CustomArrayList() {
        array = new Object[defCapacity];
    }

    public CustomArrayList(int capacityArray) {
        if (capacityArray > 0) {
            array = new Object[capacityArray];
        } else if (capacityArray == 0) {
            array = new Object[]{};
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
        if (capacityArray == array.length) {
            array = Arrays.copyOf(array, capacityArray * (int) Math.round(capacityArray * incCapacity));
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
        return new CustomArrayListIterator();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }


    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private class CustomArrayListIterator implements ListIterator<T> {
        private int index = 0;
        private int lastIndex = -1;

        @Override
        public boolean hasNext() {
            return index < lastIndex;
        }

        @Override
        public T next() {
            if (index >= capacityArray) {
                throw new NoSuchElementException();
            }
            return get(lastIndex = index++);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            if (--index < 0) {
                throw new NoSuchElementException();
            }
            return get(lastIndex -= index);
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return --index;
        }


        @Override
        public void set(T t) {
            CustomArrayList.this.set(lastIndex, t);
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
