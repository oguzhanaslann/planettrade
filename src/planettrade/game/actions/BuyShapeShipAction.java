package planettrade.game.actions;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.SpaceShip;
import project.gameengine.base.Action;

import java.util.Optional;

public class BuyShapeShipAction implements Action {
    private PlanetTradePlayer player;
    private Optional<SpaceShip> shapeShip;

    public BuyShapeShipAction(PlanetTradePlayer player, Optional<SpaceShip> shapeShip) {
        this.player = player;
        this.shapeShip = shapeShip;
    }

    public PlanetTradePlayer getPlayer() {
        return player;
    }

    public Optional<SpaceShip> getShapeShip() {
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
