package planettrade.game.actions;

import planettrade.commodity.Cargo;
import planettrade.player.PlanetTradePlayer;
import project.gameengine.base.Action;

public record SellCargoAction(
        PlanetTradePlayer player,
        Cargo cargo
) implements Action {
}
