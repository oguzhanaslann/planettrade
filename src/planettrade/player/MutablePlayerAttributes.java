package planettrade.player;

import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.LoadableSpaceShip;
import planettrade.spaceship.SpaceShip;

import java.util.Optional;

public final class MutablePlayerAttributes implements PlayerAttributes {
    private Optional<LoadableSpaceShip> shapeShip;

    private Optional<Planet> currentPlanet;

    private Money money;

    public MutablePlayerAttributes(Money money) {
        this.shapeShip = Optional.empty();
        this.currentPlanet = Optional.empty();
        this.money = money;
    }

    @Override
    public Optional<SpaceShip> shapeShip() {
        return shapeShip.map(shapeShip -> shapeShip);
    }

    public Optional<LoadableSpaceShip> loadableShapeShip() {
        return shapeShip;
    }

    public void setShapeShip(LoadableSpaceShip shapeShip) {
        this.shapeShip = Optional.of(shapeShip);
    }

    public void reduceMoney(Money money) {
        this.money = this.money.subtract(money);
    }

    public void addMoney(Money money) {
        this.money = this.money.add(money);
    }

    public void setCurrentPlanet(Planet planet) {
        this.currentPlanet = Optional.of(planet);
    }

    @Override
    public Optional<Planet> currentPlanet() {
        return currentPlanet;
    }

    @Override
    public Money money() {
        return money;
    }
}