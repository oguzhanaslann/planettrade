package planettrade;

import util.Pair;

import java.util.Map;
import java.util.Set;

//A galaxy contains a set of planets that are certain distance away (in terms of lightyear)
//from each other.
public abstract class Galaxy {
    private Set<Planet> planets;

    private Map<Pair<Planet,Planet>, LightYear> distances;
}
