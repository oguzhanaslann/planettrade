package planettrade.player;

import planettrade.Planet;
import project.gameengine.NullAction;
import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;

import java.util.Optional;

public class PlanetTradePlayer implements Player {

    private Optional<Planet> currentPlanet = Optional.empty();
    private final String name;

    public PlanetTradePlayer(String name) {
        this.name = name;
    }

    public PlanetTradePlayer(Optional<Planet> currentPlanet, String name) {
        this.currentPlanet = currentPlanet;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Action play(GameContext context) {
        return new NullAction();
    }

    @Override
    public void prepareForGame(GameContext context) {

    }
}
