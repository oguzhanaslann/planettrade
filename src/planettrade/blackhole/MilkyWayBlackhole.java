package planettrade.blackhole;

import planettrade.LightYear;
import planettrade.Planet;
import planettrade.galaxy.Galaxy;
import planettrade.galaxy.Milkyway;
import util.NumberUtils;
import util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class MilkyWayBlackhole implements Blackhole {

    private static Set<Planet> randomPlanets() {
        HashSet<Planet> planets = new HashSet<>();
        // random size
        int planetCount = NumberUtils.random(2, 10);
        IntStream
              .range(1, planetCount)
                .forEach(MilkyWayBlackhole::randomPlanet);
        return planets;
    }

    private static Planet randomPlanet(int i) {
        return Planet.random();
    }

    private static Map<Planet, LightYear[]> randomDistances(Set<Planet> planets) {
        return null;
    }

    @Override
    public Galaxy explode() {
        Set<Planet> planets = randomPlanets();
        Map<Planet, LightYear[]> distances = randomDistances(planets);
        return new Milkyway(planets, distances);
    }


    public static void main(String[] args) {
        MilkyWayBlackhole blackhole = new MilkyWayBlackhole();
        Galaxy galaxy = blackhole.explode();
        System.out.println(galaxy);
    }
}
