package planettrade.market;

import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import planettrade.util.Pair;

import java.util.List;
import java.util.function.Supplier;

public class MarketGenerator {

    private Supplier<List<Commodity>> commoditySupplier;

    public MarketGenerator(Supplier<List<Commodity>> commoditySupplier) {
        this.commoditySupplier = commoditySupplier;
    }

    public Market generate() {
        final List<Commodity> commodityList = commoditySupplier.get();
        return new MilkywayMarket(
                commodityList.stream()
                        .map(commodity -> Pair.of(commodity, Supply.random()))
                        .toList()
        );
    }
}
