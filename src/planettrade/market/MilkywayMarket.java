package planettrade.market;

import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import planettrade.money.Money;
import planettrade.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MilkywayMarket implements Market {

    private Map<Commodity, Supply> supplies;

    public MilkywayMarket(Map<Commodity, Supply> supplies) {
        this.supplies = Map.copyOf(supplies);
    }

    // from pair list of Commodity and Supply
    public MilkywayMarket(List<Pair<Commodity, Supply>> supplies) {
        this.supplies = supplies
                .stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }


    @Override
    public void buy(Commodity commodity, int amount)  {
        if (supplies.containsKey(commodity)) {
            Supply supply = supplies.get(commodity);
            supplies.put(commodity, supply.withAmount(supply.amount() + amount));
        }
    }

    @Override
    public Money sell(Commodity commodity, int amount) {
        if (supplies.containsKey(commodity)) {
            Supply supply = supplies.get(commodity);
            supplies.put(commodity, supply.withAmount(supply.amount() + amount));
            return Money.of(supply.sellPrice() * amount);
        }

        throw new IllegalStateException("Commodity not found");
    }

    @Override
    public Map<Commodity, Supply> getSupplies() {
        return Map.copyOf(supplies);
    }

    @Override
    public boolean hasSupplyOfCommodity(Commodity commodity) {
        return supplies.containsKey(commodity) && supplies.get(commodity).amount() > 0;
    }

    @Override
    public boolean hasEnoughSupplyOfCommodity(Commodity commodity, int amount) {
        return supplies.containsKey(commodity) && supplies.get(commodity).amount() >= amount;
    }

    @Override
    public double getPriceOf(Commodity commodityToBuy, int commodityAmount) {
        if (supplies.containsKey(commodityToBuy)) {
            Supply supply = supplies.get(commodityToBuy);
            return supply.buyPrice() * commodityAmount;
        }
        return 0d;
    }
}
