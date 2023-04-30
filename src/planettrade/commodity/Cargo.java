package planettrade.commodity;

public record Cargo(Commodity commodity, int quantity) {

    public static Cargo of(Commodity commodity, int quantity) {
        return new Cargo(commodity, quantity);
    }


    public Cargo plus(Cargo cargo) {
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
}
