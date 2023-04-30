package planettrade.game;

import planettrade.LightYear;
import planettrade.blackhole.MilkyWayBlackhole;
import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.galaxy.Galaxy;
import planettrade.game.actions.BuyItemAction;
import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.actions.SellCargoAction;
import planettrade.game.context.NoContext;
import planettrade.game.context.PlanetTradeContextFactory;
import planettrade.logger.Logger;
import planettrade.market.Market;
import planettrade.market.MarketGenerator;
import planettrade.money.Money;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;
import planettrade.player.PlanetTradePlayer;
import planettrade.player.PlayerAttributes;
import planettrade.player.PlayerReadOnlyInfoProvider;
import planettrade.spaceship.ShapeShip;
import planettrade.spaceship.SpaceshipFactory;
import project.gameengine.TurnBasedGameEngine;
import project.gameengine.base.*;

import java.util.*;
import java.util.function.Supplier;

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
        //IntStream
        //                .range(MINIMUM_PLAYER_COUNT, NumberUtils.random(MINIMUM_PLAYER_COUNT, MAXIMUM_PLAYER_COUNT))
        //                .mapToObj(i -> new PlanetTradePlayer("Player - " + i, this))
        //                .toList();
        this.players = List.of(
                new PlanetTradePlayer("Player - 1", this)
        );

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
                                        playerAttributes.put(player, attributes.withCurrentPlanet(planet));
                                    }
                            );
                }
        );
        Logger.release("R-13 Each player is placed at a random planet in the galaxy.");
        playerAttributes.forEach((player, attributes) -> {
            // player and placed planet
            Logger.debug("PlanetTradeGame: player: " + player + " is placed at planet: " + attributes.currentPlanet());
        });
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

        return new NoContext();
    }

    private PlanetTradePlayer getPlayerAtTurn(int currentTurn) {
        return players.get(currentTurn % players.size());
    }

    @Override
    public void update(Action action) {
        PlanetTradePlayer currentPlayer = getPlayerAtTurn(currentTurn);
        Logger.info("PlanetTradeGame: player: " + currentPlayer + " is taking action: " + action);
        processActionOf(currentPlayer, action);
        currentTurn = Math.min(currentTurn + 1, turns);
        logPlayerStats();
    }

    private void logPlayerStats() {
        playerAttributes.forEach((player, attributes) -> {
            StringBuilder builder = new StringBuilder();
            builder.append("player: ")
                    .append(player)
                    .append(" has: ")
                    .append(attributes.money())
                    .append(" money")
                    .append(" and is at planet: ")
                    .append(attributes.currentPlanet())
                    .append(" and ")
                    .append(" has Shapeship: ")
                    .append(attributes.shapeShip());
            attributes.shapeShip().ifPresent(shapeShip -> {
                builder.append(" as ")
                        .append(shapeShip);
            });

            Logger.debug(builder.toString());
        });
    }

    private void processActionOf(PlanetTradePlayer currentPlayer, Action action) {
        if (action instanceof BuyShapeShipAction) {
            Logger.debug("PlanetTradeGame: player: " + currentPlayer.getName() + " is buying spaceship" + ((BuyShapeShipAction) action).getShapeShip());
            onBuyAction(currentPlayer, (BuyShapeShipAction) action);
        } else if (action instanceof BuyItemAction) {
            Logger.debug("PlanetTradeGame: player: " + currentPlayer.getName() + " is buying item" + ((BuyItemAction) action).commodity().name());
            onBuyItemAction(currentPlayer, (BuyItemAction) action);
        } else if (action instanceof SellCargoAction) {
            Logger.debug("PlanetTradeGame: player: " + currentPlayer.getName() + " is selling cargo" + ((SellCargoAction) action).cargo());
            onSellCargoAction(currentPlayer, (SellCargoAction) action);
        }
    }

    private void onBuyAction(PlanetTradePlayer currentPlayer, BuyShapeShipAction action) {
        Optional<ShapeShip> shapeShipOptional = action.getShapeShip();
        if (shapeShipOptional.isPresent()) {
            ShapeShip shapeShip = shapeShipOptional.get();
            PlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
            PlayerAttributes updatedAttrs = attributesOfPlayer
                    .withShapeShip(shapeShipOptional)
                    .reducedMoney(shapeShip.getBuyPrice());
            Logger.release("R-14 The spaceship is added to the player's spaceship list and the current money is decreased by the buy price of the spaceship.");
            updateUserAttrSync(currentPlayer, updatedAttrs);
            Logger.debug("PlanetTradeGame: player: " + currentPlayer.getName() + " bought spaceship: " + shapeShip.getName());
        }
    }

    private void onBuyItemAction(PlanetTradePlayer currentPlayer, BuyItemAction action) {
        Commodity commodityToBuy = action.commodity();
        int commodityAmount = action.amount();

        PlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
        if (attributesOfPlayer.currentPlanet().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is not at any planet");
            return;
        }

        Planet currentPlanet = attributesOfPlayer.currentPlanet().get();
        Market market = currentPlanet.getMarket();
        if (!market.hasEnoughSupplyOfCommodity(commodityToBuy, commodityAmount)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but market does not have enough supply");
            return;
        }

        double price = market.getPriceOf(commodityToBuy, commodityAmount);
        Money priceAsMoney = Money.of(price);
        Money userMoney = attributesOfPlayer.money();

        if (userMoney.isLess(priceAsMoney)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but does not have enough money");
            return;
        }

        if (attributesOfPlayer.shapeShip().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but does not have a spaceship");
            return;
        }

        ShapeShip shapeShip = attributesOfPlayer.shapeShip().get();
        if (!shapeShip.canCarry(commodityToBuy, commodityAmount)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but spaceship cannot carry that much");
            return;
        }

        market.buy(commodityToBuy, commodityAmount);
        PlayerAttributes updatedAttrs = attributesOfPlayer
                .withMoney(userMoney.subtract(priceAsMoney));
        updateUserAttrSync(currentPlayer, updatedAttrs);
        shapeShip.loadCargo(Cargo.of(commodityToBuy, commodityAmount));
        Logger.release("PlanetTradeGame: player: " + currentPlayer.getName() + " bought commodity: " + commodityToBuy.name() + " with amount: " + commodityAmount + " with price: " + price);
    }

    private void onSellCargoAction(PlanetTradePlayer currentPlayer, SellCargoAction action) {
        Cargo cargoToSell = action.cargo();
        PlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
        if (attributesOfPlayer.currentPlanet().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is not at any planet");
            return;
        }


        // check cargo is actually in the ship
        if (attributesOfPlayer.shapeShip().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to sell cargo: " + cargoToSell + " but does not have a spaceship");
            return;
        }
        ShapeShip shapeShip = attributesOfPlayer.shapeShip().get();
        if (!shapeShip.hasCargo(cargoToSell)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to sell cargo: " + cargoToSell + " but does not have that cargo");
            return;
        }

        Planet currentPlanet = attributesOfPlayer.currentPlanet().get();
        Market market = currentPlanet.getMarket();
        Money sellMoney = market.sell(cargoToSell.commodity(), cargoToSell.quantity());

        shapeShip.unloadCargo(cargoToSell);
        PlayerAttributes updatedAttrs = attributesOfPlayer
                .withMoney(attributesOfPlayer.money().add(sellMoney));
        updateUserAttrSync(currentPlayer, updatedAttrs);
        Logger.release("PlanetTradeGame: player: " + currentPlayer.getName() + " sold cargo: " + cargoToSell + " with price: " + sellMoney);
    }

    private void updateUserAttrSync(PlanetTradePlayer currentPlayer, PlayerAttributes updatedAttrs) {
        synchronized (playerAttributes) {
            playerAttributes.put(currentPlayer, updatedAttrs);
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
