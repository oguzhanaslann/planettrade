package planettrade.planet;

import planettrade.LightYear;

import java.util.Map;

public class DistanceTable {

    private Map<Planet, Map<Planet, LightYear>> distances;

    public DistanceTable(Map<Planet, Map<Planet, LightYear>> distances) {
        this.distances = distances;
    }

    public LightYear getDistance(Planet from, Planet to) {
        LightYear distance = distances.get(from).get(to);
        if (distance == null) {
            return LightYear.ZERO;
        }
        return distance;
    }

    public Map<Planet, LightYear> getDistancesFrom(Planet from) {
        return distances.get(from);
    }

    public DistanceTable copy() {
        return new DistanceTable(Map.copyOf(distances));
    }

    //size
    public int size() {
        return distances.size();
    }

    //containsKey
    public boolean containsInformationOf(Planet planet) {
        return distances.containsKey(planet);
    }
}
