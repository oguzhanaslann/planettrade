package planettrade.game.actions;

import planettrade.game.context.BuyShapeShipContext;
import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import project.gameengine.base.Action;

import java.util.Optional;

public class BuyShapeShipAction implements Action {
    private PlanetTradePlayer player;
    private Optional<ShapeShip> shapeShip;

    public BuyShapeShipAction(PlanetTradePlayer player, Optional<ShapeShip> shapeShip) {
        this.player = player;
        this.shapeShip = shapeShip;
    }

    public PlanetTradePlayer getPlayer() {
        return player;
    }

    public Optional<ShapeShip> getShapeShip() {
        return shapeShip;
    }

    @Override
    public String toString() {
        return "BuyShapeShipAction{" +
                "player=" + player.getName() +
                ", shapeShip=" + shapeShip +
                '}';
    }
}
