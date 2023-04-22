package planettrade.money;

public record Money(double amount) {

    public static final Money ZERO = new Money(0);

    public Money {
        if (amount < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
    }

    public Money add(Money money) {
        return new Money(zeroIfNegative(amount + money.amount));
    }

    public Money subtract(Money money) {
        return new Money(zeroIfNegative(amount - money.amount));
    }

    private double zeroIfNegative(double value) {
        return value < 0 ? 0 : value;
    }

    public Money multiply(double multiplier) {
        return new Money(zeroIfNegative(amount * multiplier));
    }

    public Money divide(double divisor) {
        return new Money(zeroIfNegative(amount / divisor));
    }
}
