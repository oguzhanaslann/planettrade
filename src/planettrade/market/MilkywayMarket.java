package planettrade.market;

import planettrade.Commodity;

import java.util.ArrayList;
import java.util.List;

public class MilkywayMarket implements Market {

    private final ArrayList<CommodityWithAmount> commodities = new ArrayList<>();

    public MilkywayMarket(List<CommodityWithAmount> commodities) {
        this.commodities.addAll(commodities);
    }

    public MilkywayMarket() {}
    

    @Override
    public void buy(Commodity commodity, int amount) {

    }

    @Override
    public void sell(Commodity commodity, int amount) {

    }
}
