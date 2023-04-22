package planettrade.market;

import planettrade.commodity.Commodity;

public interface Market {
    void buy(Commodity commodity, int amount) throws NotEnoughSupplyException;
    double sell(Commodity commodity, int amount);
}
