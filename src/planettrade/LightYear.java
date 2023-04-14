package planettrade;

public record LightYear(long distance) {

    static LightYear of(long distance) {
        return new LightYear(distance);
    }
}

