package planettrade;

import planettrade.market.Market;
import planettrade.market.MarketGenerator;
import util.NumberUtils;
import util.StringUtils;

public class Planet {
    private final String name;
    private final Market market;

    private final double unitFuelPrice;

    private final double spaceShipParkingPricePerTurn;

    public Planet(String name, Market market, double unitFuelPrice, double spaceShipParkingPricePerTurn) {
        this.name = name;
        this.market = market;
        this.unitFuelPrice = unitFuelPrice;
        this.spaceShipParkingPricePerTurn = spaceShipParkingPricePerTurn;
    }

    public String getName() {
        return name;
    }

    public static Planet random() {
        return new Planet(
                StringUtils.generateRandomName(),
                MarketGenerator.random(),
                NumberUtils.random(0.1d, 1d),
                NumberUtils.random(1d, 10d)
        );
    }
}
