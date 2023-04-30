package planettrade.util;

public final class Pair<T, K> {
    private T first;
    private K second;


    public boolean holds(Object o) {
        return first.equals(o) || second.equals(o);
    }

    private Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }

    public static <T, K> Pair<T, K> of(T first, K second) {
        return new Pair<T, K>(first, second);
    }
}
