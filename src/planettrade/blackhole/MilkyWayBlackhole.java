package planettrade.blackhole;

import planettrade.LightYear;
import planettrade.galaxy.Galaxy;
import planettrade.galaxy.Milkyway;
import planettrade.logger.Logger;
import planettrade.planet.Planet;
import util.NumberUtils;

import java.util.*;
import java.util.stream.IntStream;

public class MilkyWayBlackhole implements Blackhole {

    private static Set<Planet> randomPlanets() {
        HashSet<Planet> planets = new HashSet<>();
        // random size
        int planetCount = NumberUtils.random(2, 10);
        IntStream
                .range(1, planetCount)
                .mapToObj(MilkyWayBlackhole::randomPlanet)
                .forEach(planets::add);
        return planets;
    }

    private static Planet randomPlanet(int i) {
        return Planet.random();
    }

    /**
     * distances between planets, not including the planet itself
     */
    private static Map<Planet, Map<Planet, LightYear>> randomDistances(Set<Planet> planets) {
        HashMap<Planet, Map<Planet, LightYear>> distances = new HashMap<>();


        for (Planet planet : planets) {
            distances.put(planet, new HashMap<>());
            Set<Planet> others = planets.stream()
                    .filter(p -> !p.equals(planet))
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);
            for (Planet other : others) {
                if (distances.containsKey(other) && distances.get(other).containsKey(planet)) {
                    Map<Planet, LightYear> calculatedDistanceMap = distances.get(other);
                    LightYear calculatedDistance = calculatedDistanceMap.get(planet);
                    Map<Planet, LightYear> planetDistances = distances.getOrDefault(planet, new HashMap<>());
                    planetDistances.put(other, calculatedDistance);
                    distances.put(planet, planetDistances);
                } else {
                    LightYear distance = LightYear.random(100, 1000);
                    Map<Planet, LightYear> planetDistances = distances.getOrDefault(planet, new HashMap<>());
                    planetDistances.put(other, distance);
                    distances.put(planet, planetDistances);
                }
            }
        }
        return distances;
    }

    @Override
    public Galaxy explode() {
        Logger.release("MilkyWayBlackhole explodes - KABOOM!");
        Set<Planet> planets = randomPlanets();
        Logger.debug("MilkyWayBlackhole: random planets created: " + planets);
        Logger.debug("MilkyWayBlackhole: Total planets: " + planets.size());
        Map<Planet, Map<Planet, LightYear>> distances = randomDistances(planets);
        Logger.debug("MilkyWayBlackhole: random distances created: " + distances);
        return new Milkyway(planets, distances);
    }
}
