package planettrade.player;

import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.ShapeShip;

import java.util.Optional;

public record PlayerAttributes(
        Optional<ShapeShip> shapeShip,
        Optional<Planet> currentPlanet,
        Money money

) {

    public PlayerAttributes(
            Optional<ShapeShip> shapeShip,
            Optional<Planet> currentPlanet
    ) {
        this(shapeShip, currentPlanet, Money.ZERO);
    }

    public PlayerAttributes() {
        this(Optional.empty(), Optional.empty(), Money.ZERO);
    }

    public PlayerAttributes(Money money) {
        this(Optional.empty(), Optional.empty(), money);
    }

    // copy methods
    public PlayerAttributes withShapeShip(Optional<ShapeShip> shapeShip) {
        return new PlayerAttributes(shapeShip, currentPlanet(), money());
    }

    public PlayerAttributes withCurrentPlanet(Planet currentPlanet) {
        return new PlayerAttributes(shapeShip(), Optional.of(currentPlanet), money());
    }

    public PlayerAttributes withMoney(Money money) {
        return new PlayerAttributes(shapeShip(), currentPlanet(), money);
    }

    // reduces money by the given amount
    public PlayerAttributes reducedMoney(Money money) {
        return new PlayerAttributes(shapeShip(), currentPlanet(), money().subtract(money));
    }
}
