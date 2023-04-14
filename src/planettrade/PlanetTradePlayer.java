package planettrade;

import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;

public class PlanetTradePlayer implements Player {

    private final String name;
    private double currentMoney;

    private final ShapeShip ship;

    private Planet currentPlanet;

    public PlanetTradePlayer(String name, double currentMoney, ShapeShip ship, Planet currentPlanet) {
        this.name = name;
        this.currentMoney = currentMoney;
        this.ship = ship;
        this.currentPlanet = currentPlanet;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Action play(GameContext context) {
        return null;
    }

    @Override
    public void prepareForGame(GameContext context) {}
}
