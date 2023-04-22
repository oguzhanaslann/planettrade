package planettrade.market;

import planettrade.commodity.Commodity;
import planettrade.commodity.Supply;
import util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MilkywayMarket implements Market {

    private Map<Commodity, Supply> supplies;

    public MilkywayMarket(Map<Commodity, Supply> supplies) {
        this.supplies = supplies;
    }

    // from pair list of Commodity and Supply
    public MilkywayMarket(List<Pair<Commodity, Supply>> supplies) {
        this.supplies = supplies
                .stream()
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }


    @Override
    public void buy(Commodity commodity, int amount) throws NotEnoughSupplyException {
        if (supplies.containsKey(commodity)) {
            Supply supply = supplies.get(commodity);
            if (supply.amount() >= amount) {
                supplies.put(commodity, supply.withAmount(supply.amount() - amount));
            } else {
                throw new NotEnoughSupplyException("Not enough supply for " + commodity.name());
            }
        }
    }

    @Override
    public double sell(Commodity commodity, int amount) {
        if (supplies.containsKey(commodity)) {
            Supply supply = supplies.get(commodity);
            supplies.put(commodity, supply.withAmount(supply.amount() + amount));
            return supply.sellPrice() * amount;
        }
        return 0;
    }
}
