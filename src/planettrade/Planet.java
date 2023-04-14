package planettrade;

public abstract class Planet {
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
}
