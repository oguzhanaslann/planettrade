package planettrade.galaxy;

import planettrade.LightYear;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;

import java.util.*;

public abstract class Galaxy {
    private Set<Planet> planets;

    private DistanceTable distances;

    public Galaxy(Set<Planet> planets, DistanceTable distances) {
        Objects.requireNonNull(planets, "Planets cannot be null");
        Objects.requireNonNull(distances, "Distances cannot be null");
        this.planets = planets;
        this.distances = distances;
        controlInputsValidityAndThrowErrorIfNeeded(this.planets, this.distances);
    }

    private static boolean isMultiPlanetGalaxy(Set<Planet> planets) {
        return planets.size() > 1;
    }

    private static void controlMultiPlanetGalaxyAndThrowErrorIfNeeded(Set<Planet> planets, DistanceTable distances) {
        int planetCount = planets.size();
        if (planetCount != distances.size()) {
            throw new IllegalArgumentException("The number of planets and distances must be the same");
        }

        for (Planet planet : planets) {
            if (!distances.containsInformationOf(planet)) {
                throw new IllegalArgumentException("The distances must contain all planets");
            }

            Map<Planet, LightYear> planetDistances = distances.getDistancesFrom(planet);
            if (planetDistances.size() != planetCount - 1) {
                throw new IllegalArgumentException("The distances must contain all planets except the planet itself");
            }

            Set<Planet> otherPlanets = planets.stream()
                    .filter(p -> !p.equals(planet))
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);

            for (Planet other : otherPlanets) {
                LightYear distanceFromPlanetToOther = distances.getDistancesFrom(planet).get(other);
                LightYear distanceFromOtherToPlanet = distances.getDistancesFrom(other).get(planet);
                if (!distanceFromPlanetToOther.equals(distanceFromOtherToPlanet)) {
                    throw new IllegalArgumentException("The distances must be symmetric");
                }
            }
        }
    }

    private void controlInputsValidityAndThrowErrorIfNeeded(Set<Planet> planets, DistanceTable distances) {
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

    private void controlSinglePlanetGalaxyAndThrowErrorIfNeeded(DistanceTable distances) {
        if (distances.size() != 0) {
            throw new IllegalArgumentException("The distances must be empty for a galaxy with only one planet");
        }
    }

    public Set<Planet> getPlanets() {
        return Set.copyOf(planets);
    }

    public DistanceTable getDistances() {
        return distances.copy();
    }

    public Optional<Planet> randomPlanet() {
        if (planets.size() == 0) {
            return Optional.empty();
        }
        int randomIndex = (int) (Math.random() * planets.size());
        return Optional.of(planets.stream().skip(randomIndex).findFirst().get());
    }
}
