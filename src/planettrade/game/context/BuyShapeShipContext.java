package planettrade.game.context;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import project.gameengine.base.GameContext;

import java.util.List;

public class BuyShapeShipContext implements GameContext {

    List<ShapeShip> shapeShips;
    PlanetTradePlayer planetTradePlayer;

    public BuyShapeShipContext(List<ShapeShip> shapeShips, PlanetTradePlayer currentPlayer) {
         this.shapeShips = shapeShips;
         this.planetTradePlayer = currentPlayer;
    }

    public List<ShapeShip> getShapeShips() {
        return shapeShips;
    }
}
