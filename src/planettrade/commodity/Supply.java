package planettrade.commodity;

import planettrade.util.NumberUtils;

public record Supply(int amount, double buyPrice, double sellPrice) {
    public static Supply random() {
        double price = NumberUtils.random(1, 10);
        return new Supply(
                NumberUtils.random(1, 100),
                price,
                price * NumberUtils.random(0.5, 1.5)
        );
    }

    public Supply withAmount(int amount) {
        return new Supply(amount, buyPrice, sellPrice);
    }
}
