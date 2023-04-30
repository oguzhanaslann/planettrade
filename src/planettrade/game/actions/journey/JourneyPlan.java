package planettrade.game.actions.journey;

import planettrade.planet.Planet;
import planettrade.player.PlanetTradePlayer;

public record JourneyPlan(
        PlanetTradePlayer player,
        Planet sourcePlanet,
        Planet destinationPlanet
) {
}