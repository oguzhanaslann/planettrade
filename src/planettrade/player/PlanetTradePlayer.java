package planettrade.player;

import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import planettrade.game.actions.BuyFuelAction;
import planettrade.game.actions.BuyItemAction;
import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.actions.SellCargoAction;
import planettrade.game.actions.journey.JourneyPlan;
import planettrade.game.actions.journey.JourneyPlanAction;
import planettrade.game.context.BuyShapeShipContext;
import planettrade.market.Market;
import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.SpaceShip;
import project.gameengine.NullAction;
import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;
import planettrade.util.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PlanetTradePlayer implements Player {

    private final String name;

    private PlayerReadOnlyInfoProvider readOnlyInfoProvider;

    public PlanetTradePlayer(String name, PlayerReadOnlyInfoProvider readOnlyInfoProvider) {
        this.name = name;
        this.readOnlyInfoProvider = readOnlyInfoProvider;
    }


    public boolean hasShapeShip() {
        return readOnlyInfoProvider
                .getAttributes(this)
                .spaceShip()
                .isPresent();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Action play(GameContext context) {

        if (context == null) {
            return new NullAction();
        }

        if (context instanceof BuyShapeShipContext) {
            return getBuyShapeShipAction((BuyShapeShipContext) context);
        }

        return getRandomPlayerAction(context);
    }

    private Action getRandomPlayerAction(GameContext context) {
        int action = NumberUtils.random(1, 4);
        assert action >= 1 && action <= 4;
        final int BuyNewMarketItems = 1;
        final int SellCargo = 2;
        final int BuyFuel = 3;
        final int JourneyPlan = 4;
        switch (action) {
            case BuyNewMarketItems -> {
                return getBuyItemAction();
            }
            case SellCargo -> {
                return getSellCargoAction();
            }
            case BuyFuel -> {
                return getBuyFuelAction();
            }
            case JourneyPlan -> {
                return getJourneyPlanAction();
            }
            default -> {
                return new NullAction();
            }
        }
    }

    private Action getBuyItemAction() {
        PlayerAttributes attributes = getAttributes();

        if (attributes.currentPlanet().isEmpty()) {
            throw new IllegalStateException("Player has no current planet");
        }

        Planet currentPlanet = attributes.currentPlanet().get();
        Market currentPlanetMarket = currentPlanet.getMarket();
        Map<Commodity, Supply> supplies = currentPlanetMarket.getSupplies();

        List<Commodity> existingSupplies = supplies
                .keySet()
                .stream()
                .filter(commodity -> supplies.get(commodity).amount() > 0)
                .toList();

        if (attributes.spaceShip().isEmpty()) {
            throw new IllegalStateException("Player has no shape ship");
        }

        SpaceShip currentSpaceShip = attributes.spaceShip().get();

        if (currentSpaceShip.hasCargoSpace()) {
            int availableCargoSpace = currentSpaceShip.getAvailableCargoSpace();
            int randomIndex = NumberUtils.random(0, existingSupplies.size() - 1);
            Commodity commodityToBuy = existingSupplies.get(randomIndex);
            Supply supply = supplies.get(commodityToBuy);
            int randomAmountToBuy = NumberUtils.random(1, Math.min(availableCargoSpace, supply.amount()));
            return new BuyItemAction(
                    this,
                    commodityToBuy,
                    randomAmountToBuy
            );
        }

        return new NullAction();
    }

    private Action getSellCargoAction() {
        PlayerAttributes attributes = getAttributes();

        if (attributes.spaceShip().isEmpty()) {
            throw new IllegalStateException("Player has no shape ship");
        }

        SpaceShip spaceShip = attributes.spaceShip().get();
        List<Cargo> cargos = spaceShip.getCargos();
        if (!cargos.isEmpty()) {
            Cargo randomCargo = cargos.get(NumberUtils.random(0, cargos.size() - 1));
            return new SellCargoAction(this, randomCargo);
        }

        return new NullAction();
    }

    private Action getBuyFuelAction() {
        PlayerAttributes attributes = getAttributes();
        if (attributes.spaceShip().isEmpty()) {
            throw new IllegalStateException("Player has no shape ship");
        }

        SpaceShip spaceShip = attributes.spaceShip().get();
        double currentFuel = spaceShip.getCurrentFuel();
        double fuelCapacity = spaceShip.getFuelCapacity();
        double fuelToBuy = fuelCapacity - currentFuel;
        if (fuelToBuy > 0) {
            return new BuyFuelAction(this, fuelToBuy);
        }

        return new NullAction();
    }

    private Action getJourneyPlanAction() {
        Optional<Planet> currentPlanetOptional = getAttributes().currentPlanet();
        if (currentPlanetOptional.isEmpty()) {
            throw new IllegalStateException("Player has no current planet");
        }

        Planet currentPlanet = currentPlanetOptional.get();

        Set<Planet> planets = readOnlyInfoProvider.getPlanets();
        List<Planet> planetList = planets.stream()
                .filter(planet -> !planet.equals(currentPlanet))
                .toList();
        int randomIndex = NumberUtils.random(0, planetList.size() - 1);
        Planet targetPlanet = planetList.get(randomIndex);
        return new JourneyPlanAction(
                this,
                new JourneyPlan(this, currentPlanet, targetPlanet)
        );
    }

    private PlayerAttributes getAttributes() {
        return readOnlyInfoProvider.getAttributes(this);
    }

    private BuyShapeShipAction getBuyShapeShipAction(BuyShapeShipContext context) {
        Optional<SpaceShip> shipToBuy = getShapeShipToBuy(context);
        return new BuyShapeShipAction(this, shipToBuy);
    }

    private Optional<SpaceShip> getShapeShipToBuy(BuyShapeShipContext context) {
        final BuyShapeShipContext buyShapeShipContext = context;
        final List<SpaceShip> spaceShips = buyShapeShipContext.getShapeShips();
        final Money money = getMoney();

        List<SpaceShip> affordableSpaceShips = spaceShips
                .stream()
                .filter(shapeShip -> money.isGreaterOrEqual(shapeShip.getBuyPrice()))
                .toList();

        Optional<SpaceShip> shipToBuy = affordableSpaceShips
                .stream()
                .findFirst();
        return shipToBuy;
    }

    private Money getMoney() {
        return readOnlyInfoProvider
                .getAttributes(this)
                .money();
    }

    @Override
    public void prepareForGame(GameContext context) {
    }

    @Override
    public String toString() {
        return "PlanetTradePlayer{" + "name='" + name + '\'' + '}';
    }
}
