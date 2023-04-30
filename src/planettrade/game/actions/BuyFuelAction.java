package planettrade.game.actions;

import planettrade.player.PlanetTradePlayer;
import project.gameengine.base.Action;

public record BuyFuelAction(
        PlanetTradePlayer planetTradePlayer,
        double fuelToBuy
) implements Action {
}
