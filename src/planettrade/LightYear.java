package planettrade;

import util.NumberUtils;

public record LightYear(long distance) {
     public static LightYear random(long lowerLimit, long upperLimit) {
        return new LightYear(NumberUtils.random(lowerLimit, upperLimit));
    }
}

