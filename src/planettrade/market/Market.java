package planettrade.market;

import planettrade.Commodity;

public interface Market {
    void buy(Commodity commodity, int amount);
    void sell(Commodity commodity, int amount);
}
