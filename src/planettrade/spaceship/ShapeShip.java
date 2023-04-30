package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.commodity.Cargo;
import planettrade.commodity.Commodity;
import planettrade.money.Money;

import java.util.ArrayList;
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

    public static Builder builder(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        return new Builder(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
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
        return cargos;
    }

    public LightYear getSpeed() {
        return speed;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void loadCargo(Cargo cargo) {
        if (!canCarry(cargo)) {
            throw new IllegalArgumentException("Not enough cargo space");
        }

        ArrayList<Cargo> newCargos = new ArrayList<>(cargos);
        if(newCargos.contains(cargo)) {
            newCargos.stream()
                    .filter(c -> c.equals(cargo))
                    .findFirst()
                    .ifPresent( currentCargo -> {
                        int index = newCargos.indexOf(currentCargo);
                        newCargos.set(index, currentCargo.plus(cargo));
                    });
        } else {
            newCargos.add(cargo);
        }

        cargos = List.copyOf(newCargos);
    }

    public boolean hasCargo(Cargo cargoToSell) {
        return cargos.contains(cargoToSell);
    }

    public void unloadCargo(Cargo cargoToSell) {
        if (!hasCargo(cargoToSell)) {
            throw new IllegalArgumentException("Cargo not found");
        }

        ArrayList<Cargo> newCargos = new ArrayList<>(cargos);
        boolean isRemoved = newCargos.remove(cargoToSell);
        if (!isRemoved) {
            throw new IllegalArgumentException("Cargo not found");
        }

        cargos = List.copyOf(newCargos);
    }

    @Override
    public String toString() {
        return "ShapeShip{" +
                "name='" + name + '\'' +
                ", buyPrice=" + buyPrice +
                ", capacity=" + capacity +
                ", fuelCapacity=" + fuelCapacity +
                ", fuelUsagePerLightYear=" + fuelUsagePerLightYear +
                ", cargos=" + cargos +
                ", speed=" + speed +
                ", currentFuel=" + currentFuel +
                '}';
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
