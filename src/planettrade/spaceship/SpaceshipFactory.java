package planettrade.spaceship;

import planettrade.money.Money;
import util.NumberUtils;
import util.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

public class SpaceshipFactory {

    public static List<ShapeShip> randomShapeShipGroupWithSize(int size) {
        return IntStream
                .range(1, size)
                .mapToObj(i -> random())
                .toList();
    }

    public static ShapeShip random() {
        double fuel = NumberUtils.random(100d, 1000d);
        double fuelCapacity = fuel;
        return ShapeShip.builder(
                        StringUtils.generateRandomName(), // name
                        new Money(NumberUtils.random(3000d, 10_000d)), // buyPrice
                        NumberUtils.random(1, 10), // capacity
                         fuelCapacity, // fuelCapacity
                        NumberUtils.random(0.1d, 1d) // fuelUsagePerLightYear
                )
                .withCurrentFuel(fuel)
                .build();
    }
}
