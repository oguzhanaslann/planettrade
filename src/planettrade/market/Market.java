package planettrade.market;

import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import planettrade.money.Money;

import java.util.Map;

public interface Market {

    Map<Commodity, Supply> getSupplies();

    void buy(Commodity commodity, int amount);
    Money sell(Commodity commodity, int amount);

    boolean hasSupplyOfCommodity(Commodity commodity);

    boolean hasEnoughSupplyOfCommodity(Commodity commodity, int amount);

    double getPriceOf(Commodity commodityToBuy, int commodityAmount);
}
