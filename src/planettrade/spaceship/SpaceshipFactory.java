package planettrade.spaceship;

import planettrade.money.Money;
import util.NumberUtils;
import util.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

public class SpaceshipFactory {


    public static<T extends SpaceShip> List<T> randomShapeShipGroupWithSize(int size, Class<T> clazz) {
        return IntStream
                .range(1, size)
                .mapToObj(i -> random(clazz))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public static<T extends SpaceShip> T random(Class<T> clazz) {
        if(clazz == LoadableSpaceShip.class) {
            double fuel = NumberUtils.random(100d, 1000d);
            double fuelCapacity = fuel;
            return (T) LoadableSpaceShip.builder(
                            StringUtils.generateRandomName(), // name
                            new Money(NumberUtils.random(3000d, 10_000d)), // buyPrice
                            NumberUtils.random(1, 10), // capacity
                            fuelCapacity, // fuelCapacity
                            NumberUtils.random(0.1d, 1d) // fuelUsagePerLightYear
                    )
                    .withCurrentFuel(fuel)
                    .build();
        }


        throw new IllegalArgumentException("Unknown spaceship class");
    }
}
