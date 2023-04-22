package planettrade.spaceship;

import planettrade.LightYear;
import planettrade.money.Money;
import util.NumberUtils;
import util.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

public class SpaceshipFactory {

    public static List<ShapeShip> randomList(int size) {
        return IntStream
                .range(1, size)
                .mapToObj(i -> random())
                .toList();
    }

    public static ShapeShip random() {
        return new ShapeShip(
                StringUtils.generateRandomName(), // name
                new Money(NumberUtils.random(3000d, 10_000d)), // buyPrice
                NumberUtils.random(1, 10), // capacity
                LightYear.random(1, 10), // speed
                NumberUtils.random(100d, 1000d), // fuelCapacity
                NumberUtils.random(0.1d, 1d) // fuelUsagePerLightYear
        );
    }
}
