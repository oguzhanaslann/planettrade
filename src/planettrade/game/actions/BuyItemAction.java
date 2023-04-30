package planettrade.game.actions;

import planettrade.commodity.Commodity;
import planettrade.market.Market;
import planettrade.player.PlanetTradePlayer;
import project.gameengine.base.Action;
import project.gameengine.base.Player;

public record BuyItemAction(
        PlanetTradePlayer player,
        Commodity commodity,

        int amount
) implements Action {
}
