package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.commodity.Cargo;
import planettrade.money.Money;

import java.util.ArrayList;
import java.util.List;

public final class LoadableSpaceShip extends SpaceShip {

    public LoadableSpaceShip(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        super(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
    }

    public static LoadableSpaceShip from(SpaceShip spaceShip) {
        double currentFuel = spaceShip.getCurrentFuel();
        List<Cargo> cargos = List.copyOf(spaceShip.getCargos());
        LoadableSpaceShip instance = new LoadableSpaceShip(
                spaceShip.getName(),
                spaceShip.getBuyPrice(),
                spaceShip.getCapacity(),
                spaceShip.getFuelCapacity(),
                spaceShip.getFuelUsagePerLightYear()
        );

        instance.speed = LightYear.ZERO;
        instance.currentFuel = currentFuel;
        instance.cargos = cargos;

        return instance;
    }

    public static Builder builder(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
        return new Builder(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
    }

    public void loadCargo(Cargo cargo) {
        if (!canCarry(cargo)) {
            throw new IllegalArgumentException("Not enough cargo space");
        }

        ArrayList<Cargo> newCargos = new ArrayList<>(getCargos());
        if (newCargos.contains(cargo)) {
            newCargos.stream()
                    .filter(c -> c.equals(cargo))
                    .findFirst()
                    .ifPresent(currentCargo -> {
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

        ArrayList<Cargo> newCargos = new ArrayList<>(getCargos());
        boolean isRemoved = newCargos.remove(cargoToSell);
        if (!isRemoved) {
            throw new IllegalArgumentException("Cargo not found");
        }

        cargos = List.copyOf(newCargos);
    }

    public void addFuel(double fuel) {
        if (fuel < 0) {
            throw new IllegalArgumentException("Fuel cannot be negative");
        }

        if (fuel > getFuelCapacity()) {
            throw new IllegalArgumentException("Fuel cannot be more than fuel capacity");
        }

        currentFuel += fuel;
    }

    public void reduceFuel(double fuel) {
        if (fuel < 0) {
            throw new IllegalArgumentException("Fuel cannot be negative");
        }

        if (fuel > currentFuel) {
            throw new IllegalArgumentException("Fuel cannot be more than current fuel");
        }

        currentFuel -= fuel;
    }

    public void decayCargos() {
        ArrayList<Cargo> newCargos = new ArrayList<>(getCargos());
        newCargos.forEach(cargo -> {
            int index = newCargos.indexOf(cargo);
            newCargos.set(index, cargo.decay());
        });

        cargos = List.copyOf(newCargos);
    }

    public static class Builder {
        private String name;
        private Money buyPrice;
        private int capacity;
        private double fuelCapacity;
        private double fuelUsagePerLightYear;
        private double currentFuel;
        private List<Cargo> cargos;
        private LightYear speed;

        public Builder(String name, Money buyPrice, int capacity, double fuelCapacity, double fuelUsagePerLightYear) {
            this.name = name;
            this.buyPrice = buyPrice;
            this.capacity = capacity;
            this.fuelCapacity = fuelCapacity;
            this.fuelUsagePerLightYear = fuelUsagePerLightYear;
        }

        public Builder withCurrentFuel(double currentFuel) {
            this.currentFuel = currentFuel;
            return this;
        }

        public Builder withCargos(List<Cargo> cargos) {
            this.cargos = cargos;
            return this;
        }

        public Builder withSpeed(LightYear speed) {
            this.speed = speed;
            return this;
        }

        public LoadableSpaceShip build() {
            LoadableSpaceShip instance = new LoadableSpaceShip(name, buyPrice, capacity, fuelCapacity, fuelUsagePerLightYear);
            instance.currentFuel = currentFuel;
            instance.cargos = cargos;
            instance.speed = speed;
            return instance;
        }
    }
}
