package planettrade;

import util.NumberUtils;

import java.util.Objects;

public record LightYear(long distance) {
    public static LightYear ZERO = new LightYear(0);

     public static LightYear random(long lowerLimit, long upperLimit) {
        return new LightYear(NumberUtils.random(lowerLimit, upperLimit));
    }

    @Override
    public String toString() {
        return "LightYear("+distance+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightYear lightYear = (LightYear) o;
        return distance == lightYear.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}

