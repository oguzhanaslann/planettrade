package planettrade.market;

import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import util.Pair;

import java.util.List;

public class MarketGenerator {
    public static Market random() {
        List<Commodity> commodityList = Commodity.arbitraryCommodities();
        return new MilkywayMarket(
                commodityList.stream()
                        .map(commodity -> Pair.of(commodity, Supply.random()))
                        .toList()
        );
    }
}
