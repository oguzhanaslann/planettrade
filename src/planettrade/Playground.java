package planettrade;

import planettrade.game.PlanetTradeGame;
import planettrade.logger.LogLevel;
import planettrade.logger.Logger;
import project.gameengine.BasicConsolRenderer;
import project.gameengine.base.GameRenderer;

public class Playground {
    public static void main(String[] args) {
        Logger.seed(LogLevel.DEBUG);
        GameRenderer renderer = new BasicConsolRenderer();
        PlanetTradeGame game = new PlanetTradeGame(renderer);
        game.start();
    }
}
