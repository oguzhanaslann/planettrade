package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.planet.Planet;

import java.util.Map;
import java.util.Set;

public class Milkyway extends Galaxy {

    public Milkyway(Set<Planet> planets, Map<Planet, Map<Planet, LightYear>> distances) {
        super(planets, distances);
    }
}
