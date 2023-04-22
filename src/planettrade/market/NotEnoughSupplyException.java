package planettrade.market;

public class NotEnoughSupplyException extends Exception{
    public NotEnoughSupplyException(String message) {
        super(message);
    }
}
