package planettrade.commodity;

public class SupplyOfCommodity {
    private final int amount;
    private final double buyPrice;
    private final double sellPrice;

    private final Commodity commodity;

    public SupplyOfCommodity(Commodity commodity, int amount, double buyPrice, double sellPrice) {
        this.commodity = commodity;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }
}
