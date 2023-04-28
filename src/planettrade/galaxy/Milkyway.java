package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;

import java.util.Map;
import java.util.Set;

public class Milkyway extends Galaxy {

    public Milkyway(Set<Planet> planets, DistanceTable distances) {
        super(planets, distances);
    }
}
