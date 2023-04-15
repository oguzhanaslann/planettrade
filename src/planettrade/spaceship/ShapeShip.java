package planettrade.spaceship;

import planettrade.LightYear;

public class ShapeShip {
    private final String name;
    private final double buyPrice;

    private final int capacity;

    private final LightYear speed;

    private final double fuelCapacity;

    private final double fuelUsagePerLightYear;

    private double currentFuel;

    public ShapeShip(String name, double buyPrice, int capacity, LightYear speed, double fuelCapacity, double fuelUsagePerLightYear) {
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
}
