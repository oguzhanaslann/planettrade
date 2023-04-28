package planettrade.game;

import planettrade.LightYear;
import planettrade.blackhole.MilkyWayBlackhole;
import planettrade.commodity.Commodity;
import planettrade.galaxy.Galaxy;
import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.context.PlanetTradeContextFactory;
import planettrade.logger.Logger;
import planettrade.market.Market;
import planettrade.market.MarketGenerator;
import planettrade.money.Money;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;
import planettrade.player.PlanetTradePlayer;
import planettrade.player.PlayerAttributeProvider;
import planettrade.player.PlayerAttributes;
import planettrade.player.PlayerReadOnlyInfoProvider;
import planettrade.spaceship.ShapeShip;
import planettrade.spaceship.SpaceshipFactory;
import project.gameengine.TurnBasedGameEngine;
import project.gameengine.base.*;
import util.NumberUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class PlanetTradeGame implements Game, PlayerReadOnlyInfoProvider {
    public static final Money initialMoney = new Money(10_000);
    public static final int TURN_FOR_EACH_PLAYER = 10;
    public static final int MAXIMUM_PLAYER_COUNT = 10;
    public static final int MINIMUM_PLAYER_COUNT = 1;
    final List<PlanetTradePlayer> players;
    private final GameEngine engine;
    private int turns;
    private int currentTurn = 0;

    private PlanetTradeContextFactory actionFactory = PlanetTradeContextFactory.createInstance();

    private List<Commodity> commodities;
    private List<ShapeShip> shapeShips;

    private HashMap<PlanetTradePlayer, PlayerAttributes> playerAttributes = new HashMap<>();

    private Galaxy galaxy = null;

    public PlanetTradeGame(GameRenderer gameRenderer) {
        Logger.debug("PlanetTradeGame: initializing with renderer: " + gameRenderer);
        this.engine = new TurnBasedGameEngine(this, gameRenderer);

        this.players = IntStream
                .range(MINIMUM_PLAYER_COUNT, NumberUtils.random(MINIMUM_PLAYER_COUNT, MAXIMUM_PLAYER_COUNT))
                .mapToObj(i -> new PlanetTradePlayer("Player - " + i, this))
                .toList();

        Logger.info("PlanetTradeGame: total players: " + players.size());
        players.forEach(engine::addPlayer);
        Logger.debug("PlanetTradeGame: all players added to game engine");
    }

    public void start() {
        Logger.release("PlanetTradeGame: starting game");
        engine.playARound();
        Logger.release("PlanetTradeGame: game over");
    }

    @Override
    public boolean isOver() {
        if (currentTurn >= turns) {
            onGameOver();
            return true;
        }
        return false;
    }

    private void onGameOver() {
        Logger.release("PlanetTradeGame: game is over");
        currentTurn = 0;
        turns = players.size() * TURN_FOR_EACH_PLAYER;
    }

    @Override
    public void init(List<Player> players) {
        players.forEach(player -> {
            if (!players.contains(player)) {
                throw new IllegalArgumentException("PlanetTradeGame: player: " + player + " is not in the list of players: " + players);
            }
        });

        this.players.forEach(
                player -> {
                    Logger.release("R-24 Each player starts the game with an initial amount of money and no spaceship");
                    playerAttributes.put(player, new PlayerAttributes(initialMoney));
                }
        );

        initTurns(players);

        this.commodities = Commodity.arbitraryCommodities();
        Logger.release("R-11 A list of commodities with arbitrary names and properties generated.");
        Logger.debug("PlanetTradeGame: commodities created arbitrarily: " + commodities);


        Supplier<List<Commodity>> commoditySupplier = () -> commodities;
        MilkyWayBlackhole blackhole = new MilkyWayBlackhole(new MarketGenerator(commoditySupplier));
        Logger.debug("PlanetTradeGame: blackhole created: " + blackhole);
       this.galaxy = blackhole.explode();
        Logger.debug("PlanetTradeGame: galaxy created: " + galaxy);
        Logger.release("R-10 A blackhole explodes and a galaxy is created randomly");

        this.shapeShips = SpaceshipFactory.randomShapeShipGroupWithSize(5);
        Logger.release("R-12 A list of spaceships is crated randomly by a spaceship factory.");
        Logger.debug("PlanetTradeGame: shapeShips created: " + shapeShips);

        this.players.forEach(
                player -> {
                    final PlayerAttributes attributes = playerAttributes.get(player);
                    galaxy.randomPlanet()
                            .ifPresent(
                                    planet -> {
                                        playerAttributes.put(player,attributes.withCurrentPlanet(planet));
                                    }
                            );
                }
        );
        Logger.release("R-13 Each player is placed at a random planet in the galaxy.");
        playerAttributes.forEach((player, attributes) -> {
            // player and placed planet
            Logger.debug("PlanetTradeGame: player: " + player + " is placed at planet: " + attributes.currentPlanet());
        } );
    }

    private void initTurns(List<Player> players) {
        if (turns != players.size() * TURN_FOR_EACH_PLAYER) {
            turns = players.size() * TURN_FOR_EACH_PLAYER;
            currentTurn = 0;
        }
    }

    @Override
    public GameContext getContext() {
        PlanetTradePlayer currentPlayer = getPlayerAtTurn(currentTurn);

        if (!currentPlayer.hasShapeShip()) {
            Logger.release("R-14 Each player is asked to choose and buy a spacehip from an initial list of spacehips which decreases the current money by the buy price of the chosen spaceship");
            return actionFactory.buyShapeShip(shapeShips, currentPlayer);
        }

        return null;
    }

    private PlanetTradePlayer getPlayerAtTurn(int currentTurn) {
        return players.get(currentTurn % players.size());
    }

    @Override
    public void update(Action action) {
        PlanetTradePlayer currentPlayer = getPlayerAtTurn(currentTurn);
        Logger.info("PlanetTradeGame: player: " + currentPlayer + " is taking action: " + action);
        processActionOf(currentPlayer ,action);
        currentTurn = Math.min(currentTurn + 1, turns);
    }

    private void processActionOf(PlanetTradePlayer currentPlayer, Action action) {
        if (action instanceof BuyShapeShipAction) {
            onBuyAction(currentPlayer, (BuyShapeShipAction) action);
        }
    }

    private void onBuyAction(PlanetTradePlayer currentPlayer, BuyShapeShipAction action) {
        BuyShapeShipAction buyShapeShipAction = action;
        Optional<ShapeShip> shapeShipOptional = buyShapeShipAction.getShapeShip();
        if (shapeShipOptional.isPresent()) {
            ShapeShip shapeShip = shapeShipOptional.get();
            PlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
            PlayerAttributes updatedAttrs = attributesOfPlayer
                    .withShapeShip(shapeShipOptional)
                    .reducedMoney(shapeShip.getBuyPrice());
            synchronized (playerAttributes) {
                Logger.release("R-14 The spaceship is added to the player's spaceship list and the current money is decreased by the buy price of the spaceship.");
                playerAttributes.put(currentPlayer, updatedAttrs);
            }
            Logger.debug("PlanetTradeGame: player: " + currentPlayer.getName() + " bought spaceship: " + shapeShip.getName());
        }
    }

    @Override
    public int minimumPlayerCount() {
        return MINIMUM_PLAYER_COUNT;
    }

    @Override
    public int maximumPlayerCount() {
        return MAXIMUM_PLAYER_COUNT;
    }

    @Override
    public PlayerAttributes getAttributes(Player player) {
        return playerAttributes.get(player);
    }

    @Override
    public Market getMarketOf(Planet planet) {
        return planet.getMarket();
    }

    @Override
    public LightYear getDistance(Planet from, Planet to) {
        if (galaxy == null) {
            return LightYear.ZERO;
        } else {
            DistanceTable distanceTable = galaxy.getDistances();
            return distanceTable.getDistance(from, to);
        }
    }

    @Override
    public Set<Planet> getPlanets() {
        if (galaxy == null) {
            return Collections.emptySet();
        } else {
            return galaxy.getPlanets();
        }
    }
}
