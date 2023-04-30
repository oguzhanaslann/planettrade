package planettrade.game.context;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.SpaceShip;
import project.gameengine.base.GameContext;

import java.util.List;

public class PlanetTradeContextFactory {

    private PlanetTradeContextFactory() {
    }

    public static PlanetTradeContextFactory createInstance() {
        return new PlanetTradeContextFactory();
    }

    public GameContext buyShapeShip(List<SpaceShip> spaceShips, PlanetTradePlayer currentPlayer) {
        return new BuyShapeShipContext(spaceShips, currentPlayer);
    }
}
