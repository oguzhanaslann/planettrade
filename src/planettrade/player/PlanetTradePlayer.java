package planettrade.player;

import planettrade.game.actions.BuyShapeShipAction;
import planettrade.game.context.BuyShapeShipContext;
import planettrade.money.Money;
import planettrade.spaceship.ShapeShip;
import project.gameengine.NullAction;
import project.gameengine.base.Action;
import project.gameengine.base.GameContext;
import project.gameengine.base.Player;

import java.util.List;
import java.util.Optional;

/**
 * A player has:
 *      • name
 *      • current money
 *      • spaceship
 *      • current planet
 *  The players have read-only access to the following information during the game at each turn
 *      • Their current money
 *      • Their current spaceship (all attributes)
 *      • The distances between each planet
 *      • The market information of each planet (Market items etc.)
 *
 *  Each player starts the game with an initial amount of money and no spaceship
 *
 *  At each turn player can do the following actions
 *      Buy new market items from the market of the current planet as much as the capacity of the spaceship and the supply of the market allows.
 *      The buy operation causes the current money drop with the amount calculated by unit buy price of the market item in the market and the amount
 *
 *      Sell any cargo in the spaceship. The sell operation causes increase in the current money with amount calculated
 *      by the cargo amount and unit sell price of the commodity in the market.
 *
 *      Buy fuel as much as the fuel capacity of the spaceship allows. It causes the current money drop with the amount
 *      calculated by the unit fuel price at the current planet.
 *
 *
 *      Plan journey to another planet. If this is done in one turn the player will be at the target planet in the next
 *      turn if the spaceship has sufficient amount of fuel which is calculated by the fuel usage of the spaceship and
 *      the distance between the planets. Otherwise the player stays at the same planet causing a drop in the current money by the parking price of the
 *      current planet.
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
        if (context instanceof BuyShapeShipContext) {
            final BuyShapeShipContext buyShapeShipContext = (BuyShapeShipContext) context;
            final List<ShapeShip> shapeShips = buyShapeShipContext.getShapeShips();
            final Money money = getMoney();
            List<ShapeShip> affordableShapeShips = shapeShips
                    .stream()
                    .filter(shapeShip -> money.isGreaterOrEqual(shapeShip.getBuyPrice()))
                    .toList();
            Optional<ShapeShip> shipToBuy = affordableShapeShips
                    .stream()
                    .findFirst();

            return new BuyShapeShipAction(this, shipToBuy);
        }

        return new NullAction();
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
