package pt.isel.mpd.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyQueries {
    public static <T> Iterable<T> distinct(Iterable<T> src){
        return () -> new Iterator<T>() {
            Iterator<T> iter  = src.iterator();
            T curr, curr2;
            @Override
            public boolean hasNext() {
                /*if(iter.hasNext()){
                    curr = iter.next();
                    Iterator<T> i = src.iterator();
                    while(i.hasNext()){
                        T curr2 = i.next();
                        if(!curr.equals(curr2)) return true;
                    }
                }*/
                return false;
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                return curr2;
            }
        };
    }

    public static <T> Iterable<T> concat(Iterable<T> src, Iterable<T> other){
        return () -> new Iterator<T>() {
            Iterator<T> itersrc = src.iterator();
            Iterator<T> iterother = other.iterator();
            T curr;
            boolean called = false;
            @Override
            public boolean hasNext() {
                if(called) return  true;
                if(itersrc.hasNext()){
                    curr = itersrc.next();
                    called = true;
                    return true;
                }
                else if(iterother.hasNext()){
                    curr = iterother.next();
                    called = true;
                    return true;
                }
                return false;
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                called = false;
                return curr;
            }
        };
    }

    public static <T> Iterable<T> interleave(Iterable<T> src, Iterable<T> other){
        return () -> new Iterator<T>() {
            Iterator<T> iters = src.iterator();
            Iterator<T> itero = other.iterator();
            T curr;
            boolean isSrc = true, called = false;
            @Override
            public boolean hasNext() {
                if(called) return  true;
                    if(isSrc && iters.hasNext()){
                        curr = iters.next();
                        isSrc = false;
                        called = true;
                        return true;
                    }else if(itero.hasNext()){
                        curr = itero.next();
                        isSrc = true;
                        called = true;
                        return true;
                    }
                return false;
            }
            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                called = false;
                return curr;
            }
        };
    }

    public static <T> Iterable<T> generate(Supplier<T> sup) {
        return () -> new Iterator<T>() {
            public boolean hasNext() { return true; }
            public T next() { return sup.get(); }
        };
    }

    public static <T> Iterable<T> limit(Iterable<T> src, int nr) {
        return () -> new Iterator<T>() {
            Iterator<T> iter = src.iterator();
            int idx = 0;
            public boolean hasNext() {
                return idx < nr && iter.hasNext();
            }
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                idx++;
                return iter.next();
            }
        };
    }

    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred) {
        return () -> new Iterator<T>() {
            Iterator<T> iter = src.iterator();
            T curr;
            boolean called = false;
            @Override
            public boolean hasNext() {
                if(called) return true;
                while (iter.hasNext()) {
                    curr = iter.next();
                    if (pred.test(curr)) {
                        called = true;
                        return true;
                    }
                }
                return false;
            }
            @Override
            public T next() {
                if (hasNext()){
                    called = false;
                    return curr;
                }
                throw new NoSuchElementException();
            }
        };
    }
    public static <T> Iterable<T> skip(Iterable<T> src, int nr) {
        return () -> {
            Iterator<T> iter = src.iterator();
            int idx = nr;
            while(idx-- > 0 && iter.hasNext()) iter.next();
            return iter;
        };
    }
    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper) {
        return () -> new Iterator<R>() {
            Iterator<T> iter = src.iterator();
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }
            @Override
            public R next() {
                return mapper.apply(iter.next());
            }
        };
    }

    /**
     * Terminal operation
     */
    public static <T> int count(Iterable<T> src) {
        int nr = 0;
        for (T item: src) { nr++; }
        return nr;
    }
    /**
     * Terminal operation
     */
    public static <T extends Comparable<T>> T max(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if(!iter.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");
        T first = iter.next();
        while(iter.hasNext()) {
            T curr = iter.next();
            if(curr.compareTo(first) > 0)
                first = curr;
        }
        return first;
    }


}
