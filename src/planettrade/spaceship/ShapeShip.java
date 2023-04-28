package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.commodity.Cargo;
import planettrade.money.Money;

import java.util.List;

public final class ShapeShip {
    private final String name;
    private final Money buyPrice;
    private final int capacity;
    private final double fuelCapacity;
    private final double fuelUsagePerLightYear;
    private List<Cargo> cargos = List.of();
    private LightYear speed;
    private double currentFuel = 0d;

    private ShapeShip(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.capacity = capacity;
        this.fuelCapacity = fuelCapacity;
        this.fuelUsagePerLightYear = fuelUsagePerLightYear;
    }

    public Money getBuyPrice() {
        return buyPrice;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public double getFuelUsagePerLightYear() {
        return fuelUsagePerLightYear;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public LightYear getSpeed() {
        return speed;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public static Builder builder(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        return new Builder(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
    }

    public static class Builder {
        private final String name;
        private final Money buyPrice;
        private final int capacity;
        private final double fuelCapacity;
        private final double fuelUsagePerLightYear;

        private List<Cargo> cargos = List.of();

        private double currentFuel = 0d;

        public Builder(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
            this.name = name;
            this.buyPrice = buyPrice;
            this.capacity = capacity;
            this.fuelCapacity = fuelCapacity;
            this.fuelUsagePerLightYear = fuelUsagePerLightYear;
        }

        public Builder withCargos(List<Cargo> cargos) {
            this.cargos = cargos;
            return this;
        }

        public Builder withCurrentFuel(double currentFuel) {
            this.currentFuel = currentFuel;
            return this;
        }

        public ShapeShip build() {
            ShapeShip shapeShip = new ShapeShip(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
            shapeShip.cargos = cargos;
            shapeShip.currentFuel = currentFuel;
            return shapeShip;
        }
    }
}
