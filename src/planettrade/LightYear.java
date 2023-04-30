package planettrade;

import planettrade.util.NumberUtils;

import java.util.Objects;

public record LightYear(double value) implements Comparable<LightYear> {
    public static LightYear ZERO = new LightYear(0);

     public static LightYear random(long lowerLimit, long upperLimit) {
        return new LightYear(NumberUtils.random(lowerLimit, upperLimit));
    }

    public static LightYear of(double distance) {
        return new LightYear(distance);
    }

    public LightYear add(LightYear other) {
        return new LightYear(this.value + other.value);
    }

    // add with double
    public LightYear add(double other) {
        return new LightYear(this.value + other);
    }

    public LightYear subtract(LightYear other) {
        return new LightYear(this.value - other.value);
    }

    // subtract with double
    public LightYear subtract(double other) {
        return new LightYear(this.value - other);
    }

    public LightYear multiply(LightYear other) {
        return new LightYear(this.value * other.value);
    }

    // multiply with double
    public LightYear multiply(double other) {
        return new LightYear(this.value * other);
    }

    public LightYear divide(LightYear other) {
        return new LightYear(this.value / other.value);
    }

    // divide with double
    public LightYear divide(double other) {
        return new LightYear(this.value / other);
    }

    public LightYear abs() {
        return new LightYear(Math.abs(this.value));
    }

    @Override
    public String toString() {
        return "LightYear("+ value +")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightYear lightYear = (LightYear) o;
        return value == lightYear.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(LightYear o) {
        return Double.compare(this.value, o.value);
    }
}

