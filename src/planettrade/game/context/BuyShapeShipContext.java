package planettrade.game.context;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import project.gameengine.base.GameContext;

import java.util.List;
import java.util.Optional;

public class BuyShapeShipContext implements GameContext {

    List<ShapeShip> shapeShips;

    public BuyShapeShipContext(List<ShapeShip> shapeShips) {
        this.shapeShips = shapeShips;
    }

    public void purchase(PlanetTradePlayer buyer, ShapeShip shapeShip) {
        if (!shapeShips.contains(shapeShip)) {
            throw new IllegalArgumentException("Shape ship not available");
        }
        buyer.removeMoney(shapeShip.getBuyPrice());
        buyer.setShapeShip(Optional.of(shapeShip));
    }

    public void offerTo(PlanetTradePlayer planetTradePlayer) {
    }
}
