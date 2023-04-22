package planettrade.game;

import planettrade.blackhole.MilkyWayBlackhole;
import planettrade.commodity.Commodity;
import planettrade.galaxy.Galaxy;
import planettrade.game.context.BuyShapeShipContext;
import planettrade.logger.Logger;
import planettrade.money.Money;
import planettrade.player.PlanetTradePlayer;
import planettrade.spaceship.ShapeShip;
import planettrade.spaceship.SpaceshipFactory;
import project.gameengine.TurnBasedGameEngine;
import project.gameengine.base.*;

import java.util.List;

public final class PlanetTradeGame implements Game {
    public static final Money initialMoney = new Money(10_000);
    final List<PlanetTradePlayer> players;
    private final GameEngine engine;
    int tick = 0;

    public PlanetTradeGame(GameRenderer gameRenderer, List<PlanetTradePlayer> players) {
        Logger.debug("PlanetTradeGame: initializing with renderer: " + gameRenderer);

        this.engine = new TurnBasedGameEngine(this, gameRenderer);

        this.players = players;
        Logger.info("PlanetTradeGame: total players: " + players.size());
        players.forEach(player -> engine.addPlayer(player));
        Logger.debug("PlanetTradeGame: all players added to game engine");
    }

    public void start() {
        Logger.release("PlanetTradeGame: starting game");
        engine.playARound();
        Logger.release("PlanetTradeGame: game over");
    }

    @Override
    public boolean isOver() {
        return tick++ > 10;
    }

    @Override
    public void init(List<Player> players) {

        players.forEach(player -> {
            if (!players.contains(player)) {
                throw new IllegalArgumentException("PlanetTradeGame: player: " + player + " is not in the list of players: " + players);
            }
        });

        Logger.debug("PlanetTradeGame: initializing game with players: " + players);

        List<Commodity> commodities = Commodity.arbitraryCommodities();
        Logger.debug("PlanetTradeGame: commodities created arbitrarily: " + commodities);

        MilkyWayBlackhole blackhole = new MilkyWayBlackhole();
        Logger.debug("PlanetTradeGame: blackhole created: " + blackhole);
        Galaxy galaxy = blackhole.explode();
        Logger.debug("PlanetTradeGame: blackhole exploded and galaxy created: " + galaxy);
        this.players.forEach(
                player -> {
                    galaxy.randomPlanet().ifPresent(player::setCurrentPlanet);
                }
        );

        List<ShapeShip> shapeShips = SpaceshipFactory.randomList(5);
        this.players.forEach(
                player -> {
                    player.prepareForGame(new BuyShapeShipContext(shapeShips));
                }
        );

    }

    @Override
    public GameContext getContext() {
        return null;
    }

    @Override
    public void update(Action action) {
        Logger.debug("PlanetTradeGame: updating game with action: " + action);
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
