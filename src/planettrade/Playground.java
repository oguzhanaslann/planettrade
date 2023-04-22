package planettrade;

import planettrade.game.PlanetTradeGame;
import planettrade.logger.LogLevel;
import planettrade.logger.Logger;
import planettrade.player.PlanetTradePlayer;
import project.gameengine.BasicConsolRenderer;
import project.gameengine.base.GameRenderer;
import util.NumberUtils;

import java.util.List;
import java.util.stream.IntStream;

public class Playground {
    public static final int MAXIMUM_PLAYER_COUNT = 10;
    public static final int MINIMUM_PLAYER_COUNT = 1;

    public static void main(String[] args) {
        Logger.seed(LogLevel.DEBUG);

        List<PlanetTradePlayer> players = IntStream
                .range(MINIMUM_PLAYER_COUNT, NumberUtils.random(MINIMUM_PLAYER_COUNT, MAXIMUM_PLAYER_COUNT))
                .mapToObj(i -> new PlanetTradePlayer("Player - " + i, PlanetTradeGame.initialMoney))
                .peek(i -> Logger.debug("created player: " + i))
                .toList();

        GameRenderer renderer = new BasicConsolRenderer();
        PlanetTradeGame game = new PlanetTradeGame(renderer, players);
        game.start();
    }
}
