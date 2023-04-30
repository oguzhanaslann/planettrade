package planettrade.game.actions;

import planettrade.commodity.Commodity;
import planettrade.market.Market;
import project.gameengine.base.Action;
import project.gameengine.base.Player;

public record BuyItemAction(
        Player player,
        Commodity commodity,

        int amount
) implements Action {
}
