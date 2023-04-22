package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.logger.Logger;
import planettrade.planet.Planet;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

public abstract class Galaxy {
    private Set<Planet> planets;

    private Map<Planet, Map<Planet, LightYear>> distances;

    public Galaxy(Set<Planet> planets, Map<Planet, Map<Planet, LightYear>> distances) {
        this.planets = planets;
        this.distances = distances;
        controlInputsValidityAndThrowErrorIfNeeded(this.planets, this.distances);
    }

    private static boolean isMultiPlanetGalaxy(Set<Planet> planets) {
        return planets.size() > 1;
    }

    private static void controlMultiPlanetGalaxyAndThrowErrorIfNeeded(Set<Planet> planets, Map<Planet, Map<Planet, LightYear>> distances) {
        int planetCount = planets.size();
        if (planetCount != distances.size()) {
            throw new IllegalArgumentException("The number of planets and distances must be the same");
        }

        for (Planet planet : planets) {
            if (!distances.containsKey(planet)) {
                throw new IllegalArgumentException("The distances must contain all planets");
            }

            Map<Planet, LightYear> planetDistances = distances.get(planet);
            if (planetDistances.size() != planetCount - 1) {
                throw new IllegalArgumentException("The distances must contain all planets except the planet itself");
            }

            Set<Planet> otherPlanets = planets.stream()
                    .filter(p -> !p.equals(planet))
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);

            for (Planet other : otherPlanets) {
                LightYear distanceFromPlanetToOther = distances.get(planet).get(other);
                LightYear distanceFromOtherToPlanet = distances.get(other).get(planet);
                if (!distanceFromPlanetToOther.equals(distanceFromOtherToPlanet)) {
                    throw new IllegalArgumentException("The distances must be symmetric");
                }
            }
        }
    }

    private void controlInputsValidityAndThrowErrorIfNeeded(Set<Planet> planets, Map<Planet, Map<Planet, LightYear>> distances) {
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

    private void controlSinglePlanetGalaxyAndThrowErrorIfNeeded(Map<Planet, Map<Planet, LightYear>> distances) {
        if (distances.size() != 0) {
            throw new IllegalArgumentException("The distances must be empty for a galaxy with only one planet");
        }
    }

    public Set<Planet> getPlanets() {
        return planets;
    }

    public Optional<Planet> randomPlanet() {
        if (planets.size() == 0) {
            return Optional.empty();
        }
        int randomIndex = (int) (Math.random() * planets.size());
        return Optional.of(planets.stream().skip(randomIndex).findFirst().get());
    }
}
