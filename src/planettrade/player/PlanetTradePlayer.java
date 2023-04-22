package planettrade.player;

import planettrade.game.context.BuyShapeShipContext;
import planettrade.logger.Logger;
import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.ShapeShip;
import project.gameengine.NullAction;
import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;

import java.util.Optional;

public final class PlanetTradePlayer implements Player {

    private final String name;
    private Optional<ShapeShip> shapeShip = Optional.empty();
    private Optional<Planet> currentPlanet = Optional.empty();

    private Money currentMoney = Money.ZERO;

    public PlanetTradePlayer(String name) {
        this.name = name;
    }

    public PlanetTradePlayer(String name, Money currentMoney) {
        this.name = name;
        this.currentMoney = currentMoney;
    }

    public void setCurrentPlanet(Planet planet) {
        Logger.info("PlanetTradePlayer - " + name + ": setting current planet to: " + planet);
        this.currentPlanet = Optional.of(planet);
    }

    public void addMoney(Money money) {
        this.currentMoney = this.currentMoney.add(money);
    }

    public void removeMoney(Money money) {
        this.currentMoney = this.currentMoney.subtract(money);
    }

    public void setShapeShip(Optional<ShapeShip> shapeShip) {
        this.shapeShip = shapeShip;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Action play(GameContext context) {
        return new NullAction();
    }

    @Override
    public void prepareForGame(GameContext context) {
        Logger.debug("PlanetTradePlayer: preparing for game with context: " + context);
        if (context instanceof BuyShapeShipContext) {
            Logger.debug("PlanetTradePlayer: context is BuyShapeShipContext");
            BuyShapeShipContext buyShapeShipContext = (BuyShapeShipContext) context;
        }
    }

    @Override
    public String toString() {
        return "PlanetTradePlayer{" +
                "name='" + name + '\'' +
                ", shapeShip=" + shapeShip +
                ", currentPlanet=" + currentPlanet +
                ", currentMoney=" + currentMoney +
                '}';
    }
}
