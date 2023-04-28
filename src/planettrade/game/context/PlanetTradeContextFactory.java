package planettrade.game.context;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import project.gameengine.base.GameContext;

import java.util.List;

public class PlanetTradeContextFactory {

    private PlanetTradeContextFactory() {
    }

    public static PlanetTradeContextFactory createInstance() {
        return new PlanetTradeContextFactory();
    }

    public GameContext buyShapeShip(List<ShapeShip> shapeShips, PlanetTradePlayer currentPlayer) {
        return new BuyShapeShipContext(shapeShips, currentPlayer);
    }
}
