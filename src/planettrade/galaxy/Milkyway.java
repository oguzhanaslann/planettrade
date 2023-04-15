package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.Planet;

import java.util.Map;
import java.util.Set;

public class Milkyway extends Galaxy {

    public final static LightYear knownSize = new LightYear(100_000);

    public Milkyway(Set<Planet> planets, Map<Planet, LightYear[]> distances) {
        super(planets, distances);
    }
}
