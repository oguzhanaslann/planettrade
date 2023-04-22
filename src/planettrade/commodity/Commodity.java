package planettrade.commodity;

import java.util.List;

public record Commodity(
        String name,
        double unitVolume,
        double decayRatio
) {

    static Commodity from(Commodity other) {
        return new Commodity(other.name, other.unitVolume, other.decayRatio);
    }

    public static List<Commodity> arbitraryCommodities() {
        return List.of(
                new Commodity("Water", 0.1, 0.1),
                new Commodity("Food", 0.1, 0.1),
                new Commodity("Ore", 0.1, 0.1),
                new Commodity("Metal", 0.1, 0.1),
                new Commodity("Plastic", 0.1, 0.1),
                new Commodity("Textile", 0.1, 0.1),
                new Commodity("Electronics", 0.1, 0.1),
                new Commodity("Furniture", 0.1, 0.1),
                new Commodity("Medicine", 0.1, 0.1),
                new Commodity("Weapon", 0.1, 0.1)
        );
    }
}
