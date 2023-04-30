package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.money.Money;

import java.util.Collections;
import java.util.List;

public abstract class SpaceShip {
    private final String name;
    private final Money buyPrice;
    private final int capacity;
    private final double fuelCapacity;
    private final double fuelUsagePerLightYear;
    protected List<Cargo> cargos = Collections.emptyList();
    protected LightYear speed = LightYear.ZERO;
    protected double currentFuel = 0d;

    protected SpaceShip(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.capacity = capacity;
        this.fuelCapacity = fuelCapacity;
        this.fuelUsagePerLightYear = fuelUsagePerLightYear;
    }

    public boolean hasCargoSpace() {
        int totalCargoQuantities = getTotalCargoQuantities();
        return totalCargoQuantities < capacity;
    }

    public int getTotalCargoQuantities() {
        return cargos.stream()
                .map(Cargo::quantity)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public int getAvailableCargoSpace() {
        return capacity - getTotalCargoQuantities();
    }

    public boolean canCarry(Commodity commodityToBuy, int commodityAmount) {
        return getAvailableCargoSpace() >= commodityAmount;
    }

    public boolean canCarry(Cargo cargo) {
        return canCarry(cargo.commodity(), cargo.quantity());
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
        return cargos != null ? List.copyOf(cargos) : Collections.emptyList();
    }

    public LightYear getSpeed() {
        return speed;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }
}
