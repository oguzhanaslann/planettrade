package planettrade.game.context;

import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.SpaceShip;
import project.gameengine.base.GameContext;

import java.util.List;

public class BuyShapeShipContext implements GameContext {

    List<SpaceShip> spaceShips;
    PlanetTradePlayer planetTradePlayer;

    public BuyShapeShipContext(List<SpaceShip> spaceShips, PlanetTradePlayer currentPlayer) {
         this.spaceShips = spaceShips;
         this.planetTradePlayer = currentPlayer;
    }

    public List<SpaceShip> getShapeShips() {
        return spaceShips;
    }
}
