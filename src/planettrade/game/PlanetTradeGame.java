package planettrade.game;

import planettrade.LightYear;
import planettrade.blackhole.MilkyWayBlackhole;
import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.galaxy.Galaxy;
import planettrade.game.actions.BuyFuelAction;
import planettrade.game.actions.BuyItemAction;
import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.actions.SellCargoAction;
import planettrade.game.actions.journey.JourneyPlan;
import planettrade.game.actions.journey.JourneyPlanAction;
import planettrade.game.context.NoContext;
import planettrade.game.context.PlanetTradeContextFactory;
import planettrade.logger.Logger;
import planettrade.market.Market;
import planettrade.market.MarketGenerator;
import planettrade.money.Money;
import planettrade.planet.DistanceTable;
import planettrade.planet.Planet;
import planettrade.player.MutablePlayerAttributes;
import planettrade.player.PlanetTradePlayer;
import planettrade.player.PlayerAttributes;
import planettrade.player.PlayerReadOnlyInfoProvider;
import planettrade.spaceship.LoadableSpaceShip;
import planettrade.spaceship.SpaceShip;
import planettrade.spaceship.SpaceshipFactory;
import project.gameengine.TurnBasedGameEngine;
import project.gameengine.base.*;
import util.NumberUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * R-20
 * At each turn player actions are validated by the game and if they are valid necessary updates are performed accordingly which includes:
 * • the current money of each player
 * • current planet of each player
 * R-22
 * At each market of each planet the following changes randomly
 * • unit buy price and unit sell price of each market item (with relatively small
 * amount)
 * • current supply of each market item (with relatively small amount
 */

public final class PlanetTradeGame implements Game, PlayerReadOnlyInfoProvider {
    public static final Money initialMoney = new Money(10_000);
    public static final int TURN_FOR_EACH_PLAYER = 10;
    public static final int MAXIMUM_PLAYER_COUNT = 10;
    public static final int MINIMUM_PLAYER_COUNT = 1;
    final List<PlanetTradePlayer> players;
    final HashMap<PlanetTradePlayer, Optional<JourneyPlan>> journeyPlans = new HashMap<>();
    private final GameEngine engine;
    private final HashMap<PlanetTradePlayer, MutablePlayerAttributes> playerAttributes = new HashMap<>();
    private int turns;
    private int currentTurn = 0;
    private PlanetTradeContextFactory contextFactory = PlanetTradeContextFactory.createInstance();
    private List<Commodity> commodities;
    private List<LoadableSpaceShip> shapeShips;
    private Galaxy galaxy = null;

    public PlanetTradeGame(GameRenderer gameRenderer) {
        Logger.debug("PlanetTradeGame: initializing with renderer: " + gameRenderer);
        this.engine = new TurnBasedGameEngine(this, gameRenderer);

        this.players = IntStream
                .range(MINIMUM_PLAYER_COUNT, NumberUtils.random(MINIMUM_PLAYER_COUNT, MAXIMUM_PLAYER_COUNT) + 1)
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
                    playerAttributes.put(player, new MutablePlayerAttributes(initialMoney));
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

        this.shapeShips = SpaceshipFactory.randomShapeShipGroupWithSize(5, LoadableSpaceShip.class);
        Logger.release("R-12 A list of spaceships is crated randomly by a spaceship factory.");
        Logger.debug("PlanetTradeGame: shapeShips created: " + shapeShips);

        this.players.forEach(
                player -> {
                    final MutablePlayerAttributes attributes = playerAttributes.get(player);
                    galaxy.randomPlanet().ifPresent(attributes::setCurrentPlanet);
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
            List<SpaceShip> shapeShipsMasked = shapeShips.stream()
                    .map(shapeShip -> (SpaceShip) shapeShip)
                    .toList();
            Logger.release("R-14 Each player is asked to choose and buy a spacehip from an initial list of spacehips which decreases the current money by the buy price of the chosen spaceship");
            return contextFactory.buyShapeShip(shapeShipsMasked, currentPlayer);
        }

        return new NoContext();
    }

    private PlanetTradePlayer getPlayerAtTurn(int currentTurn) {
        return players.get(currentTurn % players.size());
    }

    @Override
    public void update(Action action) {
        decayCargos();
        processJourneyPlans();
        processActionOf(action);
        logPlayerStats();
        currentTurn = Math.min(currentTurn + 1, turns);
    }

    private void decayCargos() {
        players.forEach(
                player -> {
                    MutablePlayerAttributes attrs = playerAttributes.get(player);
                    Optional<LoadableSpaceShip> spaceShipOptional = attrs.loadableShapeShip();
                    spaceShipOptional.ifPresent(spaceShip -> {
                        spaceShip.decayCargos();
                        Logger.info("PlanetTradeGame: player: " + player + " spaceship: " + spaceShip + " cargos decayed");
                    });

                }
        );
    }

    private void processJourneyPlans() {
        players.forEach(
                player -> {
                    Optional<JourneyPlan> journeyPlan = getJourneyPlanOnEmpty(player);
                    boolean hasExecuted = executeJourneyPlanGetResult(journeyPlan);
                    journeyPlans.remove(player);
                    chargePlayerForRentBy(hasExecuted, player);
                }
        );
    }

    private void chargePlayerForRentBy(boolean hasExecuted, PlanetTradePlayer player) {
        if (!hasExecuted) {
            MutablePlayerAttributes attrs = playerAttributes.get(player);
            Optional<Planet> currentPlanetOptional = attrs.currentPlanet();
            if (currentPlanetOptional.isEmpty()) {
                Logger.error("PlanetTradeGame: player: " + player + " has no current planet");
                return;
            }
            Planet currentPlanet = currentPlanetOptional.get();
            Money rent = currentPlanet.getSpaceShipParkingPricePerTurn();
            attrs.reduceMoney(rent);
            Logger.info("PlanetTradeGame: player: " + player + " has been charged for rent: " + rent + " for parking at planet: " + currentPlanet);
        }
    }

    private boolean executeJourneyPlanGetResult(Optional<JourneyPlan> journeyPlan) {
        if (journeyPlan.isEmpty()) {
            return false;
        }

        JourneyPlan plan = journeyPlan.get();
        if (!plan.player().hasShapeShip()) {
            return false;
        }

        LightYear distance = getDistance(plan.sourcePlanet(), plan.destinationPlanet());
        MutablePlayerAttributes attrs = playerAttributes.get(plan.player());
        LoadableSpaceShip spaceShip = attrs.loadableShapeShip().get();
        double maxLightYearDistance = spaceShip.getCurrentFuel() / spaceShip.getFuelUsagePerLightYear();
        LightYear maxLightYear = new LightYear(maxLightYearDistance);

        if (distance.compareTo(maxLightYear) <= 0) {
            spaceShip.reduceFuel(spaceShip.getFuelUsagePerLightYear() * distance.value());
            attrs.setCurrentPlanet(plan.destinationPlanet());
            Logger.release("PlanetTradeGame: player: " + plan.player() + " has traveled from: " + plan.sourcePlanet() + " to: " + plan.destinationPlanet());
            return true;
        } else {
            Logger.debug("PlanetTradeGame: player: " + plan.player() + " has not traveled from: " + plan.sourcePlanet() + " to: " + plan.destinationPlanet() + " because of fuel shortage");
            return false;
        }
    }

    private Optional<JourneyPlan> getJourneyPlanOnEmpty(PlanetTradePlayer player) {
        if (journeyPlans.containsKey(player)) {
            return journeyPlans.get(player);
        } else {
            return Optional.empty();
        }
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
                    .append(attributes.spaceShip());
            attributes.spaceShip().ifPresent(shapeShip -> {
                builder.append(" as ")
                        .append(shapeShip);
            });

            Logger.debug(builder.toString());
        });
    }

    private void processActionOf(Action action) {
        if (action instanceof BuyShapeShipAction) {
            BuyShapeShipAction buyShapeShipAction = (BuyShapeShipAction) action;
            Logger.debug("PlanetTradeGame: player: " + buyShapeShipAction.player().getName() + " is buying shapeShip: " + buyShapeShipAction.getShapeShip());
            onBuyAction(buyShapeShipAction);
        } else if (action instanceof BuyItemAction) {
            BuyItemAction buyItemAction = (BuyItemAction) action;
            Logger.debug("PlanetTradeGame: player: " + buyItemAction.player().getName() + " is buying item: " + buyItemAction.commodity().name());
            onBuyItemAction((BuyItemAction) action);
        } else if (action instanceof SellCargoAction) {
            SellCargoAction sellCargoAction = (SellCargoAction) action;
            Logger.debug("PlanetTradeGame: player: " + sellCargoAction.player().getName() + " is selling cargo: " + sellCargoAction.cargo());
            onSellCargoAction((SellCargoAction) action);
        } else if (action instanceof BuyFuelAction) {
            BuyFuelAction buyFuelAction = (BuyFuelAction) action;
            Logger.debug("PlanetTradeGame: player: " + buyFuelAction.player().getName() + " is buying fuel: " + buyFuelAction.fuelToBuy());
            onBuyFuelAction((BuyFuelAction) action);
        } else if (action instanceof JourneyPlanAction) {
            JourneyPlanAction journeyPlanAction = (JourneyPlanAction) action;
            Logger.debug("PlanetTradeGame: player: " + journeyPlanAction.player().getName() + " is planning: " + journeyPlanAction.journeyPlan());
            onJourneyPlanAction((JourneyPlanAction) action);
        } else {
            Logger.error("PlanetTradeGame: unknown action: " + action);
        }
    }

    private void onBuyAction(BuyShapeShipAction action) {
        Optional<SpaceShip> shapeShipOptional = action.getShapeShip();
        PlanetTradePlayer currentPlayer = action.player();
        if (shapeShipOptional.isPresent()) {
            final SpaceShip spaceShip = shapeShipOptional.get();
            synchronized (playerAttributes) {
                MutablePlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
                Logger.release("R-14 The spaceship is added to the player's spaceship list and the current money is decreased by the buy price of the spaceship.");
                attributesOfPlayer.setShapeShip(LoadableSpaceShip.from(spaceShip));
                attributesOfPlayer.reduceMoney(spaceShip.getBuyPrice());
            }
            Logger.info("PlanetTradeGame: player: " + currentPlayer.getName() + " bought spaceship: " + spaceShip.getName());
        }
    }

    private void onBuyItemAction(BuyItemAction action) {
        Commodity commodityToBuy = action.commodity();
        int commodityAmount = action.amount();
        PlanetTradePlayer currentPlayer = action.player();

        MutablePlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
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

        if (attributesOfPlayer.spaceShip().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but does not have a spaceship");
            return;
        }

        Optional<LoadableSpaceShip> spaceShipOptional = attributesOfPlayer.loadableShapeShip();
        if (spaceShipOptional.isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but does not have a spaceship");
            return;
        }

        LoadableSpaceShip spaceShip = spaceShipOptional.get();
        if (!spaceShip.canCarry(commodityToBuy, commodityAmount)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy commodity: " + commodityToBuy.name() + " but spaceship cannot carry that much");
            return;
        }

        market.buy(commodityToBuy, commodityAmount);
        attributesOfPlayer.reduceMoney(priceAsMoney);
        spaceShip.loadCargo(Cargo.of(commodityToBuy, commodityAmount));
        Logger.release("PlanetTradeGame: player: " + currentPlayer.getName() + " bought commodity: " + commodityToBuy.name() + " with amount: " + commodityAmount + " with price: " + price);
    }

    private void onSellCargoAction(SellCargoAction action) {
        Cargo cargoToSell = action.cargo();
        PlanetTradePlayer currentPlayer = action.player();
        MutablePlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
        if (attributesOfPlayer.currentPlanet().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is not at any planet");
            return;
        }


        Optional<LoadableSpaceShip> spaceShipOptional = attributesOfPlayer.loadableShapeShip();
        if (spaceShipOptional.isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to sell cargo: " + cargoToSell + " but does not have a spaceship");
            return;
        }

        LoadableSpaceShip spaceShip = spaceShipOptional.get();
        if (!spaceShip.hasCargo(cargoToSell)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to sell cargo: " + cargoToSell + " but does not have that cargo");
            return;
        }

        Planet currentPlanet = attributesOfPlayer.currentPlanet().get();
        Market market = currentPlanet.getMarket();
        Money sellMoney = market.sell(cargoToSell.commodity(), cargoToSell.quantity());
        attributesOfPlayer.addMoney(sellMoney);
        spaceShip.unloadCargo(cargoToSell);
        Logger.release("PlanetTradeGame: player: " + currentPlayer.getName() + " sold cargo: " + cargoToSell + " with price: " + sellMoney);
    }

    private void onBuyFuelAction(BuyFuelAction action) {
        double fuelToBuy = action.fuelToBuy();
        PlanetTradePlayer currentPlayer = action.player();
        MutablePlayerAttributes attributesOfPlayer = playerAttributes.get(currentPlayer);
        if (attributesOfPlayer.currentPlanet().isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is not at any planet");
            return;
        }

        Planet currentPlanet = attributesOfPlayer.currentPlanet().get();
        Money fuelPrice = currentPlanet.getUnitFuelPrice();
        Money totalPrice = fuelPrice.multiply(fuelToBuy);
        Money userMoney = attributesOfPlayer.money();

        if (userMoney.isLess(totalPrice)) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy fuel: " + fuelToBuy + " but does not have enough money");
            return;
        }

        Optional<LoadableSpaceShip> spaceShipOptional = attributesOfPlayer.loadableShapeShip();
        if (spaceShipOptional.isEmpty()) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy fuel: " + fuelToBuy + " but does not have a spaceship");
            return;
        }

        LoadableSpaceShip spaceShip = spaceShipOptional.get();
        double spaceShipFuel = spaceShip.getCurrentFuel();
        double spaceShipFuelCapacity = spaceShip.getFuelCapacity();
        double spaceShipFuelAfterPurchase = spaceShipFuel + fuelToBuy;
        if (spaceShipFuelAfterPurchase > spaceShipFuelCapacity) {
            Logger.error("PlanetTradeGame: player: " + currentPlayer.getName() + " is trying to buy fuel: " + fuelToBuy + " but spaceship cannot carry that much");
            return;
        }

        synchronized (playerAttributes) {
            attributesOfPlayer.reduceMoney(totalPrice);
            spaceShip.addFuel(fuelToBuy);
        }
        Logger.release("PlanetTradeGame: player: " + currentPlayer.getName() + " bought fuel: " + fuelToBuy + " with price: " + totalPrice);
    }

    private void onJourneyPlanAction(JourneyPlanAction action) {
        JourneyPlan journeyPlan = action.journeyPlan();
        PlanetTradePlayer currentPlayer = action.player();
        journeyPlans.put(currentPlayer, Optional.of(journeyPlan));
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
