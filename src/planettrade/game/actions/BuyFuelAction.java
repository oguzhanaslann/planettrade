package planettrade.game.actions;

import planettrade.player.PlanetTradePlayer;
import project.gameengine.base.Action;

public record BuyFuelAction(
        PlanetTradePlayer player,
        double fuelToBuy
) implements Action {
}
