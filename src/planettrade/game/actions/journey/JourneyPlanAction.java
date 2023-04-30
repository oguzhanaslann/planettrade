package planettrade.game.actions.journey;

import planettrade.player.PlanetTradePlayer;
import project.gameengine.base.Action;

public record JourneyPlanAction(
        PlanetTradePlayer player,
        JourneyPlan journeyPlan
) implements Action {
}
