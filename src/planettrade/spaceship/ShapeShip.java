package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.money.Money;

public final class ShapeShip {
    private final String name;
    private final Money buyPrice;

    private final int capacity;

    private final LightYear speed;

    private final double fuelCapacity;

    private final double fuelUsagePerLightYear;

    private double currentFuel;

    public ShapeShip(String name, Money buyPrice, int capacity, LightYear speed, double fuelCapacity, double fuelUsagePerLightYear) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.capacity = capacity;
        this.speed = speed;
        this.fuelCapacity = fuelCapacity;
        this.fuelUsagePerLightYear = fuelUsagePerLightYear;
        this.currentFuel = fuelCapacity;
    }

    void addFuel(double fuel) {
        currentFuel += fuel;
    }

    private void setFuel(double fuel) {
        if (fuel > fuelCapacity) {
            // too much fuel
        }

        if (fuel < 0) {
            // negative fuel
        }

        currentFuel = fuel;
    }

    public Money getBuyPrice() {
        return buyPrice;
    }
}
