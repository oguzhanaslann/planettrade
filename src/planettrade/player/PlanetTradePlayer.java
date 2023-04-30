package planettrade.player;

import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import planettrade.game.actions.BuyItemAction;
import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.actions.SellCargoAction;
import planettrade.game.context.BuyShapeShipContext;
import planettrade.market.Market;
import planettrade.money.Money;
import planettrade.planet.Planet;
import planettrade.spaceship.ShapeShip;
import project.gameengine.NullAction;
import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;
import util.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A player has:
 * • name
 * • current money
 * • spaceship
 * • current planet
 * The players have read-only access to the following information during the game at each turn
 * • Their current money
 * • Their current spaceship (all attributes)
 * • The distances between each planet
 * • The market information of each planet (Market items etc.)
 * <p>
 * Each player starts the game with an initial amount of money and no spaceship
 * <p>
 * At each turn player can do the following actions
 * Buy new market items from the market of the current planet as much as the capacity of the spaceship and the supply of the market allows.
 * The buy operation causes the current money drop with the amount calculated by unit buy price of the market item in the market and the amount
 * <p>
 * Sell any cargo in the spaceship. The sell operation causes increase in the current money with amount calculated
 * by the cargo amount and unit sell price of the commodity in the market.
 * <p>
 * Buy fuel as much as the fuel capacity of the spaceship allows. It causes the current money drop with the amount
 * calculated by the unit fuel price at the current planet.
 * <p>
 * <p>
 * Plan journey to another planet. If this is done in one turn the player will be at the target planet in the next
 * turn if the spaceship has sufficient amount of fuel which is calculated by the fuel usage of the spaceship and
 * the distance between the planets. Otherwise the player stays at the same planet causing a drop in the current money by the parking price of the
 * current planet.
 */

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
                .shapeShip()
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
        final int BuyNewMarketItems = 1;
        final int SellCargo = 2;
        switch (action) {
            case BuyNewMarketItems -> {
                return getBuyItemAction();
            }
            case SellCargo -> {
                return getSellCargoAction();
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

        if (attributes.shapeShip().isEmpty()) {
            throw new IllegalStateException("Player has no shape ship");
        }

        ShapeShip currentShapeShip = attributes.shapeShip().get();

        if (currentShapeShip.hasCargoSpace()) {
            int availableCargoSpace = currentShapeShip.getAvailableCargoSpace();
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


    //Sell any cargo in the spaceship. The sell operation causes increase in the current money with amount calculated
    // by the cargo amount and unit sell price of the commodity in the market.
    private Action getSellCargoAction() {
        PlayerAttributes attributes = getAttributes();

        if (attributes.shapeShip().isEmpty()) {
            throw new IllegalStateException("Player has no shape ship");
        }

        ShapeShip shapeShip = attributes.shapeShip().get();
        List<Cargo> cargos = shapeShip.getCargos();
        if (!cargos.isEmpty()) {
            Cargo randomCargo = cargos.get(NumberUtils.random(0, cargos.size() - 1));
            return new SellCargoAction(this, randomCargo);
        }

        return new NullAction();
    }

    private PlayerAttributes getAttributes() {
        return readOnlyInfoProvider.getAttributes(this);
    }

    private BuyShapeShipAction getBuyShapeShipAction(BuyShapeShipContext context) {
        Optional<ShapeShip> shipToBuy = getShapeShipToBuy(context);
        return new BuyShapeShipAction(this, shipToBuy);
    }

    private Optional<ShapeShip> getShapeShipToBuy(BuyShapeShipContext context) {
        final BuyShapeShipContext buyShapeShipContext = context;
        final List<ShapeShip> shapeShips = buyShapeShipContext.getShapeShips();
        final Money money = getMoney();

        List<ShapeShip> affordableShapeShips = shapeShips
                .stream()
                .filter(shapeShip -> money.isGreaterOrEqual(shapeShip.getBuyPrice()))
                .toList();

        Optional<ShapeShip> shipToBuy = affordableShapeShips
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
        return "PlanetTradePlayer{" +
                "name='" + name + '\'' +
                //", shapeShip=" + playerAttributeProvider.getShapeShip() +
                //", currentPlanet=" + playerAttributeProvider.getPlanet() +
                //", money=" + playerAttributeProvider.getMoney() +
                '}';
    }
}
