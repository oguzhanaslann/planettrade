package planettrade.player;

import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.SpaceShip;

import java.util.Optional;

public interface PlayerAttributes {
    Optional<SpaceShip> shapeShip() ;
    Optional<Planet> currentPlanet();
    Money money();
}
