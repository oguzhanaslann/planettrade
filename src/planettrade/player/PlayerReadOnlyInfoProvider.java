package planettrade.player;

import planettrade.LightYear;
import planettrade.market.Market;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;

import java.util.Set;

public interface PlayerReadOnlyInfoProvider extends PlayerAttributeProvider {
    LightYear getDistance(Planet from, Planet to);

    Set<Planet> getPlanets();
}
