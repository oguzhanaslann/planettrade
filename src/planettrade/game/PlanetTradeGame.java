package planettrade;

import planettrade.blackhole.MilkyWayBlackhole;
import planettrade.commodity.Commodity;
import planettrade.galaxy.Galaxy;
import planettrade.money.Money;
import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import planettrade.spaceship.SpaceshipFactory;
import project.gameengine.BasicConsolRenderer;
import project.gameengine.TurnBasedGameEngine;
import project.gameengine.base.*;

import java.util.List;
import java.util.stream.IntStream;

public class PlanetTradeGame implements Game {

    public static final int MAXIMUM_PLAYER_COUNT = 10;
    public static final int MINIMUM_PLAYER_COUNT = 1;
    final List<PlanetTradePlayer> players;
    private GameEngine engine = new TurnBasedGameEngine(
            this,
            new BasicConsolRenderer()
    );

    private Money initialMoney = new Money(10_000);

    public PlanetTradeGame() {

        players = IntStream
                .range(MINIMUM_PLAYER_COUNT, MAXIMUM_PLAYER_COUNT)
                .mapToObj(i -> new PlanetTradePlayer("Player - " + i, initialMoney))
                .toList();

        players.forEach(player -> engine.addPlayer(player));
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void init(List<Player> players) {

        MilkyWayBlackhole blackhole = new MilkyWayBlackhole();
        Galaxy galaxy = blackhole.explode();
        List<Commodity> commodities = Commodity.arbitraryCommodities();
        List<ShapeShip> shapeShips = SpaceshipFactory.randomList(5);
        this.players.forEach(
                player -> {
                    galaxy.randomPlanet().ifPresent(player::setCurrentPlanet);
                }
        );

        this.players.forEach(
                player -> {
                     player.prepareForGame();
                }
        );

    }

    @Override
    public GameContext getContext() {
        return null;
    }

    @Override
    public void update(Action action) {

    }

    @Override
    public int minimumPlayerCount() {
        return 1;
    }

    @Override
    public int maximumPlayerCount() {
        return 10;
    }
}
