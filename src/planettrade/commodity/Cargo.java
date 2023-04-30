package planettrade.commodity;

import planettrade.logger.Logger;

import java.util.Objects;

public record Cargo(Commodity commodity, int quantity) {

    public static Cargo of(Commodity commodity, int quantity) {
        return new Cargo(commodity, quantity);
    }


    public Cargo plus(Cargo cargo) {
        Objects.requireNonNull(cargo, "Cannot add null cargo");
        if (!commodity.equals(cargo.commodity())) {
            throw new IllegalArgumentException("Cannot add cargo of different commodities");
        }
        return new Cargo(commodity, quantity + cargo.quantity());
    }

    public Cargo withQuantity(int quantity) {
        return new Cargo(commodity, quantity);
    }

    public Cargo withCommodity(Commodity commodity) {
        return new Cargo(commodity, quantity);
    }

    public Cargo decay() {
        if (quantity == 0) {
            return this;
        }

        Cargo decayedCargo = new Cargo(commodity(), (int) (quantity * (1 - commodity.decayRatio())));
        Logger.info("Decayed " + quantity + " " + commodity.name() + " to " + decayedCargo.quantity());
        return decayedCargo;
    }
}
