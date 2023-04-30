package planettrade.planet;

import planettrade.market.Market;
import planettrade.market.MarketGenerator;
import planettrade.money.Money;
import util.NumberUtils;
import util.StringUtils;

public class Planet {
    private final String name;
    private final Market market;

    private final Money unitFuelPrice;

    private final double spaceShipParkingPricePerTurn;

    public Planet(String name, Market market, Money unitFuelPrice, double spaceShipParkingPricePerTurn) {
        this.name = name;
        this.market = market;
        this.unitFuelPrice = unitFuelPrice;
        this.spaceShipParkingPricePerTurn = spaceShipParkingPricePerTurn;
    }

    public String getName() {
        return name;
    }

    public Market getMarket() {
        return market;
    }

    public Money getUnitFuelPrice() {
        return unitFuelPrice;
    }

    public static Planet random(
            MarketGenerator marketGenerator
    ) {
        return new Planet(
                StringUtils.generateRandomName(),
                marketGenerator.generate(),
                Money.of(NumberUtils.random(0.1d, 1d)),
                NumberUtils.random(1d, 10d)
        );
    }

    @Override
    public String toString() {
        return "Planet("+name+")";
    }
}
