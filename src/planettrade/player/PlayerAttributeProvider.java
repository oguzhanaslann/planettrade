package planettrade.player;

import project.gameengine.base.Player;

import java.io.Externalizable;
import java.io.Serializable;

public interface PlayerAttributeProvider {
    PlayerAttributes getAttributes(Player player);
}
