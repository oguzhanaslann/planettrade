package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.Planet;

import java.util.Map;
import java.util.Set;

public abstract class Galaxy {
    private Set<Planet> planets;

    private Map<Planet, LightYear[]> distances; //distances from each planet to all other planets

    public Galaxy(Set<Planet> planets, Map<Planet, LightYear[]> distances) {
        this.planets = planets;
        this.distances = distances;

        // used class members for safety to prevent from TOCTOU attacks
        controlInputsValidityAndThrowErrorIfNeeded(this.planets, this.distances);
    }

    private void controlInputsValidityAndThrowErrorIfNeeded(Set<Planet> planets, Map<Planet, LightYear[]> distances) {
        if (isMultiPlanetGalaxy(planets)) {
            controlMultiPlanetGalaxyAndThrowErrorIfNeeded(planets, distances);
        } else if (isSinglePlanetGalaxy(planets)) {
            controlSinglePlanetGalaxyAndThrowErrorIfNeeded(distances);
        } else {
            throw new IllegalArgumentException("The number of planets must be at least 1");
        }
    }

    private boolean isSinglePlanetGalaxy(Set<Planet> planets) {
        return planets.size() == 1;
    }

    private void controlSinglePlanetGalaxyAndThrowErrorIfNeeded(Map<Planet, LightYear[]> distances) {
        if (distances.size() != 0) {
            throw new IllegalArgumentException("The distances must be empty for a galaxy with only one planet");
        }
    }

    private static boolean isMultiPlanetGalaxy(Set<Planet> planets) {
        return planets.size() > 1;
    }

    private static void controlMultiPlanetGalaxyAndThrowErrorIfNeeded(Set<Planet> planets, Map<Planet, LightYear[]> distances) {
        int planetCount = planets.size();
        if (planetCount != distances.size()) {
            throw new IllegalArgumentException("The number of planets and distances must be the same");
        }

        for (Planet planet : planets) {
            if (!distances.containsKey(planet)) {
                throw new IllegalArgumentException("The distances must contain all planets");
            }

            LightYear[] planetDistances = distances.get(planet);
            if (planetDistances.length != planets.size() - 1) {
                throw new IllegalArgumentException("The distances must contain all planets");
            }

            for (LightYear distance : planetDistances) {
                if (distance.distance() <= 0) {
                    throw new IllegalArgumentException("The distance must be positive, planet: " + planet.getName() + ", distance: " + distance.distance() + "");
                }
            }
        }
    }
}
